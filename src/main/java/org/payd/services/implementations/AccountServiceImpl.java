package org.payd.services.implementations;

import org.payd.data.models.User;
import org.payd.data.models.WalletBalance;
import org.payd.data.repositories.UserRepository;
import org.payd.data.repositories.WalletBalanceRepository;
import org.payd.services.interfaces.AccountService;
import org.payd.services.interfaces.WalletEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final WalletBalanceRepository balanceRepository;
    private final WalletEncryptionService walletEncryptionService;

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, WalletBalanceRepository balanceRepository, WalletEncryptionService walletEncryptionService) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
        this.walletEncryptionService = walletEncryptionService;
    }

    @Override
    @Transactional
    public User createWalletAndBalance(User userAccount) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        // 1. Generate Polygon-compatible keypair
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();

        String polygonAddress =
                "0x" + Keys.getAddress(ecKeyPair.getPublicKey());

        // 2. Encrypt private key
        String privateKeyHex = ecKeyPair.getPrivateKey().toString(16);
        String encryptedPrivateKey =
                walletEncryptionService.encrypt(privateKeyHex);

        // 3. Persist user wallet data
        userAccount.setPolygonAddress(polygonAddress);
        userAccount.setEncryptedPrivateKey(encryptedPrivateKey);
        userRepository.save(userAccount);

        // 4. Initialize wallet balances
        WalletBalance balance = WalletBalance.builder()
                .user(userAccount)
                .currency("USDC")
                .stablecoinBalance(BigDecimal.ZERO)
                .nairaEscrowBalance(BigDecimal.ZERO)
                .build();

        balanceRepository.save(balance);

        System.out.println("✅ Generated Polygon wallet for user: " + polygonAddress);

        return userAccount;
    }

}

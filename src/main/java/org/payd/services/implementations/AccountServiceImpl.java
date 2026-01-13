package org.payd.services.implementations;

import org.payd.data.models.User;
import org.payd.data.models.WalletBalance;
import org.payd.data.repositories.UserRepository;
import org.payd.data.repositories.WalletBalanceRepository;
import org.payd.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final WalletBalanceRepository balanceRepository;

    private static final String SYSTEM_WALLET_PASSWORD = UUID.randomUUID().toString();

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, WalletBalanceRepository balanceRepository) {
        this.userRepository = userRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    @Transactional
    public User createWalletAndBalance(User userAccount) throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String publicKey = Keys.getAddress(ecKeyPair.getPublicKey());
        String polygonAddress = "0x" + publicKey;

        Path tempDir = Files.createTempDirectory("payd-wallets");
        String walletFileName = WalletUtils.generateWalletFile(
                SYSTEM_WALLET_PASSWORD, ecKeyPair, tempDir.toFile(), false);

        userAccount.setPolygonAddress(polygonAddress);
        userRepository.save(userAccount);

        WalletBalance balance = WalletBalance.builder()
                .user(userAccount)
                .currency("USDC")
                .stablecoinBalance(BigDecimal.ZERO)
                .nairaEscrowBalance(BigDecimal.ZERO)
                .build();
        balanceRepository.save(balance);
        System.out.println("✅ Generated Polygon Address for user: " + polygonAddress);

        return userAccount;
    }
}

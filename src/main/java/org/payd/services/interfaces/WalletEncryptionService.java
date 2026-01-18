package org.payd.services.interfaces;

public interface WalletEncryptionService {

    String encrypt(String plainText);
    String decrypt(String plainText);
}

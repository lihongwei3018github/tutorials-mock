package org.example.cipher;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;

/**
 * 加密器类，提供加密和解密功能。
 */
public class Encryptor {

    /**
     * 使用AES算法加密消息。
     *
     * @param message 要加密的原始消息。
     * @param keyBytes AES加密的密钥。
     * @return 加密后的消息。
     * @throws InvalidKeyException         如果密钥无效。
     * @throws NoSuchPaddingException      如果指定的填充方式不存在。
     * @throws NoSuchAlgorithmException     如果指定的算法不存在。
     * @throws BadPaddingException         如果解密后的数据的填充不正确。
     * @throws IllegalBlockSizeException   如果解密后的数据的块大小不正确。
     */
    public byte[] encryptMessage(byte[] message, byte[] keyBytes) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        // 实例化AES加密器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 创建AES密钥
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        // 初始化加密模式下的cipher
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 执行加密操作
        return cipher.doFinal(message);
    }

    /**
     * 使用RSA算法加密消息。
     *
     * @param message        要加密的原始消息。
     * @param publicKeyCertificate 公钥证书，用于获取公钥。
     * @return 加密后的消息。
     * @throws InvalidKeyException         如果密钥无效。
     * @throws NoSuchPaddingException      如果指定的填充方式不存在。
     * @throws NoSuchAlgorithmException     如果指定的算法不存在。
     * @throws BadPaddingException         如果解密后的数据的填充不正确。
     * @throws IllegalBlockSizeException   如果解密后的数据的块大小不正确。
     */
    public byte[] encryptMessage(byte[] message, Certificate publicKeyCertificate) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        // 实例化RSA加密器
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // 初始化加密模式下的cipher，使用公钥证书中的公钥
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyCertificate);
        // 执行加密操作
        return cipher.doFinal(message);
    }

    /**
     * 使用AES算法解密消息。
     *
     * @param encryptedMessage 要解密的加密消息。
     * @param keyBytes         AES解密的密钥。
     * @return 解密后的原始消息。
     * @throws NoSuchPaddingException      如果指定的填充方式不存在。
     * @throws NoSuchAlgorithmException     如果指定的算法不存在。
     * @throws InvalidKeyException         如果密钥无效。
     * @throws BadPaddingException         如果解密后的数据的填充不正确。
     * @throws IllegalBlockSizeException   如果解密后的数据的块大小不正确。
     */
    public byte[] decryptMessage(byte[] encryptedMessage, byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 实例化AES解密器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 创建AES密钥
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        // 初始化解密模式下的cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 执行解密操作
        return cipher.doFinal(encryptedMessage);
    }

}


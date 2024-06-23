/**
 * JavaKeyStore类用于管理和操作Java密钥库（KeyStore）。
 * 它提供了一系列方法来创建、加载、修改和删除密钥库中的条目，包括私钥、公钥证书等。
 */
package org.example.keystore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/**
 * JavaKeyStore类的构造器、方法注释如下：
 */
public class JavaKeyStore {

    // Java密钥库实例
    private KeyStore keyStore;
    // 密钥库文件名
    private String keyStoreName;
    // 密钥库类型
    private String keyStoreType;
    // 密钥库的密码
    private String keyStorePassword;

    /**
     * 构造函数初始化JavaKeyStore对象。
     *
     * @param keyStoreType 密钥库的类型，如JKS、PKCS12等。
     * @param keyStorePassword 密钥库的密码。
     * @param keyStoreName 密钥库文件的名称。
     * @throws CertificateException 如果处理证书时发生错误。
     * @throws NoSuchAlgorithmException 如果获取密钥库实例时，指定的算法不存在。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     * @throws IOException 如果读写文件时发生错误。
     */
    JavaKeyStore(String keyStoreType, String keyStorePassword, String keyStoreName) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        this.keyStoreName = keyStoreName;
        this.keyStoreType = keyStoreType;
        this.keyStorePassword = keyStorePassword;
    }

    /**
     * 创建一个空的密钥库并保存到文件系统。
     *
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     * @throws CertificateException 如果处理证书时发生错误。
     * @throws NoSuchAlgorithmException 如果获取密钥库实例时，指定的算法不存在。
     * @throws IOException 如果读写文件时发生错误。
     */
    void createEmptyKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        // 使用默认类型，如果未指定密钥库类型
        if(keyStoreType == null || keyStoreType.isEmpty()){
            keyStoreType = KeyStore.getDefaultType();
        }
        keyStore = KeyStore.getInstance(keyStoreType);
        // 初始化密钥库，密码为空
        char[] pwdArray = keyStorePassword.toCharArray();
        keyStore.load(null, pwdArray);

        // 保存密钥库到文件
        FileOutputStream fos = new FileOutputStream(keyStoreName);
        keyStore.store(fos, pwdArray);
        fos.close();
    }

    /**
     * 从文件系统加载密钥库。
     *
     * @throws IOException 如果读取文件时发生错误。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     * @throws NoSuchAlgorithmException 如果获取密钥库实例时，指定的算法不存在。
     * @throws CertificateException 如果处理证书时发生错误。
     */
    void loadKeyStore() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        char[] pwdArray = keyStorePassword.toCharArray();
        FileInputStream fis = new FileInputStream(keyStoreName);
        keyStore.load(fis, pwdArray);
        fis.close();
    }

    /**
     * 向密钥库中设置一个密钥条目。
     *
     * @param alias 条目的别名。
     * @param secretKeyEntry 包含秘密密钥的条目。
     * @param protectionParameter 保护参数，通常为密码。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    void setEntry(String alias, KeyStore.SecretKeyEntry secretKeyEntry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        keyStore.setEntry(alias, secretKeyEntry, protectionParameter);
    }

    /**
     * 从密钥库中获取一个条目。
     *
     * @param alias 条目的别名。
     * @return 密钥库中的条目。
     * @throws UnrecoverableEntryException 如果条目无法恢复。
     * @throws NoSuchAlgorithmException 如果获取条目时，指定的算法不存在。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    KeyStore.Entry getEntry(String alias) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
        return keyStore.getEntry(alias, protParam);
    }

    /**
     * 向密钥库中设置一个私钥条目，包括私钥和证书链。
     *
     * @param alias 条目的别名。
     * @param privateKey 私钥。
     * @param keyPassword 私钥的密码。
     * @param certificateChain 证书链。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    void setKeyEntry(String alias, PrivateKey privateKey, String keyPassword, Certificate[] certificateChain) throws KeyStoreException {
        keyStore.setKeyEntry(alias, privateKey, keyPassword.toCharArray(), certificateChain);
    }

    /**
     * 向密钥库中设置一个证书条目。
     *
     * @param alias 条目的别名。
     * @param certificate 证书。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    void setCertificateEntry(String alias, Certificate certificate) throws KeyStoreException {
        keyStore.setCertificateEntry(alias, certificate);
    }

    /**
     * 从密钥库中获取一个证书。
     *
     * @param alias 证书的别名。
     * @return 密钥库中的证书。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    Certificate getCertificate(String alias) throws KeyStoreException {
        return keyStore.getCertificate(alias);
    }

    /**
     * 从密钥库中删除一个条目。
     *
     * @param alias 要删除的条目的别名。
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     */
    void deleteEntry(String alias) throws KeyStoreException {
        keyStore.deleteEntry(alias);
    }

    /**
     * 删除密钥库中的所有条目，并从文件系统中删除密钥库文件。
     *
     * @throws KeyStoreException 如果操作密钥库时发生错误。
     * @throws IOException 如果删除文件时发生错误。
     */
    void deleteKeyStore() throws KeyStoreException, IOException {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            keyStore.deleteEntry(alias);
        }
        keyStore = null;

        Path keyStoreFile = Paths.get(keyStoreName);
        Files.delete(keyStoreFile);
    }

    /**
     * 获取密钥库的实例。
     *
     * @return 密钥库实例。
     */
    KeyStore getKeyStore() {
        return this.keyStore;
    }
}

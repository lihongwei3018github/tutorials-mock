package org.example.pem;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Java安全工具类，提供读取PEM格式的RSA私钥和公钥的方法。
 */
public class JavaSecurityPemUtils {

    /**
     * 从PEM格式的文件中读取RSA私钥。
     * 私钥通常以PKCS8格式编码，并在文件中以Base64编码的形式包含在"-----BEGIN PRIVATE KEY-----"和"-----END PRIVATE KEY-----"标签之间。
     *
     * @param file 私钥文件
     * @return 解析后的RSAPrivateKey对象
     * @throws GeneralSecurityException 如果密钥解码或生成密钥对象失败
     * @throws IOException 如果读取文件失败
     */
    public static RSAPrivateKey readPKCS8PrivateKey(File file) throws GeneralSecurityException, IOException {
        // 读取文件内容并转换为字符串
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        // 移除PEM头部和尾部标签，以及行间隔，准备进行Base64解码
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        // 对私钥进行Base64解码
        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        // 使用RSA算法实例化KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 创建PKCS8EncodedKeySpec对象，指定解码后的密钥字节序列
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        // 通过KeyFactory生成RSAPrivateKey对象
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从PEM格式的文件中读取RSA公钥。
     * 公钥通常以X.509格式编码，并在文件中以Base64编码的形式包含在"-----BEGIN PUBLIC KEY-----"和"-----END PUBLIC KEY-----"标签之间。
     *
     * @param file 公钥文件
     * @return 解析后的RSAPublicKey对象
     * @throws GeneralSecurityException 如果密钥解码或生成密钥对象失败
     * @throws IOException 如果读取文件失败
     */
    public static RSAPublicKey readX509PublicKey(File file) throws GeneralSecurityException, IOException {
        // 读取文件内容并转换为字符串
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        // 移除PEM头部和尾部标签，以及行间隔，准备进行Base64解码
        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        // 对公钥进行Base64解码
        byte[] encoded = Base64.decodeBase64(publicKeyPEM);

        // 使用RSA算法实例化KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 创建X509EncodedKeySpec对象，指定解码后的密钥字节序列
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        // 通过KeyFactory生成RSAPublicKey对象
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

}

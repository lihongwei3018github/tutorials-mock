package org.example.cipher;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * EncryptorTest 类用于测试 Encryptor 的加密和解密功能。
 */
public class EncryptorTest {

    private String encKeyString;

    private String message;

    private String certificateString;

    private Encryptor encryptor;

    /**
     * 初始化测试数据和 Encryptor 实例。
     */
    @Before
    public void init(){
        encKeyString = "1234567890123456";
        message = "This is a secret message";
        encryptor = new Encryptor();
        certificateString = "-----BEGIN CERTIFICATE-----\n" +
                "MIICVjCCAb8CAg37MA0GCSqGSIb3DQEBBQUAMIGbMQswCQYDVQQGEwJKUDEOMAwG\n" +
                "A1UECBMFVG9reW8xEDAOBgNVBAcTB0NodW8ta3UxETAPBgNVBAoTCEZyYW5rNERE\n" +
                "MRgwFgYDVQQLEw9XZWJDZXJ0IFN1cHBvcnQxGDAWBgNVBAMTD0ZyYW5rNEREIFdl\n" +
                "YiBDQTEjMCEGCSqGSIb3DQEJARYUc3VwcG9ydEBmcmFuazRkZC5jb20wHhcNMTIw\n" +
                "ODIyMDUyNzIzWhcNMTcwODIxMDUyNzIzWjBKMQswCQYDVQQGEwJKUDEOMAwGA1UE\n" +
                "CAwFVG9reW8xETAPBgNVBAoMCEZyYW5rNEREMRgwFgYDVQQDDA93d3cuZXhhbXBs\n" +
                "ZS5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMYBBrx5PlP0WNI/ZdzD\n" +
                "++<KEY>" +
                "<KEY>" +
                "<KEY>" +
                "<KEY>" +
                "-----END CERTIFICATE-----";
    }

    /**
     * 测试使用加密密钥加密消息。
     * 验证加密后的消息不为空且长度为32的倍数。
     * @throws Exception 如果加密过程中发生错误。
     */
    @Test
    public void givenEncryptionKey_whenMessageIsPassedToEncryptor_thenMessageIsEncrypted() throws Exception {
        byte[] encryptedMessage = encryptor.encryptMessage(message.getBytes(), encKeyString.getBytes());

        assertThat(encryptedMessage).isNotNull();
        assertThat(encryptedMessage.length % 32).isEqualTo(0);
    }

    /**
     * 测试使用X.509证书加密消息。
     * 验证加密后的消息不为空且长度为128的倍数。
     * @throws Exception 如果加密过程中发生错误。
     */
    @Test
    public void givenCertificateWithPublicKey_whenMessageIsPassedToEncryptor_thenMessageIsEncrypted() throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(certificateString.getBytes());
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(is);

        byte[] encryptedMessage = encryptor.encryptMessage(message.getBytes(), certificate);

        assertThat(encryptedMessage).isNotNull();
        assertThat(encryptedMessage.length % 128).isEqualTo(0);
    }

    /**
     * 测试加密后能否成功解密消息。
     * 验证解密后的消息与原始消息一致。
     * @throws Exception 如果加密或解密过程中发生错误。
     */
    @Test
    public void givenEncryptionKey_whenMessageIsEncrypted_thenDecryptMessage() throws Exception{
        byte[] encryptedMessageBytes = encryptor.encryptMessage(message.getBytes(), encKeyString.getBytes());

        byte[] clearMessageBytes = encryptor.decryptMessage(encryptedMessageBytes, encKeyString.getBytes());

        assertThat(message).isEqualTo(new String(clearMessageBytes));
    }

}

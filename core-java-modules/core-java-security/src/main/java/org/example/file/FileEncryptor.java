package org.example.file;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 文件加密器类，用于对文件内容进行加密和解密。
 */
public class FileEncryptor {

    private SecretKey secretKey; // 私钥，用于加密和解密
    private Cipher cipher; // 加密/解密器

    /**
     * 构造函数，初始化加密/解密器。
     *
     * @param secretKey 加密使用的私钥
     * @param cipher    加密算法/模式/填充方式
     * @throws NoSuchPaddingException 如果指定的填充名称不存在
     * @throws NoSuchAlgorithmException 如果指定的算法不存在
     */
    FileEncryptor(SecretKey secretKey, String cipher) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(cipher);
    }

    /**
     * 加密给定内容并写入指定文件。
     *
     * @param content   需要加密的内容
     * @param fileName  加密后内容输出的文件名
     * @throws InvalidKeyException     如果密钥无效
     * @throws IOException            如果文件读写发生错误
     */
    public void encrypt(String content, String fileName) throws InvalidKeyException, IOException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        try (
                FileOutputStream fileOut = new FileOutputStream(fileName);
                CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)
        ) {
            fileOut.write(iv);
            cipherOut.write(content.getBytes());
        }

    }

    /**
     * 从指定文件中读取加密内容并解密。
     *
     * @param fileName 加密内容输入的文件名
     * @return 解密后的字符串内容
     * @throws InvalidAlgorithmParameterException 如果算法参数无效
     * @throws InvalidKeyException                 如果密钥无效
     * @throws IOException                        如果文件读写发生错误
     */
    public String decrypt(String fileName) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException {

        String content;

        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));

            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader)
            ) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
            }

        }
        return content;
    }
}

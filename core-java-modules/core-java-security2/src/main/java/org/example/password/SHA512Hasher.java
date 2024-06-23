package org.example.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA512Hasher类提供了SHA-512散列算法的实现，用于对密码进行加密。
 * 这种散列算法具有较高的安全性，适用于存储敏感信息，如用户密码。
 */
public class SHA512Hasher {

  /**
   * 使用SHA-512散列算法对给定的密码进行散列。
   *
   * @param passwordToHash 需要被散列的密码字符串。
   * @param salt 用于加强散列安全性的盐值字节数组。
   * @return 返回散列后的密码字符串。
   */
  public String hash(String passwordToHash, byte[] salt){
    String generatedPassword = null;
    try {
      // 实例化SHA-512散列算法的消息摘要对象
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      // 使用盐值更新消息摘要
      md.update(salt);
      // 对密码字符串进行散列处理
      byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
      // 将散列结果转换为十六进制字符串
      StringBuilder sb = new StringBuilder();
      for(int i=0; i< bytes.length ;i++){
        // 将每个字节转换为十六进制字符串，并确保字符串长度为2
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }
      generatedPassword = sb.toString();
    }
    catch (NoSuchAlgorithmException e){
      // 如果散列算法不存在，打印堆栈跟踪
      e.printStackTrace();
    }
    return generatedPassword;
  }

  /**
   * 验证给定的密码字符串和盐值是否与预散列的散列值匹配。
   *
   * @param hash 预先散列的密码字符串。
   * @param attempt 待验证的密码字符串。
   * @param salt 用于散列的盐值字节数组。
   * @return 如果散列后的密码与预散列值匹配，则返回true；否则返回false。
   */
  public boolean checkPassword(String hash, String attempt, byte[] salt){
    // 使用给定的密码和盐值重新散列，并与预散列值进行比较
    String generatedHash = hash(attempt, salt);
    return hash.equals(generatedHash);
  }

}

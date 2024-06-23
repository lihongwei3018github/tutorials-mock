package org.example.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * 密码存储哈希工具类，用于存储密码的哈希值及验证密码是否与存储的哈希令牌匹配。
 *
 * 该类的实例可以在多线程环境下安全地并发使用。
 *
 * @author erickson
 * @see <a href="http://stackoverflow.com/a/2861125/3474">StackOverflow</a>
 */
public final class PBKDF2Hasher
{

  /**
   * 本类产生的每个令牌都以此标识符作为前缀。
   */
  public static final String ID = "$31$";

  /**
   * 默认推荐的成本值，用于默认情况
   */
  public static final int DEFAULT_COST = 16;

  private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

  private static final int SIZE = 128;

  private static final Pattern LAYOUT_PATTERN = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");

  private final SecureRandom random;

  private final int cost;

  public PBKDF2Hasher()
  {
    this(DEFAULT_COST);
  }

  /**
   * 根据指定的成本创建密码管理器。
   *
   * @param cost 哈希密码的指数级计算成本，范围从0到30
   */
  public PBKDF2Hasher(int cost)
  {
    iterations(cost); /* 验证成本值 */
    this.cost = cost;
    this.random = new SecureRandom();
  }

  private static int iterations(int cost)
  {
    if ((cost < 0) || (cost > 30))
      throw new IllegalArgumentException("成本值非法: " + cost);
    return 1 << cost;
  }

  /**
   * 对密码进行哈希处理以便存储。
   *
   * @return 一个安全的身份验证令牌，用于后续认证
   */
  public String hash(char[] password)
  {
    byte[] salt = new byte[SIZE / 8];
    random.nextBytes(salt);
    byte[] dk = pbkdf2(password, salt, 1 << cost);
    byte[] hash = new byte[salt.length + dk.length];
    System.arraycopy(salt, 0, hash, 0, salt.length);
    System.arraycopy(dk, 0, hash, salt.length, dk.length);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return ID + cost + '$' + encoder.encodeToString(hash);
  }

  /**
   * 使用密码和存储的密码令牌进行身份验证。
   *
   * @return 如果密码和令牌匹配则返回true，否则返回false
   */
  public boolean checkPassword(char[] password, String token)
  {
    Matcher matcher = LAYOUT_PATTERN.matcher(token);
    if (!matcher.matches())
      throw new IllegalArgumentException("无效的令牌格式");
    int iterations = iterations(Integer.parseInt(matcher.group(1)));
    byte[] hash = Base64.getUrlDecoder().decode(matcher.group(2));
    byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
    byte[] check = pbkdf2(password, salt, iterations);
    int zero = 0;
    for (int idx = 0; idx < check.length; ++idx)
      zero |= hash[salt.length + idx] ^ check[idx];
    return zero == 0;
  }

  private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
  {
    KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
      return factory.generateSecret(spec).getEncoded();
    }
    catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("缺少算法: " + ALGORITHM, ex);
    }
    catch (InvalidKeySpecException ex) {
      throw new IllegalStateException("无效的密钥工厂", ex);
    }
  }

  @Deprecated
  public String hash(String password)
  {
    return hash(password.toCharArray());
  }

  @Deprecated
  public boolean checkPassword(String password, String token)
  {
    return checkPassword(password.toCharArray(), token);
  }

}

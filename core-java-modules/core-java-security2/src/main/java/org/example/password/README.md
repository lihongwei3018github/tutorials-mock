1. **PBE 的全称是什么？**

PBE 指的是 **Password-Based Encryption**，即基于密码的加密。它是一种加密技术，通过使用用户提供的密码（通常是人类可以记住的密码）来加密和解密数据。PBE 的典型应用是在存储或传输敏感数据时，通过密码对数据进行加密，以保护数据的安全性。

2. **Java 中的 KeySpec 是什么？**

在 Java 中，KeySpec 是一种接口，用于标识不同类型的密钥规范（Key Specification）。它是一种抽象化的概念，用于表示可以用来生成实际密钥的参数集合。具体来说，Java 中有多种 KeySpec 的实现，例如：

- **DSAPrivateKeySpec** 和 **DSAPublicKeySpec**：用于表示 DSA（数字签名算法）的私钥和公钥的参数。
- **RSAPrivateKeySpec** 和 **RSAPublicKeySpec**：用于表示 RSA 算法的私钥和公钥的参数。
- **SecretKeySpec**：用于表示对称加密算法（如 AES）的密钥。
- 等等。

每种 KeySpec 都包含了生成特定类型密钥所需的信息和参数。在 Java 中，可以使用 KeyFactory 根据特定的 KeySpec 生成相应的密钥对象，用于加密、解密或签名等操作。
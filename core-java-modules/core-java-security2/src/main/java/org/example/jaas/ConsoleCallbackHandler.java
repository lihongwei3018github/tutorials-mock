package org.example.jaas;

import javax.security.auth.callback.*;
import java.io.Console;
import java.io.IOException;

/**
 * 实现了CallbackHandler接口，用于处理身份验证过程中的回调操作。
 * 主要支持NameCallback和PasswordCallback，用于获取用户名称和密码。
 */
public class ConsoleCallbackHandler implements CallbackHandler {

    /**
     * 处理回调数组中的每个回调对象。
     * @param callbacks 回调对象数组，可能包含NameCallback和PasswordCallback。
     * @throws IOException 如果读取控制台输入时发生错误。
     * @throws UnsupportedCallbackException 如果遇到不支持的回调类型。
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        // 获取系统控制台实例
        Console console = System.console();
        // 遍历回调数组，处理每个回调对象
        for (Callback callback : callbacks) {
            // 判断回调类型，如果是NameCallback
            if (callback instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callback;
                // 从控制台读取用户名称，并设置到NameCallback中
                nameCallback.setName(console.readLine(nameCallback.getPrompt()));
                // 判断回调类型，如果是PasswordCallback
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                // 从控制台读取用户密码，并设置到PasswordCallback中
                passwordCallback.setPassword(console.readPassword(passwordCallback.getPrompt()));
                // 如果遇到不支持的回调类型，抛出异常
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}

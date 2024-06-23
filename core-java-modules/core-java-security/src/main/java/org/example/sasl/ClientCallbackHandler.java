package org.example.sasl;

import javax.security.auth.callback.*;
import javax.security.sasl.RealmCallback;
import java.io.IOException;

/**
 * 客户端回调处理程序类，用于处理身份验证过程中的各种回调。
 * 实现了CallbackHandler接口，以提供特定的用户名、密码和领域信息。
 */
public class ClientCallbackHandler implements CallbackHandler {

    /**
     * 处理回调数组，针对不同的Callback类型提供相应的处理逻辑。
     * @param cbs 回调数组，可能包含NameCallback、PasswordCallback和RealmCallback等。
     * @throws IOException 如果处理回调时发生I/O错误。
     * @throws UnsupportedCallbackException 如果遇到不支持的回调类型。
     */
    @Override
    public void handle(Callback[] cbs) throws IOException, UnsupportedCallbackException {
        // 遍历回调数组，针对不同类型的回调执行相应的处理。
        for (Callback cb : cbs) {
            // 处理名称回调，设置固定的用户名。
            if (cb instanceof NameCallback) {
                NameCallback nc = (NameCallback) cb;
                nc.setName("username");
                // 处理密码回调，设置固定的密码。
            } else if (cb instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) cb;
                pc.setPassword("password".toCharArray());
                // 处理领域回调，设置固定的服务器领域名称。
            } else if (cb instanceof RealmCallback) {
                RealmCallback rc = (RealmCallback) cb;
                rc.setText("myServer");
                // 如果遇到未知的回调类型，抛出UnsupportedCallbackException异常。
            } else {
                throw new UnsupportedCallbackException(cb, "Unsupported Callback");
            }
        }
    }
}

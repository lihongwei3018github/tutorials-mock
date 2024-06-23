package org.example.sasl;

import javax.security.auth.callback.*;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import java.io.IOException;

/**
 * 服务器回调处理程序类，用于处理各种类型的回调。
 * 该类实现了CallbackHandler接口，并提供了具体实现来处理授权、名称、密码和领域回调。
 */
public class ServerCallbackHandler implements CallbackHandler {

    /**
     * 处理提供的回调数组。
     * 对于每个回调，根据其类型执行相应的操作，如设置授权状态、用户名、密码和领域信息。
     *
     * @param cbs 回调数组，包含需要处理的各种回调对象。
     * @throws IOException            如果处理回调时发生I/O错误。
     * @throws UnsupportedCallbackException 如果遇到不支持的回调类型。
     */
    @Override
    public void handle(Callback[] cbs) throws IOException, UnsupportedCallbackException {
        // 遍历回调数组，针对不同类型的回调执行相应操作。
        for (Callback cb : cbs) {
            if (cb instanceof AuthorizeCallback) {
                // 处理授权回调，设置授权状态为true。
                AuthorizeCallback ac = (AuthorizeCallback) cb;
                ac.setAuthorized(true);
            } else if (cb instanceof NameCallback) {
                // 处理名称回调，设置用户名。
                NameCallback nc = (NameCallback) cb;
                nc.setName("username");
            } else if (cb instanceof PasswordCallback) {
                // 处理密码回调，设置密码。
                PasswordCallback pc = (PasswordCallback) cb;
                pc.setPassword("password".toCharArray());
            } else if (cb instanceof RealmCallback) {
                // 处理领域回调，设置领域名称。
                RealmCallback rc = (RealmCallback) cb;
                rc.setText("myServer");
            }
        }
    }
}

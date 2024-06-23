package org.example.sasl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * SaslTest 类用于测试 SASL (Simple Authentication and Security Layer) 客户端和服务器之间的认证交互。
 * 它通过模拟认证过程来验证 DIGEST-MD5 机制是否能正常工作。
 */
public class SaslTest {

    // 定义使用的 SASL 机制名称
    private static final String MECHANISM = "DIGEST-MD5";
    // 定义模拟的服务器名称
    private static final String SERVER_NAME = "myServer";
    // 定义模拟的协议名称
    private static final String PROTOCOL = "myProtocol";
    // 定义授权标识符，此处为 null，表示使用默认标识符
    private static final String AUTHORIZATION_ID = null;
    // 定义质量保护级别为 auth-conf，即认证并提供消息机密性保护
    private static final String QOP_LEVEL = "auth-conf";

    // SASL 服务器实例，用于模拟服务器端的认证行为
    private SaslServer saslServer;
    // SASL 客户端实例，用于模拟客户端端的认证行为
    private SaslClient saslClient;

    /**
     * setUp 方法在每个测试前被调用，用于初始化 SASL 客户端和服务器。
     * 它们使用预定义的机制、协议、服务器名称和质量保护级别进行配置。
     * @throws SaslException 如果创建 SASL 客户端或服务器时出现错误。
     */
    @Before
    public void setUp() throws SaslException {
        ServerCallbackHandler serverHandler = new ServerCallbackHandler();
        ClientCallbackHandler clientHandler = new ClientCallbackHandler();

        Map<String, String> props = new HashMap<>();
        props.put(Sasl.QOP, QOP_LEVEL);

        saslServer = Sasl.createSaslServer(MECHANISM, PROTOCOL, SERVER_NAME, props, serverHandler);
        saslClient = Sasl.createSaslClient(new String[] { MECHANISM }, AUTHORIZATION_ID, PROTOCOL, SERVER_NAME, props, clientHandler);
    }

    /**
     * 测试 SASL 客户端和服务器之间的认证流程。
     * 它模拟一个完整的认证交互，包括客户端和服务器之间的多轮挑战/响应消息交换。
     * @throws SaslException 如果认证过程中出现错误。
     */
    @Test
    public void givenHandlers_whenStarted_thenAutenticationWorks() throws SaslException {
        byte[] challenge;
        byte[] response;

        challenge = saslServer.evaluateResponse(new byte[0]);
        response = saslClient.evaluateChallenge(challenge);

        challenge = saslServer.evaluateResponse(response);
        response = saslClient.evaluateChallenge(challenge);

        assertTrue(saslServer.isComplete());
        assertTrue(saslClient.isComplete());

        String qop = (String) saslClient.getNegotiatedProperty(Sasl.QOP);
        assertEquals("auth-conf", qop);

        byte[] outgoing = "Baeldung".getBytes();
        byte[] secureOutgoing = saslClient.wrap(outgoing, 0, outgoing.length);

        byte[] secureIncoming = secureOutgoing;
        byte[] incoming = saslServer.unwrap(secureIncoming, 0, secureIncoming.length);
        assertEquals("Baeldung", new String(incoming, StandardCharsets.UTF_8));
    }

    /**
     * tearDown 方法在每个测试后被调用，用于清理 SASL 客户端和服务器资源。
     * 它调用 dispose 方法来释放与客户端和服务器实例相关的资源。
     * @throws SaslException 如果释放客户端或服务器资源时出现错误。
     */
    @After
    public void tearDown() throws SaslException {
        saslClient.dispose();
        saslServer.dispose();
    }

}

/**
 * SSL安全连接工具类。
 * 提供方法以建立与指定主机和端口的SSL安全连接。
 */
package org.example.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.io.OutputStream;

public class SecureConnection {

    /**
     * 主程序入口点。
     * 检查命令行参数数量是否正确，然后尝试使用提供的主机和端口建立SSL安全连接。
     *
     * @param args 命令行参数数组，应包含[0]主机名，[1]端口号
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("使用方式: SecureConnection 主机 端口");
            System.exit(1);
        }
        try {
            String host = getHost(args); // 获取主机名
            Integer port = getPort(args); // 获取端口号
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault(); // 获取默认的SSL套接字工厂
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port); // 使用工厂创建SSL套接字
            InputStream in = sslsocket.getInputStream(); // 获取套接字输入流
            OutputStream out = sslsocket.getOutputStream(); // 获取套接字输出流

            out.write(1); // 发送一个字节用于测试连接

            // 读取并打印从服务器接收到的数据
            while (in.available() > 0) {
                System.out.print((char) in.read());
            }

            System.out.println("SSL安全连接成功建立");

        } catch (Exception exception) {
            // 打印异常堆栈信息
            exception.printStackTrace();
        }
    }

    /**
     * 从命令行参数中获取主机名。
     *
     * @param args 命令行参数数组
     * @return 主机名字符串
     */
    private static String getHost(String[] args) {
        return args[0];
    }

    /**
     * 从命令行参数中解析并获取端口号。
     *
     * @param args 命令行参数数组
     * @return 端口号
     */
    private static Integer getPort(String[] args) {
        return Integer.parseInt(args[1]);
    }

}

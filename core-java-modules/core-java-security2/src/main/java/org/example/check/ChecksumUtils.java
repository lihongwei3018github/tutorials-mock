package org.example.check;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * ChecksumUtils 类提供了一些工具方法来计算字节数据的CRC32校验和。
 */
public class ChecksumUtils {

    /**
     * 计算字节数组的CRC32校验和。
     *
     * @param bytes 待计算校验和的字节数组。
     * @return 字节数组的CRC32校验和。
     */
    public static long getChecksumCRC32(byte[] bytes) {
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    /**
     * 从输入流中计算CRC32校验和。
     * 此方法通过读取输入流中的所有数据来计算CRC32校验和，使用了缓冲区来提高读取效率。
     *
     * @param stream 待计算校验和的输入流。
     * @param bufferSize 读取数据时使用的缓冲区大小。
     * @return 输入流所有数据的CRC32校验和。
     * @throws IOException 如果读取输入流时发生错误。
     */
    public static long getChecksumCRC32(InputStream stream, int bufferSize) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(stream, new CRC32());
        byte[] buffer = new byte[bufferSize];
        while (checkedInputStream.read(buffer, 0, buffer.length) >= 0) {}
        return checkedInputStream.getChecksum().getValue();
    }
}


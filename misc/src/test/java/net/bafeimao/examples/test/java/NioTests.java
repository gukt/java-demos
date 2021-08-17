package net.bafeimao.examples.test.java;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/10/13.
 */
public class NioTests {
    private static Logger LOGGER = LoggerFactory.getLogger(NioTests.class);
    private static final String HOST = "localhost";
    private static final int PORT = 3304;

    @Test
    public void testReadFileByFileChannel() throws IOException {
        RandomAccessFile file = new RandomAccessFile("c:/111.txt", "rw");
        FileChannel fileChannel = file.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        // 从Channel中读取内容到buffer中
        int bytesRead = fileChannel.read(buf);

        while (bytesRead != -1) {
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = fileChannel.read(buf);
        }
        file.close();
    }


    @Test
    public void testWriteToFileChannel() throws IOException {
        RandomAccessFile file = new RandomAccessFile("c:/111.txt", "rw");
        FileChannel fileChannel = file.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put("This data is written by testcase".getBytes());

        buf.flip();

        // 由于write()无法保证能写多少字节,因此这里放到while循环体中
        while(buf.hasRemaining()) {
            fileChannel.write(buf);
        }

        // 获取当前文件大小
        long size =  fileChannel.size();

        // 运行后文件内容被截断了
        fileChannel.truncate(4);

        // 将通道里尚未写入磁盘的数据强制写到磁盘上，同时将文件元数据写到磁盘上
        fileChannel.force(true);

        System.out.println("Done!!!");
    }


    @Test
    public void testServerSocketChannelAccept() throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(HOST, PORT));

        // 将这里分别设置为true(默认）和false，看看accept方法如何执行
        // ssc.configureBlocking(false);

        // NOTE: 可用Telnet连接测试

        while (true) {
            // 如果ssc是阻塞模式，那么accept会一直阻塞，反之立即返回null
            SocketChannel socketChannel = ssc.accept();

            if (socketChannel == null) {
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println("A client was connected!!!");
            }
        }
    }

    @Test
    public void testSocketChannelConnect() throws IOException {
        SocketChannel socketChannel = null;

        try {
            // 打开一个客户端SocketChannel
            socketChannel = SocketChannel.open();

            // 连接服务器
            socketChannel.connect(new InetSocketAddress(HOST, PORT));
        } finally {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        }
    }

}

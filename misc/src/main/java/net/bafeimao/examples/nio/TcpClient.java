package net.bafeimao.examples.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2015/10/14.
 */
public class TcpClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private String host;
    private int port;

    public TcpClient( int port) {
        this("localhost",port);
    }

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect()   {
        try {
            this.socketChannel = SocketChannel.open(new InetSocketAddress(host, port));

            this.socketChannel.configureBlocking(false);

            // Open selector
            this.selector = Selector.open();

            // Register client channel to selector
            this.socketChannel.register(selector, SelectionKey.OP_READ);

            Processor process = new Processor(selector);
            process.start();
        }catch (Exception e) {
            System.out.println("Failed to connect to server, exception:" + e.getMessage());
        }
    }

    public void write(String data) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
            this.socketChannel.write(buffer);
        }
        catch(Exception e) {
            System.out.println("Failed to write data to server.");
        }
    }

    class Processor extends Thread {
        private Selector selector;

        public Processor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (selector.select() > 0) {
                    // 遍历每个有可用IO操作Channel对应的SelectionKey
                    for (SelectionKey selectionKey : selector.selectedKeys()) {
                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            socketChannel.read(buffer);

                            buffer.flip();

                            String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
                            System.out.println("RECEIVED: " + receivedString + ", from:" + socketChannel.socket().getRemoteSocketAddress());

                            // 为下一次读取作准备
                            selectionKey.interestOps(SelectionKey.OP_READ);
                        }

                        // 删除正在处理的SelectionKey
                        selector.selectedKeys().remove(selectionKey);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TcpClient client = new TcpClient(3304);
        client.connect();

        for(int i =0;i < 3;i++) {
            client.write("message" + i);

            //TimeUnit.SECONDS.sleep(3);
        }
    }
}


package net.bafeimao.examples.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/10/14.
 */
public class TcpServer {
    private int port = 3001;

    public TcpServer() {
    }

    public TcpServer(int port) {
        this.port = port;
    }

    public void start()  {
        System.out.println("Server is starting ...");

        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            selector = Selector.open();

            // 打开一个ServerSocketChannel并绑定到指定端口
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));

            // 与Selector一起使用时，必须将通道配置成非阻塞的
            // NOTE: FileChannel是阻塞的，因此不能配合Selector一起使用
            serverSocketChannel.configureBlocking(false);

            // 将通道注册到Selector中，该方法返回一个与Channel关联的SelectionKey，
            // 第二个参数就是设置这个SelectionKey感兴趣的事件，以便Selector在下次选择时判断是否要将该Key放入SelectedKeys集合中

            /**
             * 通道就绪事件有：
             * 连接就绪：该通道已成功连接到远端
             * 接受就绪：该通道已成功接受新进入的远端连接
             * 读就绪：通道中有数据可读
             * 写就绪：通道中有数据可写
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server was successfully started, listening on：" +port);

            while (true) {

                selector.select();

                for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                    SelectionKey key = iterator.next();

                    if (key.isConnectable()) {
                        ((SocketChannel) key.channel()).finishConnect();
                    } else if (key.isAcceptable()) {
                        // Accept the connection
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);


                        System.out.println("Accepted an new connection:[" + clientChannel.getRemoteAddress() + "]");

                        // 将代表客户端连接的Socket也注册到selector中
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        clientChannel.read(buffer);
                        String request = new String(buffer.array()).trim();
                        System.out.println("Message read from client: " + request);

                        if (request.equals("Bye.")) {
                            clientChannel.close();
                            System.out.println("The client[" + clientChannel.getRemoteAddress() + "] is going away ...");
                        }
                     } else if (key.isWritable()) {
                        System.out.println("Writing data...");

                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.wrap("hello".getBytes("UTF-8"));
                        //ByteBuffer buffer = (ByteBuffer) key.attachment();

                        if (!buffer.hasRemaining()) {
                            clientChannel.write(buffer);
                        }
                    }

                    // 不要忘记了移除刚刚处理的Key，Selector不会在这个集合上做移除SelectionKey操作
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Server failure: " + e.getMessage());
        } finally {
            try {
                selector.close();
                serverSocketChannel.socket().close();
                serverSocketChannel.close();
            } catch (Exception ignored) {
            }
        }

    }

    public static void main(String[] args) {
        TcpServer server = new TcpServer(3304);
        server.start();
    }
}
package demos.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Application class
 *
 * @author https://github.com/gukt
 */
public class TcpServer {

  private static final int DEFAULT_SND_BUFFER_CAPACITY = 1024;
  private static final int DEFAULT_RCV_BUFFER_CAPACITY = 1024;
  private static final int DEFAULT_PORT = 8000;
  private int port;

  private TcpServer() {
    this(DEFAULT_PORT);
  }

  private TcpServer(int port) {
    this.port = port;
  }

  private void start() throws IOException {
    System.out.println("Starting socket server...");
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // (1)
    serverSocketChannel.configureBlocking(false); // (2)
    serverSocketChannel.bind(new InetSocketAddress(port)); // (3)
    Selector selector = Selector.open(); // (4)
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // (5)
    System.out.println("Started, listening on " + port);

    while (selector.select() > 0) {
      for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
        SelectionKey key = it.next();
        it.remove();
        if (key.isAcceptable()) {
          handleAccept(key);
        } else if (key.isReadable()) {
          handleRead(key);
        } else if (key.isWritable()) {
          handleWrite(key);
        } else if (key.isConnectable()) {
          handleConnect(key);
        }
      }
    }
  }

  private void handleConnect(SelectionKey key) {
    System.out.println("handleConnect: " + key);
  }

  private void handleWrite(SelectionKey key) {
    System.out.println("handleWrite: " + key);
  }

  private void handleAccept(SelectionKey key) throws IOException {
    System.out.println("handleAccept: " + key);

    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
    SocketChannel channel = ssc.accept();
    channel.configureBlocking(false);
    channel.register(
        key.selector(),
        SelectionKey.OP_READ,
        ByteBuffer.allocateDirect(DEFAULT_RCV_BUFFER_CAPACITY));
  }

  private void handleRead(SelectionKey key) throws IOException {
    System.out.println("handleRead: " + key);

    SocketChannel channel = (SocketChannel) key.channel();
    ByteBuffer buf = (ByteBuffer) key.attachment();
    ByteBuffer writeBuffer = ByteBuffer.allocate(DEFAULT_SND_BUFFER_CAPACITY);
    int bytesRead = channel.read(buf);
    if (bytesRead > 0) {
      buf.flip();
      byte[] bytes = new byte[bytesRead];
      buf.get(bytes, 0, bytesRead);
      String s = new String(bytes);
      System.out.println(s);
      buf.clear();

      writeBuffer.put(bytes);
      writeBuffer.flip();
      while (writeBuffer.hasRemaining()) {
        channel.write(writeBuffer);
      }
      writeBuffer.compact();
    } else {
      System.out.println("关闭的连接");
      channel.close();
    }
  }

  public static void main(String[] args) {
    try {
      new TcpServer().start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

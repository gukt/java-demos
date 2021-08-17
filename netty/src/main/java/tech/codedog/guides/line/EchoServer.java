package tech.codedog.guides.line;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * DiscardServer class
 *
 * @author https://github.com/gukt
 */
public class EchoServer {

  private int port;

  private EchoServer(int port) {
    this.port = port;
  }

  private void run() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  ch.pipeline().addLast(new LineBasedFrameDecoder(65535, true, false));
                  ch.pipeline().addLast(new StringDecoder());
                  //                  ch.pipeline().addLast(new JsonObjectDecoder());
                  //                  ch.pipeline().addLast(new JsonToMessageDecoder());
                  ch.pipeline().addLast(new LineEncoder());
                  ch.pipeline().addLast(new EchoServerHandler());
                }
              })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture f = b.bind(port).sync(); // (7)
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    int port = 8080;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }

    new EchoServer(port).run();
  }
}

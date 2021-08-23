package demos.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * DiscardServer class
 *
 * @author https://github.com/gukt
 */
public class DiscardServer {
  private int port;

  public DiscardServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    // NioEventLoopGroup 是一个用来处理 I/O 的多线程 EventLoop
    // 因为实现的是服务端的应用程序，所以这里需要 2 个 NioEventLoopGroup，
    // 第一个叫 boss，用来接受网络连接，第二个叫 worker，一旦 boss 接受了一个新的网络请求，就会将这条连接注册到 worker 中，
    // 然后该连接所有的后续 I/O 在 worker 中完成。
    // worker 具体使用多少个线程，依赖于具体的 EventLoopGroup 的实现，且可配置。
    EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      // ServerBootstrap 是一个用以快速启动一个服务器（或客户端）的帮助类，虽然你可以不用它，但自己配置非常冗长乏味，
      // 通常你只需要使用 ServerBootstrap 就好了。
      ServerBootstrap b = new ServerBootstrap(); // (2)
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class) // (3)
          // ChannelInitializer 是一个特别的 handler，目的是帮助我们配置新 Channel 的，
          // 因为每个 Channel 都需要初始化一个唯一的 ChannelPipeline，以便在 ChannelPipeline 中添加处理各种业务的 handle，
          // ChannelInitializer 就是帮我们做这方面工作的
          // 随着应用程序变得越来越复杂，这里可能要添加很多 handlers，如果那样可以将这里的匿名类移到更高一级定义在独立的类中。
          .childHandler(
              new ChannelInitializer<SocketChannel>() { // (4)
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  //                  ch.pipeline().addLast(new PrintHandler());
                  //                  ch.pipeline().addLast(new DiscardServerHandler());
                  ch.pipeline().addLast(new InboundHandler1());
                  ch.pipeline().addLast(new OutboundHandler1());
                  ch.pipeline().addLast(new InboundHandler2());
                  ch.pipeline().addLast(new OutboundHandler2());
                  ch.pipeline().addLast(new InboundHandler3());
//                  ch.pipeline().addLast(new DiscardServerHandler());
                }
              })
          // option 是用来配置 NioServerSocketChannel 的，NioServerSocketChannel 是用来接受新连接的
          // childOption 是用来配置 channel 的
          .option(ChannelOption.SO_BACKLOG, 128) // (5)
          .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(port).sync(); // (7)

      // Wait until the server socket is closed.
      // In this example, this does not happen, but you can do that to gracefully
      // shut down your server.
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

    new DiscardServer(port).run();
  }
}

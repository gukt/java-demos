package demos.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class InboundHandler3 extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("InboundHandler3: channelRead");
    System.out.println("InboundHandler3: writing");
    ctx.writeAndFlush(msg);
    System.out.println("InboundHandler3: written");
    //    ctx.fireChannelRead(msg);
  }
}

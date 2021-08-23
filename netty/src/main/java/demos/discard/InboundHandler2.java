package demos.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class InboundHandler2 extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("InboundHandler2: channelRead");
    System.out.println("InboundHandler2: writing");
    ctx.writeAndFlush(((ByteBuf) msg).retain());
    System.out.println("InboundHandler2: written");

    ctx.fireChannelRead(msg);
  }
}

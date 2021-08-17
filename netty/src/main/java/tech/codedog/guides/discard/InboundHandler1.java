package tech.codedog.guides.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class InboundHandler1 extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("InboundHandler1: channelRead");
    System.out.println("InboundHandler1: writing");
    ctx.writeAndFlush(msg);
    System.out.println("InboundHandler1: written");

    ctx.fireChannelRead(msg);
  }
}

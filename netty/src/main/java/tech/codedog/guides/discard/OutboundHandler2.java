package tech.codedog.guides.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class OutboundHandler2 extends ChannelOutboundHandlerAdapter {

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
    System.out.println("OutboundHandler2: writing");
    ctx.write(msg);
    System.out.println("OutboundHandler2: written");
  }
}

package tech.codedog.guides.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class PrintHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    ByteBuf in = (ByteBuf) msg;
    try {
      System.out.println("PrintHandler:" +in.toString(StandardCharsets.UTF_8));
      ctx.fireChannelRead(msg);
    } finally {
      //          ReferenceCountUtil.release(msg); // (2)
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}

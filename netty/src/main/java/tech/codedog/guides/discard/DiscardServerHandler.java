package tech.codedog.guides.discard;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.netty.channel.ChannelFutureListener.CLOSE;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    //    System.out.println(msg);
    // Discard the received data silently.
    //        ((ByteBuf) msg).release(); // (3)

    ByteBuf in = (ByteBuf) msg;
    try {
      System.out.println("DiscardServerHandler:" + in.toString(StandardCharsets.UTF_8));
      msg = Unpooled.copiedBuffer(new Date().toString().getBytes(StandardCharsets.UTF_8));
      ctx.writeAndFlush(msg);
    } finally {
      System.gc();
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

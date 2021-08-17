package tech.codedog.guides.line;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * DiscardServerHandler class
 *
 * @author https://github.com/gukt
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String msg) {
    ctx.writeAndFlush("Did you say " + msg);
  }
}

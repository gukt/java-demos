package demos.line;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JsonToMessageDecoder class
 *
 * @author https://github.com/gukt
 */
@Sharable
public class JsonToMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    String json = msg.toString(StandardCharsets.UTF_8);
    System.out.println(json);
    out.add(json);
  }
}

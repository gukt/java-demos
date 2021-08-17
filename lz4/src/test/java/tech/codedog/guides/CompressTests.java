package tech.codedog.guides;

import org.junit.Test;
import tech.codedog.guides.util.Zips;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * CompressTests class
 *
 * @author https://github.com/gukt
 */
public class CompressTests {

  @Test
  public void test1() {
    String s =
        "LZ4 is lossless compression algorithm, providing compression speed > 500 MB/s per core, scalable with multi-cores CPU. It features an extremely fast decoder, with speed in multiple GB/s per core, typically reaching RAM speed limits on multi-core systems.\n"
            + "Speed can be tuned dynamically, selecting an \"acceleration\" factor which trades compression ratio for faster speed. On the other end, a high compression derivative, LZ4_HC, is also provided, trading CPU time for improved compression ratio. All versions feature the same decompression speed.\n"
            + "\n";
    byte[] originBytes = s.getBytes(StandardCharsets.UTF_8);
    byte[] compressedBytes = Zips.compress(originBytes);
    System.out.println("originBytes(len=" + originBytes.length + "): " + Arrays.toString(originBytes));
    System.out.println(
        "compressedBytes(len=" + compressedBytes.length + "): " + Arrays.toString(compressedBytes));
    double rate = (double) compressedBytes.length / originBytes.length;
    System.out.println("Compress rate: " + rate);
  }
}

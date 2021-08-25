package demos;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * CompressTests class
 *
 * @author https://github.com/gukt
 */
public class CompressTests {

    @Test
    public void testLZ4Compressor() {
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

    /**
     * Zips class
     *
     * @author https://github.com/gukt
     */
    public static class Zips {
      public static final LZ4Compressor compressor;
      public static final LZ4FastDecompressor decompressor;

      static {
        compressor = LZ4Factory.fastestInstance().fastCompressor();
        decompressor = LZ4Factory.fastestInstance().fastDecompressor();
      }

      /**
       * Compress specified bytes
       *
       * @param originBytes source bytes
       * @return compressed bytes
       */
      public static byte[] compress(byte[] originBytes) {
        return compressor.compress(originBytes);
      }

      /**
       * @param compressedBytes 压缩后的数据
       * @param srcLen 压缩前的数据长度
       * @return
       */
      public static byte[] decompress(byte[] compressedBytes, int srcLen) {
        return decompressor.decompress(compressedBytes, srcLen);
      }

      //  /**
      //   * @param srcByte
      //   * @param blockSize 一次压缩的大小 取值范围 64 字节-32M之间
      //   * @return
      //   * @throws IOException
      //   */
      //  public static byte[] lz4Compress(byte[] srcByte, int blockSize) throws IOException {
      //    LZ4Factory factory = LZ4Factory.fastestInstance();
      //    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
      //    LZ4Compressor compressor = factory.fastCompressor();
      //    LZ4BlockOutputStream compressedOutput =
      //        new LZ4BlockOutputStream(byteOutput, blockSize, compressor);
      //    compressedOutput.write(srcByte);
      //    compressedOutput.close();
      //    return byteOutput.toByteArray();
      //  }
      //
      //  /**
      //   * @param compressorByte
      //   * @param blockSize 一次压缩的大小 取值范围 64 字节-32M之间
      //   * @return
      //   * @throws IOException
      //   */
      //  public static byte[] lz4Decompress(byte[] compressorByte, int blockSize) throws IOException {
      //    LZ4Factory factory = LZ4Factory.fastestInstance();
      //    ByteArrayOutputStream baos = new ByteArrayOutputStream(blockSize);
      //    LZ4FastDecompressor decompresser = factory.fastDecompressor();
      //    LZ4BlockInputStream lzis =
      //        new LZ4BlockInputStream(new ByteArrayInputStream(compressorByte), decompresser);
      //    int count;
      //    byte[] buffer = new byte[blockSize];
      //    while ((count = lzis.read(buffer)) != -1) {
      //      baos.write(buffer, 0, count);
      //    }
      //    lzis.close();
      //    return baos.toByteArray();
      //  }

      //  /**
      //   * File to byte[]
      //   *
      //   * @param filePath
      //   * @return
      //   * @throws IOException
      //   */
      //  public static byte[] returnFileByte(String filePath) throws IOException {
      //    FileInputStream fileInputStream = new FileInputStream(new File(filePath));
      //    FileChannel channel = fileInputStream.getChannel();
      //    ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
      //    channel.read(byteBuffer);
      //    return byteBuffer.array();
      //  }

      //  /**
      //   * createFile
      //   *
      //   * @param fileByte
      //   * @param filePath
      //   */
      //  public static void createFile(byte[] fileByte, String filePath) {
      //    BufferedOutputStream bufferedOutputStream;
      //    FileOutputStream fileOutputStream;
      //    File file = new File(filePath);
      //    try {
      //      fileOutputStream = new FileOutputStream(file);
      //      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      //      bufferedOutputStream.write(fileByte);
      //      fileOutputStream.close();
      //      bufferedOutputStream.close();
      //    } catch (IOException e) {
      //      e.printStackTrace();
      //    }
      //  }
    }
}

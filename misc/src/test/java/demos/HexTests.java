package demos;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * HexTests class
 *
 * <pre> Benchmark: https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
 * Name (ops/s)         |    16 byte |    32 byte |  128 byte | 0.95 MB |
 * |----------------------|-----------:|-----------:|----------:|--------:|
 * | Opt1: BigInteger     |  2,088,514 |  1,008,357 |   133,665 |       4 |
 * | Opt2/3: Bytes Lib    | 20,423,170 | 16,049,841 | 6,685,522 |     825 |
 * | Opt4: Apache Commons | 17,503,857 | 12,382,018 | 4,319,898 |     529 |
 * | Opt5: Guava          | 10,177,925 |  6,937,833 | 2,094,658 |     257 |
 * | Opt6: Spring         | 18,704,986 | 13,643,374 | 4,904,805 |     601 |
 * | Opt7: BC             |  7,501,666 |  3,674,422 | 1,077,236 |     152 |
 * | Opt8: JAX-B          | 13,497,736 |  8,312,834 | 2,590,940 |     346 |
 * </pre>
 *
 * @author https://github.com/gukt
 */
public class HexTests {

    String hello = "hello";
    byte[] helloBytes = hello.getBytes(StandardCharsets.UTF_8);

    // 测试单个数值（byte，short，int，long，double，float）等转换为二进制，八进制，十六进制的字符串表示形式
    @Test
    void testSingleNumberValueToBinaryOrOctalOrHexString() {
        // 十进制数组和十六进制字符串互转，比如：数字 15，
        // Integer, Long 都有相应的 toBinaryString，toHexString, toOctalString 方法，分别用以输出指定数值的“二进制”，“十六进制”，“八进制” 字符串表示形式
        System.out.println(Integer.toBinaryString(15)); // 1111
        System.out.println(Integer.toHexString(15)); // f
        System.out.println(Integer.toHexString(15).toUpperCase()); // F
        System.out.println(Integer.toOctalString(15)); // 17
        // 但是，Short 和 Byte 没有对应的 toBinaryString，toHexString, toOctalString 方法
        // 因此，对于单个 Short 和 Byte 数值，只需要调用 Integer 上的相应转换方法接口，因为 short 作为参数传入时，会自动隐式转换。
        // 所以，提供 toBinaryString，toHexString, toOctalString 几个方法是多余的。
        // 但为什么 Integer 和 Long 上都提供了以上三个转换方法呢？因为所能表示的数值范围不一样啊。
        // 理论上只需要在 Long 上提供 toBinaryString，toHexString, toOctalString 方法即可，所有范围小的都会隐式转换为 Long，
        // 也许是从内存利用率，以及常用场景考虑吧，还是在 Integer 上也提供会更高效。
        short n1 = 15;
        System.out.println(Integer.toHexString(n1)); // f
        // Double，Float 都只有 toHexString 方法，没有 toBinaryString，toOctalString 方法
        System.out.println(Float.toHexString(1.23F)); // 0x1.3ae148p0

        // 也可以调用 Integer.toString 方法将其转换为其他进制表示法
        System.out.println(Integer.toString(15, 2)); // 1111
        System.out.println(Integer.toString(15, 8)); // 17
        System.out.println(Integer.toString(15, 16)); // f

        // 总结：
        // 如果字节数 <= 4：Integer.toHexString(ByteBuffer.wrap(fourByteArray).getInt());
        // 如果字节数 <= 8: Long.toHexString(ByteBuffer.wrap(eightByteArray).getLong());
    }

    // 使用 System.out.printf 指定格式，对进制进行转换
    @Test
    void testRadixConvertingWithSystemOutPrintf() {
        // References:
        // Java String Format Examples：https://dzone.com/articles/java-string-format-examples
        // https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
        int n = 256;
        // %s 以字符串形式输出；%d 以十进制输出；%o 以十进制输出；%x 以十六进制输出。
        System.out.printf("%s -> %d, %o, %x, %2X\n", n, n, n, n, n); // 256 -> 256, 400, 100, 100
        // 似乎没有直接指定转换为二进制格式的，所以当需要在输出字符串中打印二进制时，
        // 还是需要使用 Integer.toString(n,2) 或 Integer.toBinaryString(n) 进行转换。（对于 Long 型数值，调用 Long 上的相应方法）
    }

    // 使用“Apache Commons Codec”将字节数组转换为十六进制字符串表示形式
    @Test
    void testBytesToHexWithApacheCommonsCodec() {
        // 通常使用场景并不是将一个数值转换为其他进制表现形式，而是经一个字节数组转换为其他进制形式（或字符串，字符串可以很容易的通过 getBytes 获得字节数组）
        // 这个时候，可以有多个库可以使用，比如 Apache commons, Guava 等。也可以使用 JDK 内置的方式实现。
        // Apache commons:
        // Hex.encodeHex - 用来将字节数组中的每一个字节用 16 进制字符 (0-F) 表示。返回 char[]
        //      因为每个字节是 8 位，而 16 进制最大数是 15，能用来表示 4 位，所以一个字节需要用两个16 进制字符。
        //      默认是输出小写的 char[], 如果需要大写，请指定第二个参数为 false
        //      也接受 ByteBuffer 作为输入参数，因为 ByteBuffer 也可以很容易得到内部 bytes
        // Hex.encodeHexString - 其实就是对 Hex.encodeHex 的结果(char[]) 输出为 String (new String(Hex.encodeHex(..)))
        //
        // 将字节数组表示为 char 数组
        byte[] helloBytes = hello.getBytes(StandardCharsets.UTF_8);
        // 字节数组打印到控制台输出的是以 10 进制表示的数组，
        // 比如第一个字符 h 的十进制表示是 104，它的十六进制数值是 68
        // [104, 101, 108, 108, 111]
        System.out.println(Arrays.toString(helloBytes));
        // 字符 h 用一个字节表示，这个字节的 10 进制数值是 104，对应的 16 进制就是 68
        // encodeHex 输出的字符数组元素个数是输入参数的 2 倍，因为一个字节需要两个转换成 16 进制表示需要 2 位
        char[] hexChars = Hex.encodeHex(helloBytes);
        // [6, 8, 6, 5, 6, c, 6, c, 6, f]
        System.out.println(Arrays.toString(hexChars));
        // 68656c6c6f
        System.out.println(hexChars);
    }

    // 使用“自定义的方法”将字节数组转换为十六进制字符串表示形式
    @Test
    void testBytesToHexWithCustom() {
        System.out.println(bytesToHex(helloBytes)); // 68656C6C6F
        // 使用 Apache commons，输出字符串是一样的
        System.out.println(Hex.encodeHexString(helloBytes).toUpperCase()); // 68656C6C6F
    }

    // 使用 JAXB 中的 DataTypeConverter.printHexBinary 方法，将字节数组转换为十六进制字符串表示形式
    @Test
    void testBytesToHexWithJaxbDataTypeConverter() {
        //  javax.xml.bind.DatatypeConverter.printHexBinary() 也可以用来将一个字节数组转换为十六进制字符串表现形式
        // 注意：在 JDK8 或早期版本，JAXB 是 JDK 的标准库，但是Java 9 已经被废弃了，将在 Java 11 中被移除
        // Deprecated: https://cr.openjdk.java.net/~iris/se/9/java-se-9-fr-spec/#APIs-proposed-for-removal
        // Removal: https://openjdk.java.net/jeps/320
        System.out.println(DatatypeConverter.printHexBinary(helloBytes));  // 68656C6C6F
    }

    @Test
    void testBytesToHexWithGuava() {
        // 使用 Guava 进行转换
        System.out.println(BaseEncoding.base16().encode(helloBytes)); // 68656C6C6F
        // Guava 还能提供更精确的控制，可以让我们指定分隔符，大小写等参数控制
        System.out.println(BaseEncoding.base16().lowerCase().withSeparator(":", 2).encode(helloBytes)); // 68:65:6c:6c:6f

        // Guava 还提供了其他进制的转换
        System.out.println(BaseEncoding.base32().encode(helloBytes)); // NBSWY3DP
        System.out.println(BaseEncoding.base32Hex().encode(helloBytes)); // D1IMOR3F
        System.out.println(BaseEncoding.base64().encode(helloBytes)); // aGVsbG8=
        System.out.println(BaseEncoding.base64Url().encode(helloBytes)); // aGVsbG8=
    }

    @Test
    void testByteToHexWithJavaFormatter() {
        // 利用 String.format 指定格式为 '%x', 逐个将字节转换为十六进制字符表示，然后再利用 StringBuilder 将每次转换的字符拼接起来
        // 效率比较低，不建议使用，只是演示可以有这么一种实现方式
        System.out.println(byteToHexWithJavaFormatter(helloBytes)); // 68656c6c6f
    }

    // Private methods

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    public static String bytesToHex(byte[] bytes) {
        // 需要参数两倍的空间来存储 char[]
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public static String byteToHexWithJavaFormatter(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}

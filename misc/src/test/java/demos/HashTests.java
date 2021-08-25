package demos;

import com.google.common.hash.Hashing;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * HashTests
 *
 * <pre>
 * https://www.baeldung.com/java-md5
 * </pre>
 */
public class HashTests {

    String hello = "Hello world";
    byte[] helloBytes = "Hello world".getBytes(StandardCharsets.UTF_8);
    String expectedMd5Hex = "3E25960A79DBC69B674CD4EC67A72C62";

    /**
     * 使用 JDK 自带的 MessageDigest 得到 MD5
     */
    @Test
    void testMD5WithJDK() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(hello.getBytes());
        byte[] digest = md.digest();
        // 注意：在 JDK8 或早期版本，JAXB 是 JDK 的标准库，但是Java 9 已经被废弃了，将在 Java 11 中被移除
        // 更多关于如何将字节数组转换为十六进制字符串表示的方法，请见 {@link HexTests}
        String md5Hex = DatatypeConverter.printHexBinary(digest).toUpperCase();
        Assertions.assertEquals(expectedMd5Hex, md5Hex);
    }

    // 使用 Apache commons codec
    @Test
    void testMD5WithApacheCommonsCodec() {
        // 得到 MD5 字节数组结果
        byte[] md5Bytes = DigestUtils.md5(hello);
        System.out.println(Arrays.toString(md5Bytes));

        // md5Hex 就是在通过调用 md5(String) 后，再使用 Hex.encodeHexString 得到字符串
        String md5Hex = DigestUtils.md5Hex(hello).toUpperCase();
        Assertions.assertEquals(expectedMd5Hex, md5Hex);

        // DigestUtils 也提供了其他不同的 Hash 算法，和 Guava 一样，使用都挺方便。
        // DigestUtils.sha1()
        // DigestUtils.sha256()
    }

    @Test
    void testMD5AndOtherHashWithGuava() {
        // Guava 中，已经将 Hashing.md5() 方法废弃掉了，它建议我们不再使用它，因为它既不快又不安全。
        // 如果仍然需要和老系统交互，那么可以使用这个方法。尽管它已经标注被废弃了。
        // 3e25960a79dbc69b674cd4ec67a72c62
        System.out.println(Hashing.md5().hashBytes(helloBytes).toString());
        // e6760d555c32f66f5e159331db20fd8e
        System.out.println(Hashing.md5().hashUnencodedChars(hello).toString());

        // 不要再使用 md5 了，如果为了安全，请使用 sha256 或更高，如下：
        // 64ec88ca00b268e5ba1a35678a1b5316d212f4f366b2477232534a8aeca37f3c
        System.out.println(Hashing.sha256().hashBytes(helloBytes).toString());
        // b7f783baed8297f0db917462184ff4f08e69c2d5e5f79a942600f9725f58ce1f29c18139bf80b06c0fff2bdd34738452ecf40c488c22a7e3d80cdf6f9c1c0d47
        System.out.println(Hashing.sha512().hashBytes(helloBytes).toString());
        // 如果为了速度，请使用 goodFastHash
        System.out.println(Hashing.goodFastHash(256).hashBytes(helloBytes).toString());

        // 529ed68b
        System.out.println(Hashing.crc32().hashBytes(helloBytes).toString());
    }
}

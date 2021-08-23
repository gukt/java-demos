package demos;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * GuavaTests class
 *
 * @author https://github.com/gukt
 */
public class GuavaTests {

    @Test
    void test1() throws IOException {
        File file = TestUtils.prepareTmpFile("hello.txt", "hello", "world");
        CharSource charSource  = Files.asCharSource(file, StandardCharsets.UTF_8);
        System.out.println(charSource.read());
        System.out.println(charSource.readFirstLine());

        Assertions.assertEquals(2, charSource.length());
        // 获取第一行
        Assertions.assertEquals("hello", charSource.readFirstLine());

    }
}

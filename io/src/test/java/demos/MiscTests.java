package demos;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * MiscTests class
 *
 * @author https://github.com/gukt
 */
public class MiscTests {

    @Test
    void test1() throws IOException {
        System.out.println(Arrays.toString("你好".getBytes(StandardCharsets.UTF_8)));

        File file = TestUtils.prepareTmpFile("hello.txt", "你好", "世界");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb= new StringBuilder();
        // 返回读取的行（不包括行尾符），如果读完（EOF）了，则返回 null
        while((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        System.out.println(sb);

        // File file = TestUtils.prepareTmpFile("hello.txt", "你好");
        // int ch;
        // FileReader reader = new FileReader(file);
        // // 以下会一次打印出三个字符:
        // // 20320
        // // 22909
        // // 10
        // while((ch =  reader.read()) != -1) {
        //     System.out.println(ch);
        // }

        // File file = TestUtils.prepareTmpFile("hello.txt", "你好");
        // int ch;
        // try (InputStream in = new FileInputStream(file)) {
        //     // 以下会一次读出 6 个字节
        //     while ((ch = in.read()) != -1) {
        //         // 对于中文字符，这样逐字节打印出来会有问题
        //         System.out.println((char) ch);
        //     }
        // }

    }
}

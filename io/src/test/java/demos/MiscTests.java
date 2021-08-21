package demos;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * MiscTests class
 *
 * @author https://github.com/gukt
 */
public class MiscTests {

    @Test
    void testToHexByStringFormat() {
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb);
    }

    @Test
    void testToHexByIntegerToHex() {
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int decimal = (int) b & 0xff;
            String hex = Integer.toHexString(decimal);
            if (hex.length() % 2 == 1) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        System.out.println(sb);
    }

    String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    String toBinaryString(byte[] bytes) {
        return toBinaryString(bytes, null);
    }

    String toBinaryString(byte[] bytes, CharSequence delimiter) {
        delimiter = delimiter == null ? "" : delimiter;
        StringJoiner joiner = new StringJoiner(delimiter);
        for (byte b : bytes) {
            joiner.add(Integer.toBinaryString((int) b & 0xff));
        }
        return joiner.toString();
    }

    @Test
    void testStringBuilder() {
        String s = "你好";
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(bytes));

        String s1 = "今晚打老虎";
        char[] chars1 = s1.toCharArray();
        System.out.println(Arrays.toString(chars1));

        StringBuilder sb = new StringBuilder();
        sb.append(1);
        sb.append('A');
        sb.append((byte)1);
        System.out.println(sb);
    }

    @Test
    void test21() {
        Character ch = '中';
        System.out.println(ch);
        // System.out.println();

        // 如果放开这里，可以看到，IDE 会提示这个汉字不是一个 character 可以表示的。
        // 因为它不是位于Unicode 的 BMP（基本平面），所以码点是超过 65535 的，一个 char 类型是表示不了的。
        // ch = '𠮷';
        // System.out.println(ch);

        String s2 = "\uD842\uDFB7";
        System.out.println(s2);
        // 发现这个汉字需要四个字节表示
        // [-16, -96, -82, -73]
        System.out.println(Arrays.toString(s2.getBytes(StandardCharsets.UTF_8)));
        // f0a0aeb7
        System.out.println(toHex(s2.getBytes(StandardCharsets.UTF_8)));
        // 11110000,10100000,10101110,10110111
        System.out.println(toBinaryString(s2.getBytes(StandardCharsets.UTF_8), ","));

        String s = "中";
        char ch0 = s.charAt(0);
        // char ch1 = s.charAt(1);
        // char ch2 = s.charAt(2);
        // 3
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.UTF_8)));
        System.out.println(s.getBytes(StandardCharsets.UTF_8).length);
        // 4
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.UTF_16)));
        System.out.println(s.getBytes(StandardCharsets.UTF_16).length);
        // 2
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.UTF_16BE)));
        System.out.println(s.getBytes(StandardCharsets.UTF_16BE).length);
        // 2
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.UTF_16LE)));
        System.out.println(s.getBytes(StandardCharsets.UTF_16LE).length);
        // 1
        System.out.println(Arrays.toString(s.getBytes(StandardCharsets.US_ASCII)));
        System.out.println(s.getBytes(StandardCharsets.US_ASCII).length);


    }

    @Test
    void test1() throws IOException {
        System.out.println(Arrays.toString("你好".getBytes(StandardCharsets.UTF_8)));

        File file = TestUtils.prepareTmpFile("hello.txt", "你好", "世界");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder sb = new StringBuilder();
        // 返回读取的行（不包括行尾符），如果读完（EOF）了，则返回 null
        while ((line = reader.readLine()) != null) {
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

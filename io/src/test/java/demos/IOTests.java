package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static demos.TestUtils.prepareTmpFile;

/**
 * IoTests class
 * <pre>
 *     https://www.baeldung.com/reading-file-in-java
 * </pre>
 *
 * <a href="https://www.baeldung.com/reading-file-in-java">reading-file-in-java</a>
 *
 * @author https://github.com/gukt
 */
public class IOTests {


    // === Passed above ===


    @Test
    void testLineNumberReader() throws IOException {
        File file = TestUtils.prepareTmpFile("hello.txt", "hello", "world");
        LineNumberReader reader = new LineNumberReader(new FileReader(file));
        // // LineNumberReader 的 read() 读出来的内容（字符）是 int 类型，而不是想当然的 char 类型。
        // // 由于 int 和 char 都是两个字节表示的，所以将 int 转换为 char
        // int data = reader.read();
        // while (data != -1) {
        //     char ch = (char) data;
        //     System.out.printf("Read: %s (%s: int) @ line: %d\n", ch, data, reader.getLineNumber());
        //     data = reader.read();
        // }

        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line + "@" + reader.getLineNumber());
        }
        // while((data = (char) reader.read()) != -1) {
        //     System.out.println("Read: " + data + "@line:" + reader.getLineNumber());
        // }
    }

    @Test
    void test13() throws IOException {
        File file = prepareTmpFile("foo.txt");
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[0];
        // 如果 bytes 长度为 0，则直接返回 0，哪怕是文件没有任何内容也是返回 0
        int n = is.read(bytes);
        Assertions.assertEquals(0, n);
    }

    @Test
    void test15() throws IOException {
        // available() 方法表示下一次调用流的 read 或 skip 方法时，可用的字节大小。
        // 使用 InputStream 时，不要老是从文件的角度思考问题，网络 IO 也是 InputStream，他们最大的特点是数据是陆续接收的，
        // 所以每次读之前我们应该要先检查一下本次有没有数据可操作。
        File file = prepareTmpFile("foo.txt", "hello");
        InputStream is = new FileInputStream(file);
        int len = 0;
        // 循环检查可用字节数，直到有数据可读
        while (len == 0) {
            len = is.available();
        }
        byte[] bytes = new byte[len];
        // 下面开始读，本次是一定可以一次性读完的
        int n = is.read(bytes);
        Assertions.assertEquals(n, len);
    }


    @Test
    void testReadingWithScanner() {
        // TODO: 2021/8/18
    }

    @Test
    void testReadingWithStreamTokenizer() {
        // TODO: 2021/8/18
    }

    @Test
    void testReadingWithDataInputStream() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello");
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int len = in.available();
        if (len > 0) {
            byte[] buffer = new byte[len];
            //noinspection ResultOfMethodCallIgnored
            in.read(buffer);
            System.out.println(new String(buffer, StandardCharsets.UTF_8));
        }
    }

    @Test
    void testReadingWithFileChannelOfNio() {
        // TODO: 2021/8/18
    }

    @Test
    void testReadingWithBufferedReader() {
        File file = prepareTmpFile("hello.txt", "hello");
        String content = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            content = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("hello", content);
    }

    @Test
    void testReadingWithNioJdkFiles() {
        // FIXME: 2021/8/18
        // https://www.baeldung.com/reading-file-in-java#%20id=
    }

    @Test
    void testReadingWithNio() {
        // FIXME: 2021/8/18
        // https://www.baeldung.com/reading-file-in-java#read-file-with-path-readalllines
    }

    /**
     * 使用 FileOutputStream 将内容写入文件。
     */
    @Test
    void testWritingWithFileOutputStream() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
        Assertions.assertEquals(content, readFile(filename));
    }

    @Test
    void testWritingWithBufferedOutputStream() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename))) {
            bos.write(content.getBytes(StandardCharsets.UTF_8));
        }
        Assertions.assertEquals(content, readFile(filename));
    }

    @Test
    void testWritingWithFiles() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        Files.write(Paths.get(filename), content.getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(content, readFile(filename));
    }

    @Test
    void testWritingWithFileWriter() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append(content);
        }
        Assertions.assertEquals(content, readFile(filename));
    }

    @Test
    void testWritingWithBufferedWriter() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
        Assertions.assertEquals(content, readFile(filename));
    }

    @Test
    void testWritingWithPrintWriter() throws IOException {
        String filename = "hello.txt";
        String content = "hello";
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filename))) {
            printWriter.print(content);
        }
        Assertions.assertEquals(content, readFile(filename));
    }

    // Internal Helper methods

    byte[] read1() {
        File file = new File("/tmp/foo.txt");
        InputStream is;
        try {
            is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int i = 0;
            int n;
            while ((n = is.read()) != -1) {
                bytes[i++] = (byte) n;
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        // BufferedReader reader = new BufferedReader(file);
        // FIXME: 2021/8/18
        return "";
    }

    //
    // static class FileUtils {
    //
    //     public static String toStringWithApacheCommons(String filename) {
    //         // TODO: 2021/8/19
    //         return null;
    //     }
    //
    //     public static String toStringWithGuava(String filename) {
    //         // TODO: 2021/8/19
    //         return null;
    //     }
    //
    //     public static String toStringWithScanner(String filename) {
    //         return null;
    //     }
    // }
}

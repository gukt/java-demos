package demos;

import com.google.common.io.CharSource;
import org.apache.commons.io.IOUtils;
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

    /**
     * 测试使用 {@link FileInputStream} 读取文件（逐字节方式读取）
     */
    @Test
    void testReadingWithFileInputStreamOneByOne() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello");
        // 除了可以通过 File 对象创建 FileInputStream 外，还有另外两个构造函数，分别是：1. 接受文件名字符串；2. 接受 FileDescriptor
        try (InputStream in = new FileInputStream(file)) {
            int ch;
            // 循环读取，一次读取一个字节，直到 EOF（返回 -1）
            while ((ch = in.read()) != -1) {
                System.out.print((char) ch);
            }
        }
    }

    /**
     * 测试使用 {@link FileInputStream} 读取文件，一次从文件中读取多个字节。（健壮的方式）
     */
    @Test
    void testReadingWithFileInputStreamMoreRobust() throws IOException {
        // 本例演示一次从文件中读取多个字节，显然，这种方式比从磁盘文件一次读取一个字节要高效的多
        // 一次读取一条显然有点低效，我们可以一次读取多个字节到 buffer 中
        // 值得注意的是：read(byte[]) 或 read(byte b[], int off, int len) 并不保证一次能读完指定个数的数据（即使 InputStream 中有足够的数据）
        // 因此，我们需要通过 while 循环多次读取，直到 read(byte[]) 返回 -1（EOF）
        File file = prepareTmpFile("hello.txt", "hello");
        byte[] bytes = new byte[1024];
        int n;
        try (InputStream in = new FileInputStream(file)) {
            while ((n = in.read(bytes)) != -1) {
                // 因为缓冲区 bytes 中不会全部填满，下面将 bytes 中从 [0, n) 的字节转换为字符串打印出来
                System.out.println("Read: " + new String(bytes, 0, n));
            }
        }
    }

    /**
     * 测试使用 {@link FileInputStream} 读取文件，一次从文件中读取多个字节。（有缺陷的方式）
     */
    @Test
    void testReadingWithFileInputStream() throws IOException {
        // 本例演示一次从文件中读取多个字节到指定的 buffer 中
        File file = prepareTmpFile("hello.txt", "hello");
        try (InputStream in = new FileInputStream(file)) {
            byte[] bytes = new byte[1024];
            // 注意：本例是不够健壮的写法，因为 read(byte[]) 或 read(byte b[], int off, int len) 并不保证一次能读完全部可用的数据
            // 真正是否读到 EOF，还是要判断 n 是否等于 -1
            // 对于大量数据，可能一次读不完整，需要采用循环判断 read 返回值是否等于 -1，见另一个测试 'test***MoreRobust'
            int n = in.read(bytes);
            System.out.println("Read: " + new String(bytes, 0, n));
        }
    }

    @Test
    void testReadingWithApacheCommons() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello");
        System.out.println(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    @Test
    void testSequenceInputStream() throws IOException {
        File file1 = prepareTmpFile("foo.txt", "foo");
        File file2 = prepareTmpFile("bar.txt", "bar");
        File file3 = prepareTmpFile("baz.txt", "baz");
        File file4 = prepareTmpFile("qux.txt", "qux");

        FileInputStream in1 = new FileInputStream(file1);
        FileInputStream in2 = new FileInputStream(file2);
        FileInputStream in3 = new FileInputStream(file3);
        FileInputStream in4 = new FileInputStream(file4);

        // 尽管 in1, in2, in3, in4 几个 FileInputStream 是在 try-with-resources 块的外部定义的
        // 所以它们几个不受 try-with-resources 管理。
        // 但是由于 SequenceInputStream 的关闭，会关闭它引用的所有 InputStream，
        // 所以它们也就随着 SequenceInputStream 的关闭被自动关闭。
        try (InputStream in = new SequenceInputStream(
                new SequenceInputStream(in1, in2),
                new SequenceInputStream(in3, in4)
        )) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = in.read(buffer)) != -1) {
                System.out.print(new String(buffer, 0, n));
            }
        }
    }

    @Test
    void testReadingWithGuava() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello", "world");
        CharSource charSource = com.google.common.io.Files.asCharSource(file, StandardCharsets.UTF_8);
        System.out.println(charSource.read());
        System.out.println(charSource.readFirstLine());
        System.out.println(charSource.readLines());
        // 还有其他更多方法，不罗列了
    }

    // === Passed above ===

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

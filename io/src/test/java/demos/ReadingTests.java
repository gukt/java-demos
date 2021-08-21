package demos;

import com.google.common.io.CharSource;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static demos.TestUtils.prepareTmpFile;

/**
 * ReadingTests class
 *
 * @author https://github.com/gukt
 */
public class ReadingTests {

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
}

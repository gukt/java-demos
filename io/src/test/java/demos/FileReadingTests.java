package demos;

import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static demos.TestUtils.prepareTmpFile;

/**
 * FileReadingTests class
 *
 * <pre>
 *     https://www.baeldung.com/reading-file-in-java#read-file-with-path-readalllines
 *     https://www.baeldung.com/convert-input-stream-to-string
 *     http://tutorials.jenkov.com/java-io/index.html
 * </pre>
 *
 * @author https://github.com/gukt
 */
public class FileReadingTests {

    // 利用 FileReader 读取文本内容: 逐字符读取
    @Test
    void testReadingWithFileReader1() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好");
        // 所有的 reader 子类都提供有 read() 方法，该方法返回读出来的内容（字符）
        // 虽然读出来的内容是字符，但它是以 int 类型表示的，所以需要将其转为 char
        // Java 中 char 是两个字节表示的，int 也是两个字节
        // 以下演示如何逐字符读取：逐字符读取，判断是否为 -1（表示 EOF），如果不是就一直读，直到 EOF
        try (FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();
            int data;
            // 逐字符读取
            while ((data = reader.read()) != -1) {
                sb.append((char) data);
            }
            System.out.println(sb);
        }
    }

    // 利用 FileReader 读取文本内容: 批量读取
    @Test
    void testReadingWithFileReader2() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            // 这里为了测试常见中文字符（一般为 3 个字节）是否可以正常读取，正常情况下一般取 1024 的整数倍
            // 设置为 2，对于一个常见的中文字符（BMP 基本平面内的字符）来说，一次只能读取它的部分内容
            char[] chars = new char[2];
            // 用以保存读取的字符数
            int n;
            // 批量读取
            while ((n = reader.read(chars)) != -1) {
                // 不能保证最后一次能全部填充完 chars，所以需要依赖实际读取的字符数（reader.read(chars) 的返回值）
                sb.append(chars, 0, n);
            }
            // 因为所有的字符都被读取并 append 到 StringBuilder 了，即使每次读出的只是一个完整汉字字符的部分内容，最后拼接也可以成功输出“你好”
            // Output: 你好
            System.out.println(sb);
        }
    }

    // 利用 BufferedReader 读取文本内容: 逐行读取
    @Test
    void testReadingWithBufferedReader() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello", "world");
        // BufferedReader 需要传入一个 Reader，从字符输入流中读取文本，缓冲字符以提供对字符、数组和行的有效读取。
        // BufferedReader 也是一个 Reader，所以它也可以像 FileReader 一样，可以逐字符读取；也可以一次批量读取多个字符
        // 写法和 FileReader 一样，这里不再赘述。
        // 以下演示利用 BufferedReader 逐行读取
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            // 逐行读取
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            Assertions.assertEquals("hello\nworld\n", sb.toString());
            // Java 8 还提供了 lines() 方法（Stream<String>），返回所有行。
            // reader.lines().forEach(System.out::println);
        }
    }

    // 利用 BufferedReader 读取文本内容: 利用 lines() 获取文件内容
    @Test
    void testReadingWithBufferedReader2() throws FileNotFoundException {
        // lines 内部也是利用 readLine() 方法迭代读取的
        File file = prepareTmpFile("hello.txt", "hello", "world");
        InputStream in = new FileInputStream(file);
        String text = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(text);
    }

    // 利用 LineNumberReader 读取文本内容: 逐字符读取并输出行号
    @Test
    void testReadingWithLineNumberReaderOneByOne() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello", "world");
        char ch;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            int data = reader.read();
            while (data != -1) {
                ch = (char) data;
                System.out.printf("Read: %s (%s: int) @line#%d\n", ch, data, reader.getLineNumber());
                data = reader.read();
            }
        }
    }

    // 利用 LineNumberReader 读取文本内容: 逐行读取
    @Test
    void testReadingWithLineNumberReaderByReadLine() throws IOException {
        File file = prepareTmpFile("hello.txt", "hello", "world");
        String line;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            // LineNumberReader 还提供 readLine() 方法用于一次读出一行数据，如果碰到流的尾部，readLine 返回 null
            while ((line = reader.readLine()) != null) {
                System.out.println(line + " @" + reader.getLineNumber());
            }
        }
    }

    // 使用 FileInputStream 按字节读取流 - 逐字节读取
    @Test
    void testReadingWithFileInputStream1() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好");
        // InputStream 常用于读二进制文件，如图片、声音、影像等文件
        // 如果你有一个 InputStream，想按字符读取，就可以将其包装成 InputStreamReader
        // 如果你有一个文件，想按字符读取，可以将其包装成 FileReader（继承自 InputStreamReader）
        try (InputStream in = new FileInputStream(file)) {
            int data;
            StringBuilder sb = new StringBuilder();
            // 逐字节读取
            while ((data = in.read()) != -1) {
                // 如果我们要读取基于字符（文本）的内容，请使用 Reader，而非 InputStream。
                // 因为对于 Unicode 编码而言，每个字符占的字节数是不同的，
                // 有可能是标准的 ASCII 码，占 1 个字节；也可能有中文和其他各种语言的字符。
                // 如果是位于 Unicode BMP（基本平面） 内的常用中文，每个中文字符占 3 个字符，但是有些中文可能占超过 3 个字符，
                // 我们很难自己去处理字符的分界，知道几个字节组成一个实际的字符。
                // 令人愉快的是，这些烦心事我们不必再操心，Reader 的各种子类帮我们处理好了。
                System.out.println("Read byte: " + data);
                sb.append((byte) data);
            }
            // -28-67-96-27-91-6710
            System.out.println(sb);
        }
    }

    // 使用 FileInputStream 按字节读取流 - 批量读取
    // 对于 FileInputStream 而言，逐字节读取就是从磁盘上一个字节一个字节的读取，实在是太低效了。该方法比逐字节高效
    @Test
    void testReadingWithFileInputStream2() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好");
        try (InputStream in = new FileInputStream(file)) {
            int n;
            // 逐字节读取（这里的 buffer 大小故意设置成比较小，以测试逐次读取）
            byte[] buffer = new byte[2];
            while ((n = in.read(buffer)) != -1) {
                System.out.println("Read byte: " + Arrays.toString(Arrays.copyOf(buffer, n)));
            }
        }
    }

    // 使用 InputStream 按字节读取流 - 通过 readAllBytes（JDK 9 新增） 一次性将字节全部读出，便于利用这些字节转换为 String
    // 注意：readAllBytes 不适用于读取拥有大量数据的流，在内容较小的流的场景中，该方法简单方便，单行就可以读出所有内容
    @Test
    void testReadingWithFileInputStream3() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        try (InputStream in = new FileInputStream(file)) {
            String s = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(s);
        }
    }

    @Test
    void testReadingWithFileInputStreamAndToString() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        try (InputStream in = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int n;
            byte[] buffer = new byte[1024];
            while ((n = in.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            System.out.println(baos.toString(StandardCharsets.UTF_8));
        }
    }

    // 使用 Guava 读取字节或文本，Guava 提供了 CharSource、ByteSource 用于读取文本和字节内容。
    // Guava 会自动打开和关闭流，所以这里可以接着读第一行，仍然可以读到数据，如果是自己在 InputStream 中读到文件尾部，再继续想读，还得自己重新打开流
    // 所以，总体感觉 Guava 的 API 设计也很优雅，用起来也很舒服
    @Test
    void readingWithGuava1() throws IOException {
        // 如果要读取文件内容，请使用 Files.asCharSource 以及 Files.asByteSource
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        CharSource charSource = Files.asCharSource(file, StandardCharsets.UTF_8);
        // 将 CharSource 的内容全部读出
        // NOTE: CharSource 读之前会自动打开流，读完自动关闭，因此这个测试用例不需要用 try-with-resources 方式自己处理流的关闭
        System.out.println(charSource.read());
        // 读取首行
        System.out.println(charSource.readFirstLine());
        // 读取所有行
        charSource.readLines().forEach(System.out::println);
        // 利用 <T> T readLines(LineProcessor<T> processor) 还能对每一行进行精细化处理，具体使用场景自由发挥，不测试了

        // 而且 CharSource 和 ByteSource 之间还能轻松互转
        // 也可以通过 Files.asByteSource(file) 得到 ByteSource 实例
        ByteSource byteSource = charSource.asByteSource(StandardCharsets.UTF_8);
        // 一次性读出所有字节
        System.out.println(Arrays.toString(byteSource.read()));
    }

    @Test
    void readingWithGuava2() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        InputStream in = new FileInputStream(file);
        // NOTE: reader 关闭会自动关闭 InputStream
        try (Reader reader = new InputStreamReader(in)) {
            // CharStreams 对实现了 Readable 接口的各种 Reader 提供了很多工具方法：
            // . CharStreams.toString(reader);
            // . CharStreams.readLines(reader);
            // . CharStreams.readLines(reader, lineProcessor);
            System.out.println(CharStreams.toString(reader));
        }
    }

    // 使用 Apache commons 读取文本或字节内容。
    // 它不会自动关闭流。所以，相比之下，还是 Guava 的自动打开和关闭流的方式更优雅好用。
    @Test
    void testReadingWithApacheCommons1() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        String text;
        // Apache commons 库不会自动关闭流，所以这里我们需要自己关闭
        try (InputStream in = new FileInputStream(file)) {
            text = IOUtils.toString(in, StandardCharsets.UTF_8);
        }
        System.out.println(text);
    }

    // 使用 Apache commons 读取文本或字节内容。
    @Test
    void testReadingWithApacheCommons2() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        StringWriter writer = new StringWriter();
        // Apache commons 库不会自动关闭流，所以这里我们需要自己关闭
        try (InputStream in = new FileInputStream(file)) {
            // 还可以通过 copy 方法，将 inputStream 中的内容拷贝到 StringWriter 中，然后通过 StringWriter.toString() 输出
            // copy 方法还有其他很多重载方法供我们使用（略）
            IOUtils.copy(in, writer, StandardCharsets.UTF_8);
        }
        System.out.println(writer);
    }

    @Test
    void testReadingWithScanner() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        InputStream in = new FileInputStream(file);
        String text;
        // Scanner 关闭时，它使用的 InputStream 也会自动关闭
        try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            // 这里指定一个 'A' 字符，它是一个边界标记，表示调用 next 时要达到此边界
            // 本质上，这里意味着读取整个流的内容
            text = scanner.useDelimiter("\\A").next();
        }
        System.out.println(text);
    }

    // =========================

    @Test
    void testReadingWithStringReader() {
        // StringReader:        数据源是字符串的 Reader
        // CharArrayReader:     数据源是 char 数组的 Reader
        // StringReader reader1;
        // CharArrayReader reader2;
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
        // java.nio.file.Files.readString()
    }

    @Test
    void testReadingBytesFromInputStreamWithGuava() throws IOException {
        byte[] helloBytes = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(helloBytes);
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() {
                return in;
            }
        };
        byte[] bytes = byteSource.read();
        Assertions.assertArrayEquals(helloBytes, bytes);
    }

    @Test
    void testReadingTextFromInputStreamWithGuava() throws IOException {
        byte[] helloBytes = "hello".getBytes(StandardCharsets.UTF_8);
        InputStream in = new ByteArrayInputStream(helloBytes);
        CharSource charSource = new CharSource() {
            @Override
            public Reader openStream() {
                return new InputStreamReader(in, StandardCharsets.UTF_8);
            }
        };
        String s = charSource.read();
        Assertions.assertEquals("hello", s);

        // ByteSource 和 CharSource 实例可以互转，可以灵活地为我们提供：“按字节读取” 和 “按字符读取”。
        // ByteSource byteSource = charSource.asByteSource(StandardCharsets.UTF_8);
        // byte[] bytes = byteSource.read();

        // 如果读取的是 FileInputStream, 可使用 Files 工具类
        // String s = Files.asCharSource(file, StandardCharsets.UTF_8).read();
        // byte[] bytes = Files.asByteSource(file).read();
    }

    @Test
    void testReadingTextFromInputStreamWithByteArrayOutputStream() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int n;
        byte[] data = new byte[2];
        while ((n = in.read(data)) != -1) {
            baos.write(data, 0, n);
        }
        baos.flush();
        String s = baos.toString(StandardCharsets.UTF_8); // Since JDK 10
        System.out.println(s);
    }

    // JDK NIO Files 工具类提供了很多工具方法，方便我们读写文件
    @Test
    void testReadingTextFromFileWithJdkNioFilesReadAllBytes() throws IOException {
        File file = prepareTmpFile("hello.txt", "你好", "世界");
        Path path = file.toPath();
        // 注意：以下都不需要处理打开关闭流，工具方法内部会自动打开和关闭（和 Guava 类似，Good）
        // 读取所有字节
        System.out.println(Arrays.toString(java.nio.file.Files.readAllBytes(path)));
        // 读取所有行
        // NOTE: 该方法只适合读取内容较少的文件
        System.out.println(java.nio.file.Files.readAllLines(path));

        // 如果要读取大量数据的文件，使用 BufferedReader 的 readLine()
        try (BufferedReader reader = java.nio.file.Files.newBufferedReader(path)) {
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
        }

        // NOTE: 使用 Stream 时，用完我们需要关闭它
        try (Stream<String> lines = java.nio.file.Files.lines(path)) {
            String data = lines.collect(Collectors.joining("\n"));
            System.out.println(data);
        }

        // 读取所有内容，返回字符串
        System.out.println(java.nio.file.Files.readString(path));
    }
}

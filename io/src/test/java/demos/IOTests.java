package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * IoTests class
 *
 * @author https://github.com/gukt
 */
public class IOTests {

    @Test
    void test1() throws FileNotFoundException {
        File file = new File("a");
        InputStream is = new FileInputStream(file);
        try {
            is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int i = 0;
            int n;
            while ((n = is.read()) != -1) {
                bytes[i++] = (byte) n;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReadingWithApacheCommons() {
        // FIXME: 2021/8/18
        // https://www.baeldung.com/reading-file-in-java
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
    void testReadingWithDataInputStream() {
        // TODO: 2021/8/18  
    }

    @Test
    void testReadingWithFileChannelOfNio() {
        // TODO: 2021/8/18
    }

    @Test
    void testReadingWithGuava() {
        // FIXME: 2021/8/18
    }

    @Test
    void testReadingWithBufferedReader() {
        File file = prepareFile("hello.txt", "hello");
        String content = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            content = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals("hello", content);
    }

    @Test
    void testReadingWithNioFileUtilities() {
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

    private File prepareFile(String filename, String... lines) {
        return prepareFile(filename, false, lines);
    }

    private File prepareFile(String filename, boolean append, String... lines) {
        File file = new File("/tmp", filename);
        try (FileWriter writer = new FileWriter(file, append)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


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

    void readFromInputStream(InputStream is) {

    }

    private String readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        // BufferedReader reader = new BufferedReader(file);
        // FIXME: 2021/8/18
        return "";
    }


}

package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * FileWritingTests class
 *
 * @author https://github.com/gukt
 */
public class FileWritingTests {

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

    private String readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        // BufferedReader reader = new BufferedReader(file);
        // FIXME: 2021/8/18
        return "";
    }
}

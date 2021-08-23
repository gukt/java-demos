package demos;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * TestUtils class
 *
 * @author https://github.com/gukt
 */
public class TestUtils {

    static File prepareTmpFile(String filename, String... lines) {
        return prepareTmpFile(filename, false, lines);
    }

    static File prepareTmpFile(String filename, boolean append, String... lines) {
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

    static String toString(InputStream in) throws IOException {
        return IOUtils.toString(in, StandardCharsets.UTF_8);
    }
}

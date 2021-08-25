package demos;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demo1Tests class
 *
 * @author https://github.com/gukt
 */
public class FileTests {

    // ======================================================================
    // NOTE: 以下测试环境为 linux，且也仅基于 linux 环境讨论。
    // 某些方法内部处理逻辑可能依赖于不同系统，可能有稍许不同，这些不在本测试注释的表述范围内。
    // ======================================================================

    private File ensureFile(String filename) {
        return ensureFile(filename, false);
    }

    private File ensureFile(String filename, boolean deleteOnExists) {
        File file = new File(filename);
        if (file.exists() && deleteOnExists) {
            boolean deleted = file.delete();
            System.out.println("ensureFile - File '" + filename + "' exists, deleted: " + deleted);
        }
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("ensureFile - Created file: " + filename);
                }
            } catch (IOException e) {
                System.out.println("ensureFile -  Failed to create file: " + filename);
            }
        }
        return file;
    }

    private File ensureFileAbsent(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("ensureFileAbsent - Deleted the file: " + filename);
            }
        }
        return file;
    }

    @Test
    void testPath() throws IOException {
        // getPath() 返回的就是使用 new File(filename) 时指定的 pathname。
        // 不管你用的是相对路径还是绝对路径，甚至空路径（""）
        System.out.println(new File("a.txt").getPath());
        System.out.println(new File("./a.txt").getPath());
        System.out.println(new File("../a.txt").getPath());
        System.out.println(new File("").getPath());

        // getAbsolutePath() 返回绝对路径，如果 File 对象的 pathname 路径中包括 "./" 或 "../" 等，也会原封不动的拼接到绝对路径中。
        // 相对路径是从系统属性 user.dir 中解析的。
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io
        System.out.println(System.getProperty("user.dir"));

        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/a.txt
        System.out.println(new File("a.txt").getAbsolutePath());
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/./a.txt
        System.out.println(new File("./a.txt").getAbsolutePath());
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/../a.txt
        System.out.println(new File("../a.txt").getAbsolutePath());
        // 使用 new File 时，参数 pathname 是可以指定为空字符串的
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io
        System.out.println(new File("").getAbsolutePath());
        assertEquals(System.getProperty("user.dir"), new File("").getAbsolutePath());

        // getAbsolutePath() 方法只是将相对路径和 pathname 拼接在一起，即使是使用了 "./", "../" 这种表示法，也会原封不动的表示出来
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/./a.txt
        System.out.println(new File("./a.txt").getAbsolutePath());
        // getCanonicalPath() 会将路径中的".", "../" 等表示为规范路径
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/a.txt
        System.out.println(new File("./a.txt").getCanonicalPath());

        // 对于这种有三个点("...") 的， getCanonicalPath 就不能识别了，此时返回的和 getAbsolutePath 一样
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/.../a.txt
        System.out.println(new File(".../a.txt").getAbsolutePath());
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/.../a.txt
        System.out.println(new File(".../a.txt").getCanonicalPath());

        // 测试这个抽象路径名是否是绝对的。 绝对路径名的定义取决于系统。
        // 在 UNIX 系统上，如果前缀为"/" ，则路径名是绝对路径名。
        // 在 Microsoft Windows 系统上，如果路径名的前缀是驱动器说明符后跟"\\" ，或者其前缀是"\\\\" ，则该路径名是绝对路径名。
        System.out.println(new File("a.txt").isAbsolute());
        System.out.println(new File("/tmp/a.txt").isAbsolute());

        // 前面都是返回 String 的路径，还有其他一些方法返回 File 的，他们是
        File file = new File("../../a.txt");
        // Output: ../..
        System.out.println(file.getParentFile());
        // Output: /Users/ktgu/workspace/projects/900-demos/a.txt
        System.out.println(file.getCanonicalFile());
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/../../a.txt
        System.out.println(file.getAbsoluteFile());
    }

    @Test
    void testIsHidden() {
        // isHidden 是用来判断文件是否是隐藏的，这依赖于不同平台的实现。
        // 对于 linux 系统，以 '.' 开头的文件名（或目录）约定是隐藏的；windows 系统可以设置文件是否为隐藏。
        // 哪怕文件是不存在的，isHidden 也能返回 true/false, 看来 linux 环境下它是直接根据名称约定来判断的（windows 下没有试过）
        File file = new File("/tmp/foo.txt");
        assertFalse(file.isHidden());

        File file12 = ensureFile("/tmp/.gitignore");
        assertTrue(file12.isHidden());
    }

    @Test
    void testPermissions() {
        String pathname = "/tmp/foo.txt";
        File file = ensureFile(pathname, true);
        // 测试应用程序是否可以读取此抽象路径名表示的文件。
        // 在某些平台上，可以使用特殊权限启动 Java 虚拟机，允许它读取标记为不可读的文件。
        // 因此，即使文件没有读取权限，此方法也可能返回true 。
        assertTrue(file.canRead());
        // 测试应用程序是否可以修改此抽象路径名表示的文件。
        // 在某些平台上，可以使用特殊权限启动 Java 虚拟机，允许它修改标记为只读的文件。
        // 因此，即使文件被标记为只读，此方法也可能返回true 。
        assertTrue(file.canWrite());
        // 测试应用程序是否可以执行此抽象路径名表示的文件。
        // 在某些平台上，可以使用特殊权限启动 Java 虚拟机，允许它执行未标记为可执行文件的文件。
        // 因此，即使文件没有执行权限，此方法也可能返回true 。
        assertFalse(file.canExecute());
        // 设置为可执行
        if (file.setExecutable(true)) {
            assertTrue(file.canExecute());
        }
    }

    @Test
    void testGetSpace() {
        // 可以通过一下方法获取存储空间信息（但不保证正确，因为文件系统随时都在变动）
        System.out.println(new File("/").getFreeSpace());
        System.out.println(new File("/").getTotalSpace());
        System.out.println(new File("/").getUsableSpace());
    }

    @Test
    void testIsFileOrDirectory() {
        // 在 linux 中，一切皆文件；就像 Java 中一切皆对象。
        // isFile 和 isDirectory 用来判断是否是文件还是目录；
        // isFile: 仅当文件存在且不是目录时才返回 ture
        File file1 = ensureFile("/tmp/a.txt");
        assertTrue(file1.exists());
        assertTrue(file1.isFile());

        File file2 = new File("/tmp/a");
        // 创建目录，确保目录存在
        if (file2.mkdir()) {
            // 因为是目录，显然 isDirectory 返回 true
            assertTrue(new File("/tmp/a").isDirectory());
        }
    }

    @Test
    void testMisc() {
        // File 用以表示一个文件或目录
        // 分隔符有两种：
        // 目录分隔符 - linux: (/); windows: (\)
        // 路径分隔符 - linux: (:); windows: (;)
        // 获得路径分隔符
        // 一个返回 String，一个返回 char
        System.out.println(File.pathSeparator);
        System.out.println(File.pathSeparatorChar);
        // 获得目录分隔符
        System.out.println(File.separator);
        System.out.println(File.separatorChar);

        File file1 = new File("/tmp");
        File file2 = new File("/tmp/", "/a.txt");
        System.out.println(file2.getAbsolutePath());
    }
}

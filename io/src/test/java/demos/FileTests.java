package demos;

import org.junit.jupiter.api.Assertions;
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
        if(file.exists() && deleteOnExists){
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

    private void ensureFileAbsent(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("ensureFileAbsent - Deleted the file: " + filename);
            }
        }
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

        System.out.println(new File("a.txt").getParent());
        System.out.println(new File("a.txt").getParent());
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
    void testMisc() {
        System.out.println(new File("/").getFreeSpace());
        System.out.println(new File("/").getTotalSpace());
        System.out.println(new File("/").getUsableSpace());

        // 在 linux 中，一切皆文件；就像 Java 中一切皆对象。
        // isFile 和 isDirectory 用来判断是否是文件还是目录；
        assertTrue(ensureFile("/tmp/foo.txt").isFile());
        assertTrue(ensureFile("/tmp").isDirectory());

        File file = new File("/tmp/a.txt");
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getParent());

        String filename = "/tmp/foo.txt";
        ensureFileAbsent(filename);
        assertFalse(new File(filename).exists());

        // getParentFile() 用来访问上级目录；parent() 用来得到上级目录的路径
        File file21 = ensureFile(filename);
        System.out.println(file21.getParentFile());
        System.out.println(file21.getParent());

        // File file = new File("a.txt");
        // file.isAbsolute();
        // // file.compareTo()
        // file.getCanonicalFile();
        // file.getCanonicalPath();
        // file.getName();
        // file.getPath();
        // file.getParent();
    }

    @Test
    void testReadFile() {

        // File 用以表示一个文件或目录
        // 绝对目录就是一个从根目录开始的全路径；相对目录，顾名思义，需要有参照物，即相对于那个目录而言
        // 分隔符有两种：目录分隔符；路径分隔符
        // 目录分隔符：windows(\); linux(/)
        // 路径分隔符 windows (;), linux(:)
        // 所以 windows 下path 一般表示为 path-to-a;path-to-b, linux: path-to-a:path-to-b

        // 获得路径分隔符
        System.out.println(File.pathSeparator);
        System.out.println(File.pathSeparatorChar);

        // 获得目录分隔符（一个返回 String，一个返回 char）
        System.out.println(File.separator);
        System.out.println(File.separatorChar);

        // File 构造函数有 4 个
        String filename = "foo.txt";
        File file1 = new File(filename);
        File file12 = new File("aaa", "b.txt");
        File file3 = new File(file1, "aaa");
        // File file4 = new File()
    }
}

package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * file.mkdir()需要父目录存在，如果不存在将返回false表示创建目录不成功
 */
public class FileTests {

    @Test
    public void testNewFileAndDeleteFile() throws IOException {
        File file = new File("c:/tmp.txt");

        // 如果沒有就创建新文件
        if (!file.exists()) {
            boolean success = file.createNewFile();
            Assertions.assertTrue(success);
        }

        // 删除临时文件
        if (file.exists()) {
            boolean deleted = file.delete();
            Assertions.assertTrue(deleted);
        }
    }

    @Test
    public void testMakeDir() {
        File file = new File("c:/foo/bar");
        file.mkdir();
    }

    @Test
    public void testCreateDir() throws IOException {
        File file = new File("d:/path/to/file.txt");
        if (!file.exists()) {
            // 利用mkdir是不能创建多层目录的，可以使用mkdirs来创建多层目录
            boolean success = file.mkdir();
            Assertions.assertFalse(success);

            // 虽然这样可以自动创建不存在的父目录，但是file.txt也被创建成了目录
            success = file.mkdirs();
            Assertions.assertTrue(success);

            // 递归删除刚刚创建成功的目录
            FileUtils.delete("d:/path");

            success = file.getParentFile().mkdirs();
            Assertions.assertTrue(success);

            // 尽管上面创建父目录是成功了，但是文件file.txt依然是不存在的
            Assertions.assertFalse(file.exists());

            // 调用createNewFile后，会自动创建指定的文件
            boolean created = file.createNewFile();
            Assertions.assertTrue(created);

            // 测试结束，清理
            FileUtils.delete("d:/path");
        }
    }

    @Test
    public void testCreateTmpFile() throws IOException {
        // 在系统默认的临时文件夹下创建临时文件
        File tmpFile1 = File.createTempFile("tmp", ".tbd");
        System.out.println(tmpFile1);

        // 在指定的目录下创建临时文件
        File tmpFile2 = File.createTempFile("tmp", ".tbd", new File("d:/tmp"));
        System.out.println(tmpFile2);

        // 测试结束，清理
        tmpFile1.delete();
        tmpFile2.delete();
    }

    @Test
    public void testRenameOrMove() throws IOException {
        // 在指定的目录下创建临时文件
        File tmpFile = File.createTempFile("tmp", ".tbd", new File("d:/tmp"));
        System.out.println(tmpFile);

        File file = new File("d:/renamed" + System.currentTimeMillis() + ".txt");
        Assertions.assertFalse(file.exists());

        // 如果父目录没发生变化，rename就是重命名，如果父路径发生更改，rename就是移动
        tmpFile.renameTo(file);
        Assertions.assertTrue(file.exists());
        Assertions.assertFalse(tmpFile.exists()); // 临时文件已经没有了

        // 测试结束，清理
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testFileMisc() throws IOException {
        File file = new File("./conf/common/aaa.txt");

        System.out.println("file.getName()=" + file.getName());
        System.out.println("file.getPath()=" + file.getPath());
        System.out.println("file.getParent()=" + file.getParent());
        System.out.println("file.getParentFile()=**File**:" + file.getParentFile());

        System.out.println("file.getAbsolutePath()=" + file.getAbsolutePath());
        System.out.println("file.getAbsoluteFile()=**File**:" + file.getAbsoluteFile());

        System.out.println("file.getCanonicalPath()=" + file.getCanonicalPath());
        System.out.println("file.getCanonicalFile()=" + file.getCanonicalFile());

        System.out.println(file.isFile()); // true;
        System.out.println(file.isDirectory()); // false;
        System.out.println(file.isHidden()); // false;

        // 是否是绝对地址，UNIX以/开头，Windows以盘符开头
        System.out.println(file.isAbsolute()); // true;

        Assertions.assertTrue(file.canWrite());
        Assertions.assertTrue(file.canRead());
        Assertions.assertTrue(file.canExecute());
    }
}

package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

/**
 * PathTests class
 *
 * @author https://github.com/gukt
 */
public class PathTests {

    @Test
    void testPathStartsWithAndEndsWith() {
        // startsWith 和 endsWith 都分别有两个重载方法，一个接受 Path 类型参数，一个接受 String 类型参数
        Path path1 = Paths.get("/tmp/a");
        Path path2 = Paths.get("a");
        // 接受 Path 类型参数
        Assertions.assertTrue(path1.endsWith(path2));
        // 接受 String 类型参数
        Assertions.assertTrue(path1.endsWith("a"));

        Assertions.assertTrue(path1.startsWith(Paths.get("/tmp")));
        Assertions.assertTrue(path1.startsWith("/tmp"));
        // 这里返回的是 false，因为显然 /tmp 和 tmp 表示的是两个不同的路径
        // tmp 表示的是相对路径，/tmp 表示的是绝对路径（linux 下）
        Assertions.assertFalse(path1.startsWith("tmp"));
    }

    @Test
    void testPathComparing() {
        // Path 继承自 Comparable，所以两个 Path 对象是可以比较的
        // 比较时不访问文件系统，也不需要存在任何文件，仅仅是按字段顺序比较两个抽象路径，
        // 此方法排序基于特定文件系统提供者（FileSystemProvider）
        //
        // Output: 0
        System.out.println(Paths.get("a").compareTo(Paths.get("a")));
        // Output: -1
        System.out.println(Paths.get("a").compareTo(Paths.get("b")));
        // Output: 40
        System.out.println(Paths.get("a").compareTo(Paths.get("9")));
        // -2
        System.out.println(Paths.get("a").compareTo(Paths.get("a/b")));
        // -4
        System.out.println(Paths.get("a").compareTo(Paths.get("a/b/c")));
    }

    @Test
    void testPathCreation() {
        // Path 类只是一个接口，要实例化请使用 Paths 工具类
        // Paths.get() 方法可以提供一个或多个路径元素
        // Output: a
        System.out.println(Paths.get("a"));
        // Output: a/b
        System.out.println(Paths.get("a", "b"));
        // Output: a/b/c
        System.out.println(Paths.get("a", "b", "c"));
        // 再也不同担心或自行处理每个路径前后多余的 / 了
        // Output: a/b/c
        System.out.println(Paths.get("a//", "/b", "/c/"));

        // 路径组件中也支持 . 和 ..
        Path path = Paths.get("a", "./b", "../c");
        // Output: a/./b/../c
        System.out.println(path);
        // 上面这种输出是合法的，但是很难一眼看出真实的路径到底是什么，可以通过 normalize 方法得到标准路径
        // 上面的路径，经过 normalize 后一眼就能看出，路径中的名称元素 'b' 在规范后的路径中已经不存在了
        // Output: /a/c
        System.out.println(path.normalize());
    }

    @Test
    void testRelativeAndAbsolutePath() {
        // Path 有相对路径和绝对路径之分：
        // 绝对路径 - 表示从文件系统的根路径开始，比如 /tmp 就是绝对路径。
        // 相对路径 - 表示相对于某个路径
        //
        // Output: true
        System.out.println(Paths.get("/a").isAbsolute());
        // Output: false
        System.out.println(Paths.get("a").isAbsolute());
        // toAbsolutePath() 用来将“相对路径”转换为“绝对路径”，如果 path 已经是绝对路径，则原样返回
        // Output: /a/b
        System.out.println(Paths.get("/a/b").toAbsolutePath());
        // 转换为绝对路径时，依赖 FileSystemProvider 的具体实现，
        // 在 Unix 系统上（或基于 BSD 的 MacOS 上），文件系统默认目录是 user.dir 环境变量指定的路径。
        // 在 Windows 系统上，默认目录是由环境变量 default.dir 指定的路径。
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io
        System.out.println(System.getProperty("user.dir"));
        // Output: /Users/ktgu/workspace/projects/900-demos/java-demos/io/a/c
        System.out.println(Paths.get("a/c").toAbsolutePath());

        // toRealPath() 用来返回“存在的”文件的真实路径，如果文件不存在，则抛出 java.nio.file.NoSuchFileException
        Assertions.assertThrows(NoSuchFileException.class, () -> System.out.println(Paths.get("/a/b").toRealPath()));
    }

    @Test
    void testGetComponentsOfPath() {
        // NOTE：本测试基于 Unix 系统文件系统举例

        // Path 路径是分层次的，每个层次我们称之为“路径组件”
        // Path 接口提供了多个方法帮助我们获取路径的各个路径组件
        // 比如：getFileName(), getParent(), getRoot() 方法，注意：他们的返回值都是 Path 类型
        //
        // getFileName 用来返回此路径表示的“文件或目录”，文件名是目录层次结构中离 root 最远的元素。
        // Output: b
        System.out.println(Paths.get("/tmp/a/b").getFileName());
        // Output: /tmp/a
        System.out.println(Paths.get("/tmp/a/b").getParent());
        // 对于 getRoot() 来说，如果是绝对路径，返回 /; 反之，返回 null
        // Output: /
        System.out.println(Paths.get("/tmp/a/b").getRoot());
        // Output: null
        System.out.println(Paths.get("tmp/a/b").getRoot());
    }

    @Test
    void testGetNameElementOfPath() {
        // getNameCount() 用来返回 Path 中名称元素的个数,
        // Output: 3
        System.out.println(Paths.get("tmp/a/b").getNameCount());
        // getName(index) 可以通过索引返回这些名称元素，比起以前我们对路径进行字符串解析，要方便很多
        // Output: tmp (注意这里的返回值是不带前缀 / 的）
        System.out.println(Paths.get("/tmp/a/b").getName(0));
        // Output: a
        System.out.println(Paths.get("/tmp/a/b").getName(1));
        // Output: b
        System.out.println(Paths.get("/tmp/a/b").getName(2));
        // 关于 getNameCount() 方法还要注意在使用了 . 或 ../ 的路径时的情况
        Path path1 = Paths.get("a", "./b", "../c");
        // Output: a/./b/../c
        System.out.println(path1);
        // Output: /a/c
        System.out.println(path1.normalize());
        // Output: 5 (注意：这里是 5，使用的是没有 normalize 前表示的路径）
        System.out.println(path1.getNameCount());

        // 还提供了 subpath(beginIndex, endIndex) 方法，用以获取指定索引区间（上下界都是开区间）的 names
        // Output: a
        System.out.println(Paths.get("a/b/c/d/e").subpath(0, 1));
        // Output: b
        System.out.println(Paths.get("a/b/c/d/e").subpath(1, 2));
        // Output: a/b
        System.out.println(Paths.get("a/b/c/d/e").subpath(0, 2));
        // Output: a/b
        System.out.println(Paths.get("/a/b/c/d/e").subpath(0, 2));

        // Path 接口继承了 Iterable 接口，因此可以用 forEach 将 path 中的每个“名称元素”遍历出来
        Path path3 = Paths.get("/a/", "../b/", "./c");
        for (Path path4 : path3) {
            System.out.println(path4);
        }
    }

    @Test
    void testFileSystem() {
        // Output: /
        FileSystems.getDefault().getRootDirectories().forEach(System.out::println);

        FileSystem fs = FileSystems.getDefault();
        System.out.println(fs);
    }

    @Test
    void testInterconversion() {
        // Path 可以转换为 File 和 Uri 对象
        Path path1 = Paths.get("a/c");
        System.out.println(path1.toFile());
        // 当然，File 对象也可以调用 toPath 转换为 Path 对象
        System.out.println(path1.toFile().toPath());
        // TODO: 2021/8/24 进一步查看文档细节
        // toUri()
        System.out.println(path1.toUri());
        // File.toURI()
        System.out.println(path1.toFile().toURI());
    }

    @Test
    void test1() throws IOException {
        // Path 类是一个接口，要实例化请使用 Paths 工具类
        Path path1 = Paths.get("a/c");
        Path path2 = Paths.get("a/b/../c");
        System.out.println(path1.compareTo(path2));
        System.out.println(path1.equals(path2));

        System.out.println("here");

        // 两个 register 方法
        // path.register();
        // path.relativize();
        // 两个 resolve 方法
        // path.resolve()

        // path.resolveSibling();
        // path.toAbsolutePath();
        // path.toRealPath();
        // path.spliterator()

        // 获取默认的 FileSystem
        FileSystem fs = FileSystems.getDefault();

        // Output: /
        // fs.getSeparator();
        // fs.isOpen();
        // fs.isReadOnly();
        // fs.provider();
        // fs.getRootDirectories();
        // fs.getFileStores();
        // fs.getPath("/tmp", "a", "b");
        // fs.getUserPrincipalLookupService();
    }
}

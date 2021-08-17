package net.bafeimao.examples.test.guava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import net.bafeimao.examples.util.FileUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class GuavaFileTests {
	private Charset utf8 = Charset.forName("utf-8");
	private File file;

	@Before
	public void before() throws IOException {
		file = new File("d:/guava.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		Files.append("hello\n", file, utf8);
		Files.append("world", file, utf8);
	}

	@After
	public void after() {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testReadTextFile() throws IOException {
		// 获得字节数组然后输出
		byte[] bytes = Files.toByteArray(file);
		System.out.println(new String(bytes));

		// 直接输入字符串，效果同上面是一样的
		Files.toString(file, Charset.forName("utf-8"));

		// 还可以从BufferedReader里面读
		BufferedReader reader = Files.newReader(file, utf8);
		String lineText;
		while ((lineText = reader.readLine()) != null) {
			System.out.println(lineText);
		}
		reader.close();

		// 读第一行
		String text = Files.readFirstLine(file, Charset.forName("utf-8"));
		System.out.println(text);

		// 读所有的行，并放到一个List中
		List<String> lines = Files.readLines(file, Charset.forName("utf-8"));
		System.out.println(lines);

		// 逐行读还能逐行调用相应的回调函数，

		/**
		 * 下面的LineProcessor是从{@link FileUtils#readLines}方法中抄过来的
		 * 目的是说明，我们可以通过设定LineProcessor加入更多的控制
		 * */
		Files.readLines(file, Charset.forName("utf-8"), new LineProcessor<List<String>>() {
			final List<String> result = Lists.newArrayList();

			@Override
			public boolean processLine(String line) {
				result.add(line);
				return true;
			}

			@Override
			public List<String> getResult() {
				return result;
			}
		});
	}

	@Test
	public void testOverrideWriteTextFile() throws IOException {
		// 写内容到文件中，会覆盖原内容
		Files.write("hello".getBytes(), file);
		Files.write("hello1", file, Charset.forName("utf-8"));

		// 如果要附加内容，请用append方法
		Files.append("\nhello1", file, Charset.forName("utf-8"));
	}

	@Test
	public void testFileCopyAndEquality() throws IOException {
		File copiedFile1 = new File("d:/guava-copied.txt");

		// Copy to file directly
		Files.copy(file, copiedFile1);

		Files.equal(file, copiedFile1);

		// Copy to FileOutputStream
		Files.copy(file, new FileOutputStream("d:/guava-copied2.txt"));

		// 拷贝文件内容到一个Appendable接口实例
		// 常见实现Appendable接口的类有：StringBuilder, StringBuffer, Writer, PrintStream, CharBuffer
		FileWriter writer = new FileWriter("d:/guava1.txt");
		Files.copy(file, Charset.forName("utf-8"), writer);

		// StringBuilder也是一种常见的实现Appenable接口的类
		StringBuilder sBuilder = new StringBuilder();
		Files.copy(file, Charset.forName("utf-8"), sBuilder);
		System.out.println(sBuilder.toString());

		FileUtils.delete("d:/guava1.txt");
		FileUtils.delete("d:/guava-copied.txt");
		FileUtils.delete("d:/guava-copied2.txt");
	}

	/**
	 * 测试测试创建父路径以及创建临时路径
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateParentDir() throws IOException {
		// 创建"d:/path/to/some"路径(但是dir1不会被创建)
		File path = new File("d:/path/to/some/dir1/");
		Files.createParentDirs(path);

		// C:\Users\GUKAIT~1\AppData\Local\Temp\1428054149846-0
		File tmpDir = Files.createTempDir();
		System.out.println(tmpDir);
	}

	@Test
	public void testMisc() {
		// 获得文件名（不带扩展名）和扩展名
		System.out.println(Files.getFileExtension("d:/xxx.yyy")); // yyy
		System.out.println(Files.getNameWithoutExtension("d:/xxx.yyy")); // xxx

		// C:\Users\GUKAIT~1\AppData\Local\Temp\1428054149846-0
		File tmpDir = Files.createTempDir();
		System.out.println(tmpDir);
	}

	@Test
	public void tes1() throws IOException {
		TreeTraverser<File> fileTraverser = Files.fileTreeTraverser();
		Iterable<File> fileIterable = fileTraverser.children(new File("d:\\"));

		FluentIterable<File> iterable = fileTraverser.breadthFirstTraversal(new File("d:\\"));
		Optional<File> fileOptional = iterable.first();
		File file = fileOptional.get();
		System.out.println(file);

		for (File f : fileIterable) {
			System.out.println(f.getName());
		}

		Files.simplifyPath("d://guava.txt");
	}

	@Test
	public void testCreateTmpDir() throws IOException {
		/**
		 * 由于File提供了创建临时文件的方法，但是在临时目录下创建临时目录并不方便，所以Guava提供了另一个创建临时文件夹的方法
		 * 
		 * Guava推荐我们在创建临时文件夹（而不是文件）的时候使用该方法， 当然了，创建临时文件仍然需要使用File.createTempFile()了
		 * 
		 * 如果我们不用Guava提供的createTempDir方法，而是我们自己创建临时文件夹
		 * 那我们就需要小心的处理多线程环境下可能产生的竞争条件（race condition)的问题
		 * 
		 * */
		File dir = Files.createTempDir();
		System.out.println(dir);

		File tmpFile = File.createTempFile("tmp", "");
		System.out.println(tmpFile);
		boolean success = tmpFile.mkdir();
		System.out.println(success);
	}

	@Test
	public void testIsDirAndIsFile() {
		Files.isDirectory().apply(new File("d:")); // true;
		Files.isFile().apply(new File("d:/guava.txt")); // true

		// 这和直接调用file.isDirectory() / file.isFile()有和区别？??
		// Files提供这两个方法似乎没太大意义
		new File("d:").isDirectory(); // true
		new File("d:/guava.txt").isFile(); // true
	}

	/**
	 * 测试文件移动（也可以称之为重命名，实际意义是一样的)
	 * 
	 * @throws IOException
	 */
	@Test
	public void testRenameOrMove() throws IOException {
		Files.move(file, new File("d:/guava-renamed.txt"));
	}

	@Test
	public void testCreateParentDirs() throws IOException {
		File file = new File("d:/path/to/file.txt");
		Files.createParentDirs(file);

		// 检查目录被成功创建了
		File file2 = new File("d:/path/to");
		Assert.assertTrue(file2.exists());
		Assert.assertTrue(file2.isDirectory());

		// d:/path/to目录被创建了，但是文件file.txt并没有创建
		boolean exists = file.exists();
		Assert.assertFalse(exists);

		// 如果要创建文件，直接调用JDK提供的方法即可
		boolean created = file.createNewFile();
		Assert.assertTrue(created);

		// 测试完成，清理
		FileUtils.delete("d:/path");
	}
}

package net.bafeimao.examples.test.java;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class StreamTests {

	@Test
	public void testFileInputStream() throws IOException {
		FileInputStream fis = new FileInputStream("d:/test.txt");

		try {
			// 设置接受数据的字节数组,设置其大小正好是fis当前可以读的有效字节数大小
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			System.out.println(new String(bytes));
		} finally {
			try {
				fis.close();
			} catch (Exception ignored) {
			}
		}
	}

	@Test
	public void testCopyFile1() throws IOException {
		FileInputStream fis = new FileInputStream("d:/test.txt");
		FileOutputStream fos = new FileOutputStream("d:/test1.txt");

		// 至此，test.txt文件已自动被创建，并且处于使用状态

		try {
			// 循环逐字节读取，边读边写入FileOutputStream
			int b = fis.read();
			while (b != -1) {
				fos.write(b);
				b = fis.read();
			}
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception ignored) {
			}
		}
	}

	@Test
	public void testCopyFile2() throws IOException {
		FileInputStream fis = new FileInputStream("d:/test.txt");
		FileOutputStream fos = new FileOutputStream("d:/test1.txt");

		try {
			// 一次性读取文件所有字节到bytes，然后一次性写入FileOutputStream
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);
			fos.write(bytes);
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * 直接往一个FileOutputStream中写入字节就可以创建一个新文件
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateFileByFileOutputStream() throws IOException {
		FileOutputStream fos = new FileOutputStream("d:/aaa.txt");
		try {
			fos.write("hello".getBytes());
		} finally {
			try {
				fos.close();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * 测试证明，使用BufferedOutputStream包装过的FileOutputStream在效率上明显差别十几倍乃至几十倍，取决于文件大小
	 * 
	 * @throws IOException
	 */
	@Test
	public void testPerformanceWithBufferedOutputStream() throws IOException {
		int n = 10000;

		FileOutputStream fos = new FileOutputStream("d:/aaa.txt");
		try {
			// 直接利用FileOutputStream写入字节
			long start = System.currentTimeMillis();
			for (int i = 0; i < n; i++) {
				fos.write(("the line#" + i).getBytes());
			}
			System.out.println("total:" + (System.currentTimeMillis() - start));

		} finally {
			try {
				fos.close();
			} catch (Exception ignored) {
			}
		}

		FileOutputStream fos2 = new FileOutputStream("d:/bbb.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos2);
		try {
			// 下面利用BufferedOutputStream写入字节
			long start = System.currentTimeMillis();
			for (int i = 0; i < n; i++) {
				bos.write(("the line#" + i).getBytes());
			}
			// bos.flush();
			System.out.println("total:" + (System.currentTimeMillis() - start));
		} finally {
			try {
				bos.close();
			} catch (Exception ignored) {
			}
		}
	}
}

package net.bafeimao.examples.test.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ReaderTests {

	/**
	 * 测试逐字符读取文本文件，并打印
	 * 
	 * @throws IOException
	 */
	@Test
	public void testReadByFileReader() throws IOException {
		FileReader reader = new FileReader("d:/aaa.txt");

		try {
			int readedChar;
			while ((readedChar = reader.read()) != -1) {
				System.out.print((char) readedChar);
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * 测试一次读取多个字符到数组中，并打印
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBatchReadByFileReader() throws IOException {
		FileReader reader = new FileReader("d:/aaa.txt");
		try {
			char[] someChars = new char[1024];
			int n = reader.read(someChars);
			System.out.println("Readed " + n + " characters, they are:");
			System.out.print(someChars);
		} finally {
			reader.close();
		}
	}

	@Test
	public void testReadTextByBufferredReader() throws IOException {
		// 由InputStream构造出reader，再讲reader装饰进BufferedReader
		FileInputStream fis = new FileInputStream("d:/aaa.txt");
		InputStreamReader reader = new InputStreamReader(fis);
		BufferedReader bufferedReader = new BufferedReader(reader);

		// BufferedReader顾名思义，性能上肯定要好些，而且它还提供readLine方法，读取行信息更方便
		try {
			String text;
			while ((text = bufferedReader.readLine()) != null) {
				System.out.println(text);
			}
		} finally {
			bufferedReader.close();
		}
	}
}

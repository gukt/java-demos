package net.bafeimao.examples.test.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class ResourceTest {

	@Test
	public void testByteArrayResource() {
		Resource resource = new ByteArrayResource("Hello World!".getBytes());

		Assert.assertEquals(false, resource.isOpen());

		if (resource.exists()) {
			// 因为isOpen=false，所以可读取多次
			dumpStream(resource);
			dumpStream(resource);
			dumpStream(resource);
		}
	}

	@Test
	public void testInputStreamResource() {
		ByteArrayInputStream bis = new ByteArrayInputStream("Hello World!".getBytes());
		Resource resource = new InputStreamResource(bis);

		Assert.assertEquals(true, resource.isOpen());

		if (resource.exists()) {
			dumpStream(resource);
			// dumpStream(resource); // 多次读取会出错，因为该资源的isOpen=true
		}

	}

	@Test
	public void testFileResource() {
		Resource resource = new FileSystemResource("d:/temp/temp1.txt");

		// Alternative usage
		// File file = new File("d:/temp/temp1.txt");
		// Resource resource = new FileSystemResource(file);

		if (resource.exists()) {
			dumpStream(resource);
		}
		Assert.assertEquals(false, resource.isOpen());
	}

	@Test
	public void testClasspathResourceByDefaultClassLoader() throws IOException {
		Resource resource = new ClassPathResource("net/bafeimao/examples/spring/resources/test1.properties");
		if (resource.exists()) {
			dumpStream(resource);
		}
		System.out.println("path:" + resource.getFile().getAbsolutePath());
		Assert.assertEquals(false, resource.isOpen());
	}

	@Test
	public void testClasspathResourceByClassLoader() throws IOException {
		ClassLoader cl = this.getClass().getClassLoader();
		Resource resource = new ClassPathResource("net/bafeimao/examples/spring/resources/test1.properties", cl);
		if (resource.exists()) {
			dumpStream(resource);
		}
		System.out.println("path:" + resource.getFile().getAbsolutePath());
		Assert.assertEquals(false, resource.isOpen());
	}

	@Test
	public void testClasspathResourceByClass() throws IOException {
		Class<?> clazz = this.getClass();
		Resource resource1 = new ClassPathResource("cn/javass/springmvc/chapter4/test1.properties", clazz);
		if (resource1.exists()) {
			dumpStream(resource1);
		}
		System.out.println("path:" + resource1.getFile().getAbsolutePath());
		Assert.assertEquals(false, resource1.isOpen());

		Resource resource2 = new ClassPathResource("test1.properties", this.getClass());
		if (resource2.exists()) {
			dumpStream(resource2);
		}
		System.out.println("path:" + resource2.getFile().getAbsolutePath());
		Assert.assertEquals(false, resource2.isOpen());
	}

	private void dumpStream(Resource resource) {
		InputStream is = null;
		try {
			// 1.获取文件资源
			is = resource.getInputStream();
			// 2.读取资源
			byte[] descBytes = new byte[is.available()];
			is.read(descBytes);
			System.out.println(new String(descBytes));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 3.关闭资源
				is.close();
			} catch (IOException e) {
			}
		}
	}

}

package net.bafeimao.examples.util;

import java.io.File;

/**
 * 工具类
 * 
 * @author gukaitong
 * 
 */
public class FileUtils {
	/**
	 * 递归删除目录或文件
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
		} else {
			// 递归删除子目录
			File[] files = file.listFiles();
			for (File f : files) {
				delete(f);
				f.delete();
			}

			// 删除当前目录
			file.delete();
		}
	}

	/**
	 * 递归删除目录或文件
	 * 
	 * @param pathname
	 */
	public static void delete(String pathname) {
		if (pathname == null || pathname.length() == 0)
			return;

		delete(new File(pathname));
	}
}
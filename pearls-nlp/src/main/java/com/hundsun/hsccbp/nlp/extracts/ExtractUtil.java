package com.hundsun.hsccbp.nlp.extracts;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.codahale.metrics.annotation.Timed;

public class ExtractUtil {
	public static List<String> fileList = new ArrayList<String>();

	/**
	 * 遍历整个文件夹，找出所有文件
	 * 
	 * @param strPath
	 * @return
	 */
	@Timed
	public static List<String> listDepthFiles(String strPath) {
		File dir = new File(strPath);
		// TODO,应该加过滤器，是列出html结尾的文件。File[] files = dir.listFiles(filter);
		File[] files = dir.listFiles();
		if (files == null) {
			return null;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				listDepthFiles(files[i].getAbsolutePath());
			} else {
				String strFileName = files[i].getAbsolutePath();
				// System.out.println("----" + strFileName);
				fileList.add(strFileName);
			}
		}
		return fileList;
	}

	/**
	 * 创建抽象路径所所对应的目录，如果抽象路径本身就表示目录，则直接创建对应目录，如果如果抽象路径本身表示为文件，则创建文件所在的目录
	 * 
	 * @param filePath
	 */
	public static boolean mkdirs(Path filePath) {
		boolean mkSuccess = false;
		// TODO 此处不知怎么判断filePath是目录还是文件
		// File file = filePath.toFile();
		// if (file.isFile()){//TODO
		File file = filePath.getParent().toFile();
		// }
		if (!file.exists()) {
			mkSuccess = file.mkdirs();
		}
		return mkSuccess;
	}

	/**
	 * 把字符从srcCharset编码类型转换成destCharset编码类型
	 * @param src
	 * @param srcCharset
	 * @param destCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String src, String srcCharset,
			String destCharset) throws UnsupportedEncodingException {
		byte[] temp = src.getBytes(srcCharset);// 这里写原编码方式
		byte[] newtemp = new String(temp, srcCharset).getBytes(destCharset);// 这里写转换后的编码方式
		String dest = new String(newtemp, destCharset);// 这里写转换后的编码方式
		//System.out.println(dest);
		return dest;
	}
}

package com.yang.common.util.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.yang.common.util.MyTool;

/**
 * IO工具类
 * @author yanglei 
 * 2017年7月27日 下午3:52:09
 */
public class IOUtils {

	private IOUtils() {
	}

	private static IOUtils io = null;

	public static IOUtils getSingleInstance() {
		if (null == io) {
			synchronized (IOUtils.class) {
				if (null == io) {
					io = new IOUtils();
				}
			}
		}
		return io;
	}

	/**
	 * 关闭对象
	 * 
	 * @param closeable
	 */
	public void close(Closeable... closeable) {
		if (null != closeable && closeable.length >= 1) {
			for (Closeable c : closeable) {
				if (null != c) {
					try {
						c.close();
					} catch (IOException i) {
					}
				}
			}
		}
	}

	/**
	 * 加载属性文件
	 * 
	 * @param path
	 * @return
	 */
	public Properties loadProperties(String path) {
		Properties properties = null;
		InputStream is = null;
		try {
			properties = new Properties();
			is = getInputStream(path);
			if (null == is) {
				throw new RuntimeException("请检查" + path + "是否存在");
			}
			properties.load(is);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException("加载属性文件[" + path + "]出现异常", e);
		} finally {
			close(is);
		}
	}

	/**
	 * 加载属性文件中前缀为prefix的属性
	 * 
	 * @param path
	 * @param prefix
	 * @return
	 */
	public Properties loadProperties(String path, String prefix) {
		Properties properties = loadProperties(path);
		Properties rs = new Properties();
		int index = prefix.length();
		for (Iterator<Object> i = properties.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			if (key.startsWith(prefix) && key.length() > index) {
				rs.put(key.substring(index + 1), properties.get(key));
			}
		}
		return rs;
	}

	/**
	 * 根据路径获取实际文件
	 * 
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unused")
	private File getFile(String path) {
		String f = path;
		try {
			if (f.toLowerCase().startsWith("classpath:"))// 类路径
			{
				f = f.substring(10);
				f = Thread.currentThread().getContextClassLoader()
						.getResource(f).getPath();
			} else if (f.toLowerCase().startsWith("file:")) {
				f = f.substring(5);
			} else// WEB路径
			{
				File file = new File(Thread.currentThread()
						.getContextClassLoader().getResource("").getPath());
				File[] files = file.getParentFile().getParentFile()
						.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.equals("WebContent")
										|| name.equals("WebRoot")
										|| name.equals("WEB-INF");
							}
						});
				if (!f.startsWith("/")) {
					f = "/" + f;
				}
				String tmp = files[0].getPath();
				if (tmp.endsWith("WEB-INF") && f.startsWith("/WEB-INF")) {
					tmp = tmp.substring(0, tmp.length() - 7);
				}
				f = tmp + f;
			}
		} catch (Exception e) {
			throw new RuntimeException("找不到文件：" + path, e);
		}
		return new File(f);
	}

	/**
	 * 跟据路径获取输入流
	 * 
	 * @param path
	 * @return
	 */
	public InputStream getInputStream(String path) {
		try {
			return new PathMatchingResourcePatternResolver().getResource(path).getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("访问资源出现异常：" + path, e);
		}
	}

	/**
	 * 跟据路径获取输入流
	 * 
	 * @param path
	 * @return
	 */
	public InputStream getInputStream(String path, Class<?> cls) {
		try {
			return new ClassPathResource(path, cls).getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("访问资源出现异常：" + path, e);
		}
	}

	/**
	 * 跟据路径获取输入流
	 * 
	 * @param path
	 * @return
	 */
	public InputStream getInputStream(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (Exception e) {
			throw new RuntimeException("找不到文件：" + file.getPath(), e);
		}
	}

	public List<String> readLines(InputStream input) {
		InputStreamReader reader = new InputStreamReader(input);
		return readLines(reader);
	}

	public List<String> readLines(Reader input) {
		try {
			BufferedReader reader = new BufferedReader(input);
			List<String> list = new ArrayList<String>();
			String line = reader.readLine();
			while (line != null) {
				list.add(line);
				line = reader.readLine();
			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Iterator<String> iterator(InputStream input) {
		return iterator(input, null);
	}

	public Iterator<String> iterator(InputStream input, String encoding) {
		try {
			Reader reader = null;
			if (encoding == null) {
				reader = new InputStreamReader(input);
			} else {
				reader = new InputStreamReader(input, encoding);
			}
			return new LineIterator(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 文件复制
	 * 
	 * @param src
	 * @param target
	 * @return
	 */
	public int copy(File src, File target) {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(target);
			return copy(is, os);
		} catch (IOException e) {
			throw new RuntimeException("复制文件 时出现异常", e);
		} finally {
			close(os, is);
		}
	}

	/**
	 * 流复制
	 * 
	 * @param input
	 * @param output
	 * @return
	 */
	public int copy(InputStream input, OutputStream output) {
		try {
			byte[] buffer = new byte[4096];
			long count = 0;
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
			}
			return (int) count;
		} catch (IOException e) {
			throw new RuntimeException("复制流 时出现异常", e);
		}
	}

	/**
	 * 流复制
	 * 
	 * @param input
	 * @param output
	 * @param length
	 * @return
	 */
	public long copy(InputStream input, OutputStream output, long length) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(output);
			long total = length;
			int maxRead = 8192;
			byte[] buffer = null;
			while (total > 0) {
				int read = total > maxRead ? maxRead : (int) total;
				buffer = new byte[read];
				input.read(buffer);
				bos.write(buffer);
				total = total - read;
			}
		} catch (IOException e) {
			throw new RuntimeException("复制流 时出现异常", e);
		} finally {
			MyTool.IO.close(bos);
		}
		return length;
	}

	private static class LineIterator implements Iterator<String> {
		private final BufferedReader bufferedReader;
		private String cachedLine;
		private boolean finished = false;

		public LineIterator(final Reader reader)
				throws IllegalArgumentException {
			if (reader == null) {
				throw new IllegalArgumentException("Reader must not be null");
			}
			if (reader instanceof BufferedReader) {
				bufferedReader = (BufferedReader) reader;
			} else {
				bufferedReader = new BufferedReader(reader);
			}
		}

		public boolean hasNext() {
			if (cachedLine != null) {
				return true;
			} else if (finished) {
				return false;
			} else {
				try {
					while (true) {
						String line = bufferedReader.readLine();
						if (line == null) {
							finished = true;
							return false;
						} else if (isValidLine(line)) {
							cachedLine = line;
							return true;
						}
					}
				} catch (IOException ioe) {
					close();
					throw new IllegalStateException(ioe);
				}
			}
		}

		protected boolean isValidLine(String line) {
			return true;
		}

		public String next() {
			return nextLine();
		}

		public String nextLine() {
			if (!hasNext()) {
				throw new RuntimeException("No more lines");
			}
			String currentLine = cachedLine;
			cachedLine = null;
			return currentLine;
		}

		public void close() {
			finished = true;
			MyTool.IO.close(bufferedReader);
			cachedLine = null;
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"Remove unsupported on LineIterator");
		}
	}
}

package com.yang.common.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.util.MyTool;

/**
 * Ftp工具类
 * @author yanglei 
 * 2017年7月27日 下午3:54:09
 */
public class FtpUtils {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private FtpUtils() {
	}

	private static FtpUtils ftp = null;

	public static FtpUtils getSingleInstance() {
		if (null == ftp) {
			synchronized (FtpUtils.class) {
				if (null == ftp) {
					ftp = new FtpUtils();
				}
			}
		}
		return ftp;
	}

	/**
	 * 从FTP服务器下载文件
	 * 
	 * @param server
	 *            FTP服务器IP
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            登录用户名
	 * @param password
	 *            密码
	 * @param ftpFolder
	 *            FTP服务器文件夹路径
	 * @param localFolder
	 *            本地存储文件夹路径
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public boolean getFile(String server, int port, String username,
			String password, String ftpFolder, String localFolder,
			String fileName) {
		boolean rtnFlag = false;
		FTPClient ftp = new FTPClient();
		FileOutputStream fos = null;

		try {
			logger.info(" connecting FTP server " + server + " : " + port + " ...");
			// 连接FTP服务器
			ftp.connect(server, port);
			logger.info(" connected -------- " + server + " : " + port);

			// 登陆FTP服务器
			if (!ftp.login(username, password)) {
				throw new RuntimeException("登录FTP服务器失败");
			}
			logger.info(" user login ------- " + username + " : " + password);

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			// 设置下载目录
			if (!ftp.changeWorkingDirectory(ftpFolder)) {
				throw new RuntimeException("FTP服务器上不存在文件夹 " + ftpFolder);
			}

			// 创建文件夹
			File fileFold = new File(localFolder);
			if (!fileFold.exists()) {
				fileFold.mkdirs();
			}
			File file = new File(localFolder, fileName);
			fos = new FileOutputStream(file);

			// 下载文件
			if (!ftp.retrieveFile(fileName, fos)) {
				fos.close();
				file.delete();
				return false;
			}
			ftp.logout();
			logger.info(" recv file -------- " + localFolder + "/" + fileName);

			rtnFlag = true;
		} catch (Exception e) {
			throw new RuntimeException("与FTP服务器通讯出错：" + e.getMessage());
		} finally {
			MyTool.IO.close(fos);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					throw new RuntimeException("关闭FTP连接发生异常");
				}
			}
		}
		return rtnFlag;
	}

	/**
	 * 从FTP服务器下载文件
	 * 
	 * @param server
	 *            FTP服务器IP
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            登录用户名
	 * @param password
	 *            密码
	 * @param ftpFolder
	 *            FTP服务器文件路径
	 * @param ftpFileName
	 *            FTP服务器文件名
	 * @param localFolder
	 *            本地存储文件夹路径
	 * @param localFileName
	 *            本地存储文件名
	 * @return
	 * @throws Exception
	 */
	public boolean getFile1(String server, int port, String username,
			String password, String ftpFolder, String ftpFileName,
			String localFolder, String localFileName) {
		boolean rtnFlag = false;
		FTPClient ftp = new FTPClient();
		FileOutputStream fos = null;

		try {
			logger.info(" connecting FTP server " + server + " : " + port + " ...");
			// 连接FTP服务器
			ftp.connect(server, port);
			logger.info(" connected -------- " + server + " : " + port);

			// 登陆FTP服务器
			if (!ftp.login(username, password)) {
				throw new RuntimeException("登录FTP服务器失败");
			}
			logger.info(" user login ------- " + username + " : " + password);

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			// 设置下载目录
			if (!ftp.changeWorkingDirectory(ftpFolder)) {
				throw new RuntimeException("FTP服务器上不存在文件夹 " + ftpFolder);
			}

			// 创建文件夹
			File fileFold = new File(localFolder);
			if (!fileFold.exists()) {
				fileFold.mkdirs();
			}
			// 创建文件
			File file = new File(localFolder, localFileName);
			fos = new FileOutputStream(file);

			// 下载文件
			if (!ftp.retrieveFile(ftpFileName, fos)) {
				fos.close();
				file.delete();
				return false;
			}
			ftp.logout();
			logger.info(" recv file -------- " + localFolder + "/" + localFileName);

			rtnFlag = true;
		} catch (Exception e) {
			throw new RuntimeException("与FTP服务器通讯出错：" + e.getMessage());
		} finally {
			MyTool.IO.close(fos);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					throw new RuntimeException("关闭FTP连接发生异常");
				}
			}
		}
		return rtnFlag;
	}

	/**
	 * 上传文件至FTP服务器
	 * 
	 * @param server
	 *            FTP服务器IP
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            登录用户名
	 * @param password
	 *            密码
	 * @param ftpFolder
	 *            FTP服务器上传文件夹路径
	 * @param localFolder
	 *            本地存储文件夹路径
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public boolean sendFile(String server, int port, String username,
			String password, String ftpFolder, String localFolder,
			String fileName) {
		boolean rtnFlag = false;
		FTPClient ftp = new FTPClient();
		FileInputStream fis = null;

		try {
			logger.info(" connecting FTP server " + server + " : " + port + " ...");
			// 连接FTP服务器
			ftp.connect(server, port);
			logger.info(" connected -------- " + server + " : " + port);

			// 登陆FTP服务器
			if (!ftp.login(username, password)) {
				throw new RuntimeException("登录FTP服务器" + server + "失败");
			}
			logger.info(" user login ------- " + username + " : " + password);

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			File srcFile = new File(localFolder, fileName);
			fis = new FileInputStream(srcFile);

			// 设置上传目录
			if (null != ftpFolder && 0 < ftpFolder.trim().length()) {
				// 如果不存在目录，则创建目录
				if (!ftp.changeWorkingDirectory(ftpFolder)) {
					ftp.makeDirectory(ftpFolder);
				}
				ftp.changeWorkingDirectory(ftpFolder);

			} else {
				throw new RuntimeException("FTP服务器上传文件夹不能为空");
			}

			// 上传文件
			if (!ftp.storeFile(fileName, fis)) {
				return false;
			}
			ftp.logout();
			logger.info(" store file ------- " + ftpFolder + "/" + fileName);

			rtnFlag = true;
		} catch (Exception e) {
			throw new RuntimeException("与FTP服务器通讯出错：" + e.getMessage());
		} finally {
			MyTool.IO.close(fis);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					throw new RuntimeException("关闭FTP连接发生异常");
				}
			}
		}
		return rtnFlag;
	}

	/**
	 * 上传文件至FTP服务器
	 * 
	 * @param server
	 *            FTP服务器IP
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            登录用户名
	 * @param password
	 *            密码
	 * @param ftpFolder
	 *            FTP服务器上传文件夹路径
	 * @param fullPathOfLocalFile
	 *            本地存储文件全路径
	 * @param ftpFileName
	 *            上传至FTP文件保存文件名
	 * @return
	 * @throws Exception
	 */
	public boolean sendFile1(String server, int port, String username,
			String password, String ftpFolder, String fullPathOfLocalFile,
			String ftpFileName) {
		boolean rtnFlag = false;
		FTPClient ftp = new FTPClient();
		FileInputStream fis = null;

		try {
			logger.info(" connecting FTP server " + server + " : " + port + " ...");
			// 连接FTP服务器
			ftp.connect(server, port);
			logger.info(" connected -------- " + server + " : " + port);

			// 登陆FTP服务器
			if (!ftp.login(username, password)) {
				throw new RuntimeException("登录FTP服务器" + server + "失败");
			}
			logger.info(" user login ------- " + username + " : " + password);

			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			fis = new FileInputStream(fullPathOfLocalFile);

			// 设置上传目录
			if (null != ftpFolder && 0 < ftpFolder.trim().length()) {
				// 如果不存在目录，则创建目录
				if (!ftp.changeWorkingDirectory(ftpFolder)) {
					ftp.makeDirectory(ftpFolder);
				}
				ftp.changeWorkingDirectory(ftpFolder);

			} else {
				throw new RuntimeException("FTP服务器上传文件夹不能为空");
			}

			// 上传文件
			if (!ftp.storeFile(ftpFileName, fis)) {
				return false;
			}
			ftp.logout();
			logger.info(" store file ------- " + ftpFolder + "/" + ftpFileName);

			rtnFlag = true;
		} catch (Exception e) {
			throw new RuntimeException("与FTP服务器通讯出错：" + e.getMessage());
		} finally {
			MyTool.IO.close(fis);
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
					throw new RuntimeException("关闭FTP连接发生异常");
				}
			}
		}
		return rtnFlag;
	}
}

package com.yang.common.util.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2Utils;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.util.MyTool;


/**
 * 文件工具类
 * @author HP
 *
 */
public class FileUtils {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private FileUtils(){
	}
	
	private static FileUtils file = null;
	
	/**
	 * 获取唯一实例
	 * @return
	 */
	public static FileUtils getSingleInstance() {
		if(null == file){
			synchronized(FileUtils.class){
				if(null == file){
					file = new FileUtils();
				}
			}
		}
		return file;
	}
	
	/**
	 * 文件下载
	 * @param file
	 * @param fileName（含扩展名）  空则默认为file.getName()
	 * @param response
	 * @author yanglei
	 * 2017年7月14日 上午11:07:29
	 */
	public void downLoadFile(File file, String fileName, HttpServletResponse response){
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			if(!file.exists()){
				logger.info("file not found : {}", file.getAbsolutePath());
				throw new FileNotFoundException("file not found : " + file.getAbsolutePath());
			}
			if(StringUtils.isBlank(fileName)){
				fileName = file.getName();
			}
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"), "iso-8859-1"));
			
			bis = new BufferedInputStream(new FileInputStream(file));
			os = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int n;
			while((n = bis.read(buffer)) != -1){
				os.write(buffer, 0, n);
			}
			os.flush();
		} catch (IOException e) {
			logger.error("文件下载异常", e);
		}finally{
			MyTool.IO.close(os);
			MyTool.IO.close(bis);
		}
	}
	
	/**
	 * 将文件（文件夹）中符合条件的文件压缩为目标文件
	 * @param file 原始文件或文件夹
	 * @param target 压缩后的文件
	 * @param filter 文件名过滤器
	 */
	public void compress(String file, String target, FilenameFilter filter){
		ArchiveOutputStream aos = null;
		FileOutputStream fos = null;
		try {
			File f = new File(file);
			checkFile(f);
			
			String suffix = FilenameUtils.getExtension(target);
			ArchiveStreamFactory asf = new ArchiveStreamFactory();
			fos = new FileOutputStream(target);
			try {
				aos = asf.createArchiveOutputStream(suffix,fos);
			} catch (ArchiveException e) {
				aos = asf.createArchiveOutputStream(ArchiveStreamFactory.ZIP,fos);
			}
			
			File ar = new File(target);
			mkdirs(ar.getParentFile());
			appendFileToArchive(aos, null, f, filter);
			aos.finish();
			logger.debug("成功将 " + file + " 压缩成 " + target + " 文件");
		} catch (Exception e) {
			throw new RuntimeException("将 " + file + " 压缩为" + target + " 文件异常", e);
		} finally{
			MyTool.IO.close(aos);
			MyTool.IO.close(fos);
		}
	}
	
	/**
	 * 将文件（文件夹）中符合条件的文件压缩为目标文件
	 * @param file 原始文件或文件夹
	 * @param target 压缩后的文件
	 */
	public void compress(String file, String target){
		compress(file, target, null);
	}
	
	/**
	 * 将一组文件名压缩为目标文件
	 * @param names
	 * @param target
	 * @param delete
	 */
	public void compress(List<String> names, String target, boolean delete){
		ArchiveOutputStream aos = null;
		FileOutputStream fos = null;
		try {
			String suffix = FilenameUtils.getExtension(target);
			ArchiveStreamFactory asf = new ArchiveStreamFactory();
			fos = new FileOutputStream(target);
			try {
				aos = asf.createArchiveOutputStream(suffix,fos);
			} catch (ArchiveException e) {
				aos = asf.createArchiveOutputStream(ArchiveStreamFactory.ZIP,fos);
			}
			
			File ar = new File(target);
			mkdirs(ar.getParentFile());
			for(String name : names){
				appendFileToArchive(aos, null, new File(name), null);
				if(delete){
					deleteFile(name);
				}
			}
			aos.finish();
		} catch (Exception e) {
			throw new RuntimeException("压缩为" + target + " 文件异常", e);
		} finally{
			MyTool.IO.close(aos);
			MyTool.IO.close(fos);
		}
	}
	
	/**
	 * 压缩成gz文件
	 * @param file		源文件
	 * @param folder	压缩文件存放路径
	 * @return 压缩后的文件名
	 */
	public String compress2Gzip(String file, String folder){
		String compress = folder + "/" + GzipUtils.getCompressedFilename(new File(file).getName());
		compressors(file, compress);
		return compress;
	}
	
	/**
	 * 压缩成bzip2文件
	 * @param file		源文件
	 * @param folder	压缩文件存放路径
	 * @return 压缩后的文件名
	 */
	public String compress2Bzip2(String file, String folder){
		//String compress = folder + "/" + BZip2Utils.getCompressedFilename(new File(file).getName());
		String compress = folder + "/" + new File(file).getName() + ".bzip2";
		compressors(file, compress);
		return compress;
	}
	
	/**
	 * 压缩成xz文件
	 * @param file
	 * @param folder
	 * @return 压缩后的文件名
	 */
	public String compress2XZ(String file, String folder){
		String compress = folder + "/" + XZUtils.getCompressedFilename(new File(file).getName());
		compressors(file, compress);//该功能需要导入相应的jar包至类路径下
		return compress;
	}
	
	/**
	 * 压缩成pack200文件
	 * @param file
	 * @param folder
	 * @return 压缩后的文件名
	 */
	public String compress2Pack200(String file, String folder){
		//暂未实现
		return null;
	}
	
	/**
	 * 将压缩文件解压至文件夹folder下
	 * @param compress 压缩文件
	 * @param folder 解压文件目录
	 */
	public void uncompress(String compress, String folder){
		checkFile(new File(compress));	//检查文件是否存在以及是否可读
		mkdirs(new File(folder));		//创建解压后的文件存放目录（如果需要创建）
		
		String suffix = FilenameUtils.getExtension(compress);//压缩文件后缀
		if(CompressorStreamFactory.GZIP.equalsIgnoreCase(suffix))
		{
			String filename = GzipUtils.getUncompressedFilename(getLastFilename(compress));
			uncompress(compress, folder, filename, suffix);
		}else if(CompressorStreamFactory.BZIP2.equalsIgnoreCase(suffix)){
			String filename = BZip2Utils.getUncompressedFilename(getLastFilename(compress));
			uncompress(compress, folder, filename, suffix);
		}else if(CompressorStreamFactory.XZ.equalsIgnoreCase(suffix)){
			String filename = XZUtils.getUncompressedFilename(getLastFilename(compress));
			uncompress(compress, folder, filename, suffix);
		}else if(CompressorStreamFactory.PACK200.equalsIgnoreCase(suffix)){
			//暂未实现
		}else{
			uncompress(compress, folder, suffix);	
		}
	}
	
	/**
	 * 检验文件
	 * @param file
	 */
	public boolean checkFile(File file){
		if(!file.exists()){
			throw new RuntimeException("文件 " + file.getPath() + " 不存在 ");
		}else if(!file.canRead()){
			throw new RuntimeException("文件 " + file.getPath() + " 无法访问 ");
		}
		return true;
	}
	
	/**
	 * 创建目录
	 * @param folder
	 */
	public void mkdirs(File folder){
		if(null != folder && !folder.exists()){
			folder.mkdirs();
		}
	}
	
	/**
	 * 获取文件名的最后一段
	 * <pre>
	 * 	C:/abc/sz/abc.sz 			-->abc.sz
	 *  //server/d/abc.sz.tar.gz 	-->abc.sz.tar.gz
	 *  abc							-->abc
	 *  C:/abd/						-->""
	 * </pre>
	 * @param filename
	 * @return
	 */
	public String getLastFilename(String filename){
		if(null == filename){
			return null;
		}
		if(filename.endsWith("/") || filename.endsWith("\\")){
			return "";
		}
		int index = filename.lastIndexOf('/');
		if(-1 == index){
			index = filename.lastIndexOf('\\');
		}
		return -1 == index ? filename : filename.substring(index + 1);
	}
	
	/**
	 * 删除文件
	 * @param filename
	 */
	public void deleteFile(String filename){
		if(null != filename){
			new File(filename).delete();
		}
	}
	
	/**
	 * 删除文件
	 * @param file
	 */
	public void deleteFile(File file){
		if(null != file){
			file.delete();	
		}
	}
	
	/**
	 *=======================================以下为私有方法==================================================== 
	 */
	/**
	 * 向压缩文件流中添加文件（夹）
	 * @param aos
	 * @param root 创建压缩文件时的根目录
	 * @param file
	 * @param filter 文件名过滤器（不过滤文件夹）
	 */
	private void appendFileToArchive(ArchiveOutputStream aos, String root, File file, FilenameFilter filter){
		InputStream is = null;
		try {
			String entryName = file.getName();
			if(null != root && !"".equals(root)){
				entryName = root + "/" + entryName;
			} 
			if(file.isDirectory()){
				ArchiveEntry entry = aos.createArchiveEntry(file, entryName);
				File[] children = file.listFiles();
				for(int i=0,length=children.length; i < length; i++){
					appendFileToArchive(aos, entryName, children[i], filter);
				}
				if(entry.getSize() > 0){
					aos.putArchiveEntry(entry);
					aos.closeArchiveEntry();
				}
			}else if(file.isFile()){
				if(null == filter || null != filter && filter.accept(file.getParentFile(), file.getName())){
					ArchiveEntry entry = aos.createArchiveEntry(file, entryName);
					aos.putArchiveEntry(entry);
					is = new BufferedInputStream(new FileInputStream(file));
					MyTool.IO.copy(is, aos);
					aos.closeArchiveEntry();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("向压缩文件中添加文件（夹）异常", e);
		} finally{
			MyTool.IO.close(is);
		}
	}
	
	/**
	 * 压缩文件
	 * @param file
	 * @param compress
	 */
	private void compressors(String file, String compress){
		CompressorOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			File input = new File(file);
			checkFile(input);
			
			File compressFile = new File(compress);
			mkdirs(compressFile.getParentFile());
			
			String suffix = FilenameUtils.getExtension(compress);
			bis = new BufferedInputStream(new FileInputStream(input));
			bos = new CompressorStreamFactory().createCompressorOutputStream(suffix, new FileOutputStream(compressFile));
			
			MyTool.IO.copy(bis, bos);
			logger.debug("成功将 " + file + " 压缩成 " + compress + " 文件");
		} catch (Exception e) {
			throw new RuntimeException("将 " + file + " 压缩成 " + compress + " 文件时出现异常", e);
		} finally{
			MyTool.IO.close(bis);
			MyTool.IO.close(bos);
		}
	}
	
	/**
	 * 解压文件
	 * @param compress	压缩文件
	 * @param folder	解压后的文件存放路径	
	 * @param suffix	压缩文件后缀
	 */
	private void uncompress(String compress, String folder, String suffix){
		ArchiveInputStream ais = null;
		FileInputStream fis = null;
		try {
			ArchiveStreamFactory asf = new ArchiveStreamFactory();
			fis = new FileInputStream(compress);
			try{
				ais = asf.createArchiveInputStream(suffix, fis);
			}catch(ArchiveException e){
				ais = asf.createArchiveInputStream(ArchiveStreamFactory.ZIP, fis);
			}
			
			FileOutputStream output = null;
			ArchiveEntry entry = null;
			while(null != (entry = ais.getNextEntry())){
				try{
					File entryFile = new File(folder + "/" + entry.getName());
					if(entry.isDirectory()){
						if(!entryFile.exists()){
							entryFile.mkdirs();
						}
					}else{
						output = new FileOutputStream(entryFile);
						int n = -1;
						while(ais.canReadEntryData(entry) && -1 != (n = ais.read()))
						{
							output.write(n);
						}
					}
				}finally{
					MyTool.IO.close(output);
				}
			}
			logger.debug("成功将 " + compress + " 解压至 " + folder);
		} catch (Exception e) {
			throw new RuntimeException("解压 "+compress+" 文件至 "+folder+" 时出现异常", e);
		} finally{
			MyTool.IO.close(ais);
			MyTool.IO.close(fis);
		}
	}
	
	/**
	 * 解压文件
	 * @param compress	压缩文件
	 * @param folder	解压后的文件存放路径
	 * @param filename	解压后的文件名
	 * @param suffix	压缩文件后缀
	 */
	private void uncompress(String compress, String folder, String filename, String suffix){
		CompressorInputStream cis = null;
		FileOutputStream bos = null;
		try {
			cis = new CompressorStreamFactory().createCompressorInputStream(suffix, new FileInputStream(compress));
			bos = new FileOutputStream(new File(folder + "/" + filename));
			MyTool.IO.copy(cis, bos);
			logger.debug("成功将 " + compress + " 解压至 " + folder);
		} catch (Exception e) {
			throw new RuntimeException("解压 "+compress+" 文件至 "+folder+" 时出现异常", e);
		} finally{
			MyTool.IO.close(bos);
			MyTool.IO.close(cis);
		}
	}
}

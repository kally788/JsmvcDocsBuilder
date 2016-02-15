package com.jsmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**   
 * @title: 文件管理工具
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:59:27 
 *
 */
public class FileUnit {
	
	/**
	 * 深度拷贝整个文件夹包括子文件，对已经存在的文件会被覆盖
	 * @param inStream 要拷贝的文件
	 * @param outStream 输出的文件
	 * @throws Exception 
	 */
	private int failCount = 0;
	public void copyFiles(String inPath, String outPath) throws Exception {
		if(failCount>50){
			failCount = 0;
			throw new Exception("Mkdir directory failCount > 10. path:" + outPath);
		}
		File sourceDir = new File(inPath);
		if(!sourceDir.exists() || sourceDir.isFile()){
			return;
		}
		File targetDir = new File(outPath);
		if(!targetDir.exists() || targetDir.isFile()){
			if(!targetDir.mkdir()){
				failCount++;
				Thread.sleep(10);
				copyFiles(inPath, outPath);
				return;
			}
		}
		failCount = 0;
		File[] list = sourceDir.listFiles();
		for (int i = 0; i < list.length; i++) {
			File node = list[i];
			if (node.isDirectory()) {
		    	copyFiles(node.getPath(), targetDir.getPath()+"/"+node.getName());
			}else{
			    FileUnit.copyFile(node.getPath(), targetDir.getPath()+"/"+node.getName(), false);
			}
		}
	}
	
	/**
	 * 写入字符串到文件，加锁
	 * @param inString 要写入的字符串
	 * @param outStream 输出文件
	 * @param append 是否追加到已有文件结尾，false时会替换掉已存在的
	 * @throws Exception 
	 */
	private int lockCount = 0;
	public void writeStringToFileLock(String inString, String outPath, boolean append) throws Exception { 
		if(lockCount>10){
			lockCount = 0;
			throw new Exception("Get file lock timeout. path:" + outPath);
		}
		FileOutputStream outStream = new FileOutputStream(outPath, append);
		FileLock lock = outStream.getChannel().tryLock();
		if(lock != null){
			lockCount = 0;
			outStream.write(inString.getBytes(StandardCharsets.UTF_8));
			lock.release();
			outStream.close();
		}else{
			lockCount++;
			outStream.close();
			Thread.sleep(1000);
			writeStringToFileLock(inString, outPath, append);
		}
	}
	
	/**
	 * 读取文件到一个字符串，不保留换行
	 * @param path 要读取的文件路径
	 * @return String
	 * @throws Exception 
	 */
	public static String getFileToString(String path) throws Exception{
		String file = "";
		List<String> ls = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		for(int i=0;i<ls.size();i++){
			file += ls.get(i);
		}
		return file;
	}
	
	/**
	 * 读取文件到一个字符串，保留换行信息
	 * @param path 要读取的文件路径
	 * @return String
	 * @throws Exception 
	 */
	public static String getFileToStringOriginal(String path) throws Exception {
		String file = "";
		List<String> ls = Files.readAllLines(Paths.get(path),
				StandardCharsets.UTF_8);
		for (int i = 0; i < ls.size(); i++) {
			if(file.equals("")){
				file = ls.get(i);
			}else{
				file += "\n"+ls.get(i);
			}
		}
		return file;
	}
	
	/**
	 * 写入字符串到文件
	 * @param inString 要写入的字符串
	 * @param outStream 输出文件
	 * @param append 是否追加到已有文件结尾，false时会替换掉已存在的
	 * @throws Exception 
	 */
	public static void writeStringToFile(String inString, String outPath, boolean append) throws Exception { 
		FileOutputStream outStream = new FileOutputStream(outPath, append);
		outStream.write(inString.getBytes(StandardCharsets.UTF_8));
		outStream.close();
	}
	
	/**
	 * 一般拷贝
	 * @param inStream 要拷贝的文件
	 * @param outStream 输出的文件
	 * @param append 是否追加到已有文件结尾，false时会替换掉已存在的
	 * @throws Exception 
	 */
	public static void copyFile(String inPath, String outPath, boolean append) throws Exception { 
		InputStream inStream = new FileInputStream(inPath);
		FileOutputStream outStream = new FileOutputStream(outPath, append);
		int byteread = 0;
		byte[] buffer = new byte[1444]; 
		while ( (byteread = inStream.read(buffer)) != -1) { 
			outStream.write(buffer, 0, byteread); 
		}
		inStream.close();
		outStream.close();
	}
	
	/**
	 * 压缩拷贝，对于要拷贝的文件进行jsmin压缩
	 * @param inStream 要拷贝的文件
	 * @param outStream 输出的文件
	 * @throws Exception 
	 */
	public static void copyJSMinFile(String inPath, String outPath, boolean append) throws Exception { 
		InputStream inStream = new FileInputStream(inPath);
		FileOutputStream outStream = new FileOutputStream(outPath, append);
		new JSMin(inStream, outStream).jsmin();
		inStream.close();
		outStream.close();
	}
	
	/**
	 * 删除目录，包括子目录和它自己
	 * @param file 要删除的文件路径
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if(!file.exists()){  
			return;
		}
		if (file.isDirectory()) {
			File[] ff = file.listFiles();
			for (int i = 0; i < ff.length; i++) {
				deleteFile(ff[i].getPath());
			}
		}
		file.delete();
	}
	
	/**
	 * 清理空目录
	 */
	public static void clearDir(String path){
		File file = new File(path);
		if(!file.exists() || !file.isDirectory()){  
			return;
		}
		File[] ff = file.listFiles();
		if(ff.length == 0){
			file.delete();
		}else{
			for (int i = 0; i < ff.length; i++) {
				clearDir(ff[i].getPath());
			}
		}
	}

	/**
	 * 创建目录，如果目录已经存在，原目录会被清理
	 * @param path 要创建的目录路径
	 */
    public static void createDir(String path){
    	File file = new File(path);
    	if(file.exists()){     
    		deleteFile(file.getPath());
		}
		file.mkdir(); 
    }
}

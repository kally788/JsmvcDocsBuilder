package com.jsmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**   
 * @title: ZIP解压缩文件的工具类，文件可以是多级目录的结构 （使用JDK的ZipEntry存在文件名中文乱码问题，用apache-tools的则不会）
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午1:05:48 
 *
 */
public class Zip{
	/**
	 * 压缩文件操作
	 * @param filePath 要压缩的文件路径
	 * @param descDir 压缩文件保存的路径
	 */
	public static void zipFiles(String filePath, String descDir){
		ZipOutputStream zos = null;
		try{
			// 创建一个Zip输出流
			zos = new ZipOutputStream(new FileOutputStream(descDir));
			// 启动压缩
			startZip(zos, "", filePath);
		}catch (IOException e){
			// 压缩失败，则删除创建的文件
			File zipFile = new File(descDir);
			if (zipFile.exists()){
				zipFile.delete();
			}
			e.printStackTrace();
		}finally{
			try{
				if (zos != null){
					zos.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对目录中所有文件递归遍历进行压缩
	 * @param zos ZIP压缩输出流
	 * @param oppositePath 在zip文件中的相对路径
	 * @param directory 要压缩的文件的路径
	 * @throws IOException
	 */
	private static void startZip(ZipOutputStream zos, String oppositePath, String directory) throws IOException{
		File file = new File(directory);
		if (file.isDirectory())
		{
			// 如果是压缩目录
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++){
				File aFile = files[i];
				if (aFile.isDirectory()){
					// 如果是目录，修改相对地址
					String newOppositePath = oppositePath + aFile.getName() + "/";
					// 压缩目录，这是关键，创建一个目录的条目时，需要在目录名后面加多一个"/"
					ZipEntry entry = new ZipEntry(oppositePath + aFile.getName() + "/");
					zos.putNextEntry(entry);
					zos.closeEntry();
					// 进行递归调用
					startZip(zos, newOppositePath, aFile.getPath());
				}else{
					// 如果不是目录，则进行压缩
					zipFile(zos, oppositePath, aFile);
				}
			}
		}else{
			// 如果是压缩文件，直接调用压缩方法进行压缩
			zipFile(zos, oppositePath, file);
		}
	}

	/**
	 * 压缩单个文件到目录中
	 * @param zos zip输出流
	 * @param oppositePath 在zip文件中的相对路径
	 * @param file 要压缩的的文件
	 */
	private static void zipFile(ZipOutputStream zos, String oppositePath, File file){
		// 创建一个Zip条目，每个Zip条目都是必须相对于根路径
		InputStream is = null;
		try{
			ZipEntry entry = new ZipEntry(oppositePath + file.getName());
			// 将条目保存到Zip压缩文件当中
			zos.putNextEntry(entry);
			// 从文件输入流当中读取数据，并将数据写到输出流当中.
			is = new FileInputStream(file);
			int length = 0;
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			while ((length = is.read(buffer, 0, bufferSize)) >= 0){
				zos.write(buffer, 0, length);
			}
			zos.closeEntry();
		}catch (IOException ex){
			ex.printStackTrace();
		}finally{
			try{
				if (is != null){
					is.close();
				}
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}


	/**
	 * 解压文件操作
	 * @param zipFilePath zip文件路径
	 * @param descDir 解压出来的文件保存的目录
	 * @throws IOException
	 */
	public static void unZipFiles(String zipFilePath, String descDir){
		File zipFile = new File(zipFilePath);
		File pathFile = new File(descDir);
		if (!pathFile.exists()){
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		InputStream in = null;
		OutputStream out = null;
		try{
			zip = new ZipFile(zipFile);
			for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();){
				ZipEntry entry = entries.nextElement();
				String zipEntryName = entry.getName();
				in = zip.getInputStream(entry);
				String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");;
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0,outPath.lastIndexOf('/')));
				if (!file.exists()){
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经创建,不需要解压
				if (new File(outPath).isDirectory()){
					continue;
				}
				out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[4 * 1024];
				int len;
				while ((len = in.read(buf1)) > 0){
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
		}catch (IOException e){
			pathFile.delete();
			e.printStackTrace();
		}finally{
			try{
				if (zip != null){
					zip.close();
				}
				if (in != null){
					in.close();
				}
				if (out != null){
					out.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
}

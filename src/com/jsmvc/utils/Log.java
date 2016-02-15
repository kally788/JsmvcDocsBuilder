package com.jsmvc.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Log {
	private String logList = "";
	private String logDir = "./log/";//日志目录
	private int logFileMax = 2048000;//字节
	private int logLevel = 0;//<=0信息+警告+错误，=1警告+错误，=2错误，>=3关闭日志
	
	public void info(String v){
		String line = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " # [info]    # " + v;
		System.out.println(line);
		if(logLevel < 1){
			logList += line + System.getProperty("line.separator");
		}
	}
	
	public void warning(String v){
		String line = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " # [warning] # " + v;
		System.out.println(line);
		if(logLevel < 2){
			logList += line + System.getProperty("line.separator");
		}
	}
	
	public void error(String v){
		String line = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + " # [error]   # " + v;
		System.out.println(line);
		if(logLevel < 3){
			logList += line + System.getProperty("line.separator");
		}
	}
	
	public void cleanSeparator(){
		if(logList.length()>=System.getProperty("line.separator").length()){
			logList = logList.substring(0, logList.lastIndexOf(System.getProperty("line.separator")));
		}
	}
	
	public void save(boolean isError) throws Exception{
		if(logDir.equals("") || logList.equals("")){
			return;
		}
		File logDirectory = new File(logDir);
		if(!logDirectory.exists()||!logDirectory.isDirectory()){
			logDirectory.mkdirs();
		}
		String logPath = logDir+(isError?"error0.log":"complete0.log");
		File[] files = logDirectory.listFiles();
		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			if(f.isFile() && f.getName().indexOf(isError?"error":"complete") == 0){
				fileList.add(f);
			}
		}
		Collections.sort(fileList, new Comparator<File>() {
		    @Override
		    public int compare(File o1, File o2) {
		        return o2.getName().compareTo(o1.getName());
		    }
		});
		if(fileList.size()>0){
			if(fileList.get(0).length() >= logFileMax){
				logPath = logDir+(isError?"error":"complete")+fileList.size()+".log";
			}else{
				logPath = fileList.get(0).getPath();
			}
		}
		new FileUnit().writeStringToFileLock(logList+System.getProperty("line.separator"), logPath, true);
	}
	
	public Log(String dir, int max, int level){
		logDir = dir;
		logFileMax = max;
		logLevel = level;
	}
	
	public Log(){
		
	}
}

package com.jsmvc.docsbuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.jsmvc.utils.Md5;
import com.jsmvc.utils.StringUnit;

/**   
 * @title: 目录配置，优先读取 conf.txt 文件中的配置
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午12:41:50 
 *
 */
public class Config extends com.jsmvc.packproject.Config{
	
	//要读取注释的源码目录
	private String sourceDir = "tpl/default/$jsmvc$/";
	//文档名称
	private String docsName = "JsMvc API";
	//项目版本
	private String projVer = "";
	
	//模版目录
	private String templateDir = "./tpl/default/";
	//菜单数据模版文件，相对模版根目录
	private String templateM = "js/model/MenuList.js";
	//内容数据模版文件，相对模版根目录
	private String templateC = "js/model/ContList.js";
		
	//临时目录，打包时会在打包程序根目录下创建该目录
	private String tmpDocsDir = Md5.getMD5(java.util.UUID.randomUUID().toString())+"/";
	
	//确保配置项的正确性
	private void correctConfig(){
		sourceDir = StringUnit.addBar(sourceDir);
		templateDir =StringUnit.addBar(templateDir);
	}
	//读取配置文件
	private void readConfig(String configPath) throws IOException{
		List<String> conf = Files.readAllLines(Paths.get(configPath), StandardCharsets.UTF_8);
		for(int i=0;i<conf.size();i++){
			String line = conf.get(i);
			if(line.length() < 1){
				continue;
			}
			if(line.replaceAll("\\s*", "").substring(0,1).equals("#")){
				continue;
			}
			String[] lineItem = line.split("=");
			if(lineItem.length < 2){
				continue;
			}
			String v = line.substring(lineItem[0].length()+1,line.length()).trim();
			if(v.equals("") && !lineItem[0].trim().equals("projVer")){
				continue;
			}
			switch(lineItem[0].trim()){
			case "sourceDir":
				sourceDir = v;
				break;
			case "templateDir":
				templateDir = v;
				break;
			case "templateM":
				templateM = v;
				break;
			case "templateC":
				templateC = v;
				break;
			case "docsName":
				docsName = v;
				break;
			case "projVer":
				projVer = v;
				break;
			}
		}
	}

	/**
	 * 创建一个配置文件
	 * @param configPath 指定配置文件路径
	 * @throws IOException
	 */
	public Config(String configPath) throws IOException{
		File configFile = new File(configPath);
		if(!configFile.exists()||!configFile.isFile()){
			throw new IOException("Read custom config error:" + configPath);
		}else{
			readConfig(configPath);
			correctConfig();
		}
	}
	
	/**
	 * 创建一个配置文件，如果默认根目录下有config.conf文件，即读取，否则采用程序默认值
	 * @throws IOException
	 */
	public Config() throws IOException{
		File configFile = new File("config.conf");
		if(configFile.exists() && configFile.isFile()){
			readConfig("config.conf");
		}
		correctConfig();
	}

	//可写属性
	
	public void setSourceDir(String v){
		sourceDir = StringUnit.addBar(v);
	}
	
	public void setDocsName(String v){
		if(!v.equals("")){
			docsName = v;
		}
	}
	
	public void setProjVer(String v){
		projVer = v;
	}

	//可读属性
		
	public String getSourceDir() {
		return sourceDir;
	}
	
	public String getDocsName() {
		return docsName;
	}
	
	public String getProjVer() {
		return projVer;
	}
	
	public String getTemplate() {
		return templateDir;
	}

	public String getTemplateM() {
		return templateM;
	}

	public String getTemplateC() {
		return templateC;
	}
	
	public String getTmpDocsDir() {
		return tmpDocsDir;
	}
}

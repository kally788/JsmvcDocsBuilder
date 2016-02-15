package com.jsmvc.docsbuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.jsmvc.packproject.PackProject;
import com.jsmvc.utils.FileUnit;
import com.jsmvc.utils.Log;

public class DocsBuilder {
	private Config config;
	private Log log;
	
	private void startup(){
		try{
			log.info("Extract notes ...");
			CreateNotes cn = new CreateNotes(log);
			cn.startup(config.getSourceDir(), "");
			log.info("  Total file count: "+cn.totalCount);
			log.info("  Exist notes file count: "+cn.completeCount);
			
			log.info("Create directory.");
			FileUnit.createDir(config.getTmpDocsDir());
			log.info("Create file.");
			new FileUnit().copyFiles(config.getTemplate(), config.getTmpDocsDir());

			log.info("Create data.");
			String cont = FileUnit.getFileToStringOriginal(config.getTmpDocsDir()+config.getTemplateC());
			String menu = FileUnit.getFileToStringOriginal(config.getTmpDocsDir()+config.getTemplateM());
			String index = FileUnit.getFileToStringOriginal(config.getTmpDocsDir()+"index.html");
			cont = cont.replaceAll("\\/\\*\\@cont\\{\\*\\/([\\s\\S]*?)\\/\\*\\}cont\\@\\*\\/", "/*@cont{*/\n"+java.util.regex.Matcher.quoteReplacement(cn.contJson.toJSONString())+"\n/*}cont@*/");
			menu = menu.replaceAll("\\/\\*\\@cont\\{\\*\\/([\\s\\S]*?)\\/\\*\\}cont\\@\\*\\/", "/*@cont{*/\n"+java.util.regex.Matcher.quoteReplacement(cn.menuJson.toJSONString())+"\n/*}cont@*/");
			menu = menu.replaceAll("var docName = \\{v:\"(.*)\"\\}", "var docName = {v:\""+config.getDocsName()+"\"}");
			menu = menu.replaceAll("var docVersion = \\{v:\"(.*)\"\\}", "var docVersion = {v:\""+config.getProjVer()+"\"}");
			index = index.replaceAll("<title>(.*)<\\/title>", "<title>"+config.getDocsName()+"</title>");
			
			log.info("Create docs.");
			FileUnit.writeStringToFile(cont,config.getTmpDocsDir()+config.getTemplateC(),false);
			FileUnit.writeStringToFile(menu,config.getTmpDocsDir()+config.getTemplateM(),false);
			FileUnit.writeStringToFile(index,config.getTmpDocsDir()+"index.html",false);
			
			log.info("Make ing =================================>");
			config.setInputDir(config.getTmpDocsDir());
			PackProject.make(config, log);
		}catch(Exception e){
			System.gc();
			StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        log.error("Fatal error!!!!"); 
        	log.error("Error info:");
        	log.error(sw.toString());
        	try {
        		log.save(true);
			} catch (Exception e1) {
				log.error("Write log fail!");
				e1.printStackTrace();
			}
		}finally{
			FileUnit.deleteFile(config.getTmpDocsDir());
		}
	}
	
	private DocsBuilder(Config conf, Log l){
		config = conf;
		log = l;
		startup();
	}
	
	private DocsBuilder(Config conf){
		config = conf;
		log = new Log(config.getLogDir(), config.getLogMax(), config.getLogLevel());
		startup();
	}
	
	private DocsBuilder() throws IOException{
		config = new Config();
		log = new Log(config.getLogDir(), config.getLogMax(), config.getLogLevel());
		startup();
	}
	
	/**
	 * 开始生成注释文档
	 * @param conf 指定一个自定义的配置文件
	 * @param l 日志对象
	 */
	public static void make(Config conf, Log l){
		new DocsBuilder(conf, l);
	}
	public static void make(Config conf){
		new DocsBuilder(conf);
	}
	public static void make() throws IOException{
		new DocsBuilder();
	}
	
	/**
	 * 第一个参数为文档名称
	 * 第二个参数为项目版本
	 * 第三个参数为项目目录
	 * 第四个参数为输出目录
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		
		Config conf = new Config();
		if(args.length > 0 && !args[0].equals("")){
			conf.setDocsName(args[0]);//文档名称
		}
		if(args.length > 1 && !args[1].equals("")){
			conf.setProjVer(args[1]);//项目版本
			//conf.setVersion(args[1]);//文档版本
		}
		if(args.length > 2 && !args[2].equals("")){
			conf.setSourceDir(args[2]);//项目目录
		}
		if(args.length > 3 && !args[3].equals("")){
			conf.setOutputDir(args[3]);//输出目录
		}
		DocsBuilder.make(conf);
		
	}
}

package com.jsmvc.utils;

public class StringUnit {
	// 清除字符串前面的所有*字符
	public static String trimFrontStar(String source) {
		if (source.length() > 0 && source.substring(0, 1).equals("*")) {
			source = source.substring(1, source.length());
			return trimFrontStar(source);
		}
		return source;
	}

	// 清除字符串第一个空格
	public static String trimOneSpace(String source) {
		if (source.length() > 0 && source.substring(0, 1).equals(" ")) {
			source = source.substring(1, source.length());
		}
		return source;
	}
	
	//剪裁字符串前后的/\
	public static String trimBothBar(String source) {
		boolean isNew = false;
		if(source.length() > 0){
			if (source.substring(0, 1).equals("/") || source.substring(0, 1).equals("\\")) {
				source = source.substring(1, source.length());
				isNew = true;
			}
		}
		if(source.length() > 0){
			int s = source.length()-1;
			int e = source.length();
			if (source.substring(s, e).equals("/") || source.substring(s, e).equals("\\")) {
				source = source.substring(0, s);
				isNew = true;
			}
		}
		if(isNew){
			return trimBothBar(source);
		}else{
			return source;
		}
	}
	
	//字符串后面加上/
	public static String addBar(String source){
		String last = source.substring(source.length()-1,source.length());
		if(!last.equals("/") && !last.equals("\\")){
			source += "/";
		}
		return source;
	}	

	// HTML标签转义
	public static String html(String content) {
		if (content == null) {
			return "";
		}
		String html = content;
		// html = html.replace( "'", "&apos;");
		html = html.replaceAll("&", "&amp;");
		html = html.replace("\"", "&quot;"); // "
		html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
		html = html.replace(" ", "&nbsp;");// 替换空格
		html = html.replace("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		return html;
	}
}

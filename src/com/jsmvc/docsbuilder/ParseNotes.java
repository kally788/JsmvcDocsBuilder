package com.jsmvc.docsbuilder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsmvc.utils.StringUnit;

public class ParseNotes {
	private static String exampleTPL = "{desc:'',code:null}";// 例子
	private static String paramTPL = "{name:'',type:'',desc:''}";// 参数
	private static String retTPL = "{type:'',desc:''}";// 返回
	
	/**
	 * 解释标注为  @example 的注释列表
	 * @param ls 一个或多个例子注释列表 [{desc:'',code:null},...]
	 * @return
	 */
	public static JSONArray example(JSONArray ls) {
		JSONArray list = new JSONArray();
		if (ls.size() > 0) {
			list.add(JSONObject.parse(exampleTPL));
		}
		for (int i = 0; i < ls.size(); i++) {
			String line = (String) ls.get(i);
			String[] start = line.split("<code>");
			String[] end = line.split("</code>");
			if (line.indexOf("</code>") != -1
					&& (end.length == 0 || end[0].replaceAll("[\\*\\s]", "")
							.equals(""))) {
				if (i < ls.size() - 1) {
					list.add(JSONObject.parse(exampleTPL));
				}
			} else {
				JSONObject item = (JSONObject) list.get(list.size() - 1);
				if (line.indexOf("<code>") != -1
						&& (start.length == 0 || start[0].replaceAll(
								"[\\*\\s]", "").equals(""))) {
					item.put("code", "");
				} else if (item.get("code") == null) {
					item.put(
							"desc",
							item.get("desc").equals("") ? StringUnit
									.html(StringUnit.trimOneSpace(StringUnit
											.trimFrontStar(line))) : item.get("desc")
									+ "<br>"
									+ StringUnit.html(StringUnit
											.trimOneSpace(StringUnit.trimFrontStar(line))));
				} else {
					item.put(
							"code",
							item.get("code").equals("") ? StringUnit.trimFrontStar(line)
									: item.get("code") + "\n"
											+ StringUnit.trimFrontStar(line));
				}
			}
		}
		return list;
	}

	/**
	 * 解释标注为  @returns 的注释
	 * @param ls 注释数组列表
	 * @return 参考 retTPL
	 */
	public static JSONObject returns(JSONArray ls) {
		JSONObject param = (JSONObject) JSONObject.parse(retTPL);
		if (ls.size() > 0) {
			String line = ((String) ls.get(0));
			String[] node = line.split(" ");
			String pType = node[0];
			param.put("type", pType);
			if (node.length > 1) {
				param.put("desc", StringUnit.html(line.substring(
						(pType).length() + 1, line.length())));
			}
			for (int i = 1; i < ls.size(); i++) {
				if (param.get("desc").equals("")) {
					param.put("desc", StringUnit.html(StringUnit
							.trimOneSpace(StringUnit.trimFrontStar((String) ls.get(i)))));
				} else {
					param.put(
							"desc",
							param.get("desc")
									+ "<br>"
									+ StringUnit.html(StringUnit
											.trimOneSpace(StringUnit
													.trimFrontStar((String) ls.get(i)))));
				}
			}
		}
		return param;
	}

	
	/**
	 * 解释标注为  @param 的注释
	 * @param ls  注释数组列表
	 * @return 参考paramTPL
	 */
	public static JSONObject param(JSONArray ls) {
		JSONObject param = (JSONObject) JSONObject.parse(paramTPL);
		if (ls.size() > 0) {
			String line = ((String) ls.get(0));
			String[] node1 = line.split(":");
			String pName = node1[0];
			param.put("name", pName);
			if (node1.length > 1) {
				String node2[] = node1[1].split(" ");
				String pType = node2[0];
				param.put("type", pType);
				if (node2.length > 1) {
					param.put("desc", StringUnit.html(line.substring(
							(pName + pType).length() + 2, line.length())));
				}
			}
			for (int i = 1; i < ls.size(); i++) {
				if (param.get("desc").equals("")) {
					param.put("desc", StringUnit.html(StringUnit
							.trimOneSpace(StringUnit.trimFrontStar((String) ls.get(i)))));
				} else {
					param.put(
							"desc",
							param.get("desc")
									+ "<br>"
									+ StringUnit.html(StringUnit
											.trimOneSpace(StringUnit
													.trimFrontStar((String) ls.get(i)))));
				}
			}
		}
		return param;
	}

	// 解析基础项
	
	/**
	 * 解释一般的注释，如：@xxxx xxx，取得空格之后的值
	 * @param ls  注释数组列表
	 * @param isOne 只取一行或者多行，true 为只取一行
	 * @return String
	 */
	public static String base(JSONArray ls, boolean isOne) {
		String str = "";
		if (ls.size() > 0) {
			String line = ((String) ls.get(0));
			if (isOne) {
				return line.split(" ")[0];
			}
			str = StringUnit.html(StringUnit.trimOneSpace(StringUnit.trimFrontStar(line)));
			for (int i = 1; i < ls.size(); i++) {
				if (str.equals("")) {
					str = StringUnit.html(StringUnit.trimOneSpace(StringUnit
							.trimFrontStar((String) ls.get(i))));
				} else {
					str = str
							+ "<br>"
							+ StringUnit.html(StringUnit.trimOneSpace(StringUnit
									.trimFrontStar((String) ls.get(i))));
				}
			}
		}
		return str;
	}
}

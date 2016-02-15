package com.jsmvc.docsbuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ExtractNotes {

	/**
	 * 提取文档中的注释，只会提取 /**开头的注释
	 * @param filePath 要提取的文件路径
	 * @return List<String>
	 * @throws IOException
	 */
	public static List<String> getDocNotes(String filePath) throws IOException {
		List<String> comments = new ArrayList<String>();
		BufferedReader bfr = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), StandardCharsets.UTF_8));
		String line = null;
		while ((line = bfr.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("//")) {
				// comments.add(line);
			} else if (line.startsWith("/*") && line.endsWith("*/")) {
				// comments.add(line);
			} else if (line.startsWith("/**") && !line.endsWith("*/")) {
				StringBuffer multilineComment = new StringBuffer(line);
				while ((line = bfr.readLine()) != null) {
					line = line.trim();
					multilineComment.append("\n").append(line);
					if (line.endsWith("*/")) {
						comments.add(multilineComment.toString());
						break;
					}
				}
			}
		}
		bfr.close();
		return comments;
	}

	/**
	 * 转换提取后的注释列表为数组形式
	 * @param comments 要转换的注释列表
	 * @return JSONArray[{type:注释@后面的内容,value:[注释详细值，一个字符串元素一行]},...]
	 * @throws IOException
	 */
	public static JSONArray transfNotesToArray(List<String> comments) {
		JSONArray list = new JSONArray();
		for (int n = 0; n < comments.size(); n++) {
			JSONArray niceList = new JSONArray();
			String[] notes = comments.get(n).split("\n");
			for (int i = 0; i < notes.length; i++) {
				String line = notes[i];
				String[] node = line.split("@");
				if (node.length > 1
						&& node[0].replaceAll("[\\*\\s]", "").equals("")
						&& node[1].substring(0, 3).matches("[a-zA-Z]+")) {
					JSONObject item = new JSONObject();
					JSONArray value = new JSONArray();
					String[] ls = node[1].split(" ");
					String type = ls[0];
					if (ls.length > 1) {
						value.add(line.substring(
								(node[0] + "@" + type + " ").length(),
								line.length()));
					}
					item.put("type", type);
					item.put("value", value);
					niceList.add(item);
				} else if (niceList.size() > 0 && !line.startsWith("*/")) {
					JSONObject currItem = (JSONObject) niceList.get(niceList
							.size() - 1);
					JSONArray currValue = (JSONArray) currItem.get("value");
					currValue.add(line);
				}
			}
			list.add(niceList);
		}
		return list;
	}
}

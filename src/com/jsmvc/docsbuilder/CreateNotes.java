package com.jsmvc.docsbuilder;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsmvc.utils.Log;

public class CreateNotes {
	private static String docTPL = "{statement:'',desc:'',example:[],exte:'',func:{pub:[],pro:[],sta:[]},evt:{pub:[],pro:[]},attr:{pub:[],pro:[],sta:[]},notice:[]}";
	private static String menuTPL = "{name:'',list:[]}";// 菜单
	private static String funcTPL = "{name:'',desc:'',param:[],ret:null,example:[]}";// 函数和事件
	private static String attrTPL = "{name:'',desc:'',type:''}";// 属性和通知

	private Log log;
	
	// 前端模块能识别的数据结构
	public JSONObject contJson = new JSONObject();
	public JSONArray menuJson = new JSONArray();

	public int totalCount = 0;
	public int completeCount = 0;

	/**
	 * 把注释数组生成为前端模块能识别的 json 数据对象，参考 docTPL
	 * 
	 * @param list
	 *            注释数组
	 * @param docName
	 *            文档名称
	 */
	public boolean createCont(JSONArray list, String docName) {
		if (list.size() < 1) {
			return false;
		}
		JSONObject doc = (JSONObject) JSONObject.parse(docTPL);
		contJson.put(docName, doc);
		boolean isEmpty = true;
		for (int n = 0; n < list.size(); n++) {
			JSONArray niceList = (JSONArray) list.get(n);
			String nodeType = "";
			for (int i = 0; i < niceList.size();) {
				JSONObject item = (JSONObject) niceList.get(i);
				if (item.get("type").equals("type")) {
					nodeType = ParseNotes.base((JSONArray) item.get("value"),
							true);
					niceList.remove(i);
					continue;
				}
				i++;
			}
			if (nodeType.equals("")
					|| (!nodeType.equals("object") && !nodeType.equals("class")
							&& !nodeType.equals("function")
							&& !nodeType.equals("event")
							&& !nodeType.equals("attr") && !nodeType
								.equals("notice"))) {
				continue;
			}
			isEmpty = false;
			if (nodeType.equals("object") || nodeType.equals("class")) {
				doc.put("statement", nodeType);
				for (int i = 0; i < niceList.size(); i++) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("desc")
							|| item.get("type").equals("exte")) {
						doc.put((String) item.get("type"), ParseNotes.base(
								(JSONArray) item.get("value"), false));
					} else if (item.get("type").equals("example")) {
						((JSONArray) doc.get("example")).add(ParseNotes
								.example((JSONArray) item.get("value")));
					}
				}
			} else if (nodeType.equals("function") || nodeType.equals("event")) {
				String modification = "";
				for (int i = 0; i < niceList.size();) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("modification")) {
						modification = ParseNotes.base(
								(JSONArray) item.get("value"), true);
						niceList.remove(i);
						continue;
					}
					i++;
				}
				// 没有声明修饰的默认为公共
				if (nodeType.equals("function")) {
					if (!modification.equals("pub")
							&& !modification.equals("pro")
							&& !modification.equals("sta")) {
						modification = "pub";
					}
				} else {
					if (!modification.equals("pub")
							&& !modification.equals("pro")) {
						modification = "pub";
					}
				}

				JSONObject func = (JSONObject) JSONObject.parse(funcTPL);
				((JSONArray) (((JSONObject) doc
						.get(nodeType.equals("function") ? "func" : "evt"))
						.get(modification))).add(func);
				for (int i = 0; i < niceList.size(); i++) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("name")
							|| item.get("type").equals("desc")) {
						func.put((String) item.get("type"), ParseNotes.base(
								(JSONArray) item.get("value"), false));
					} else if (item.get("type").equals("returns")) {
						func.put("ret", ParseNotes.returns((JSONArray) item
								.get("value")));
					} else if (item.get("type").equals("param")) {
						((JSONArray) func.get("param")).add(ParseNotes
								.param((JSONArray) item.get("value")));
					} else if (item.get("type").equals("example")) {
						((JSONArray) func.get("example")).add(ParseNotes
								.example((JSONArray) item.get("value")));
					}
				}
			} else if (nodeType.equals("attr")) {
				String modification = "";
				for (int i = 0; i < niceList.size();) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("modification")) {
						modification = ParseNotes.base(
								(JSONArray) item.get("value"), true);
						niceList.remove(i);
						continue;
					}
					i++;
				}
				if (!modification.equals("pub") && !modification.equals("pro")
						&& !modification.equals("sta")) {
					modification = "pub";
				}
				JSONObject attr = (JSONObject) JSONObject.parse(attrTPL);
				((JSONArray) (((JSONObject) doc.get("attr")).get(modification)))
						.add(attr);
				for (int i = 0; i < niceList.size(); i++) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("name")
							|| item.get("type").equals("desc")) {
						attr.put((String) item.get("type"), ParseNotes.base(
								(JSONArray) item.get("value"), false));
					} else if (item.get("type").equals("value")) {
						attr.put("type", ParseNotes.base(
								(JSONArray) item.get("value"), false));
					}
				}
			} else if (nodeType.equals("notice")) {
				JSONObject attr = (JSONObject) JSONObject.parse(attrTPL);
				((JSONArray) doc.get("notice")).add(attr);
				for (int i = 0; i < niceList.size(); i++) {
					JSONObject item = (JSONObject) niceList.get(i);
					if (item.get("type").equals("name")
							|| item.get("type").equals("desc")) {
						attr.put((String) item.get("type"), ParseNotes.base(
								(JSONArray) item.get("value"), false));
					} else if (item.get("type").equals("value")) {
						attr.put("type", ParseNotes.base(
								(JSONArray) item.get("value"), false));
					}
				}
			}
		}
		if (isEmpty) {
			contJson.remove(docName);
			return false;
		} else {
			completeCount++;
			return true;
		}
	}

	/**
	 * 创建前端模版的菜单 json 数据对象
	 * 
	 * @param name
	 *            目录名称
	 * @return JSONObject 目录数据对象，参考 menuTPL
	 */
	public JSONObject createMenu(String name) {
		JSONObject menu = null;
		for (int i = 0; i < menuJson.size(); i++) {
			if (((JSONObject) menuJson.get(i)).get("name").equals(name)) {
				menu = (JSONObject) menuJson.get(i);
				break;
			}
		}
		if (menu == null) {
			menu = (JSONObject) JSONObject.parse(menuTPL);
			menu.put("name", name);
			menuJson.add(menu);
		}
		return menu;
	}

	/**
	 * 开始创建注释数据
	 * 
	 * @param path
	 *            要创建注释文档的源文件目录
	 * @param name
	 *            上级目录名称，初始时填 ""即可，该参数的目的是为了递归出 a.b.c这样的目录结构
	 * @throws IOException
	 */
	public void startup(String path, String name) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			return;
		}
		if (dir.isDirectory()) {
			String docName = (name.equals("") ? "" : (name + "."))
					+ dir.getName();
			createMenu(docName);
			File[] list = dir.listFiles();
			for (int i = 0; i < list.length; i++) {
				startup(list[i].getPath(), docName);
			}
		} else {
			String fileName = dir.getName().substring(0,
					dir.getName().lastIndexOf("."));
			if (name.equals("")) {
				createMenu(fileName);
			} else {
				((JSONArray) createMenu(name).get("list")).add(fileName);
			}
			totalCount++;
			if (createCont(ExtractNotes.transfNotesToArray(ExtractNotes
					.getDocNotes(path)), (name.equals("") ? "" : (name + "."))
					+ fileName)) {
				log.info("  Exist notes # file:" + path);
			} else {
				log.warning("  Not notes # file:" + path);
			}
		}
	}
	
	public CreateNotes(Log l){
		log = l;
	}
}

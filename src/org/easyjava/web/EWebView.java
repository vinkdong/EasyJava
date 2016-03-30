package org.easyjava.web;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easyjava.database.DATABASE;
import org.easyjava.database.DB;
import org.easyjava.database.Model;
import org.easyjava.file.Dict;
import org.easyjava.file.EFile;
import org.easyjava.file.EViewType;
import org.easyjava.file.EXml;
import org.easyjava.network.ENetwork;
import org.easyjava.util.EOut;
import org.easyjava.util.EString;
import org.easyjava.util.ETool;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jdk.nashorn.internal.objects.Global;

public class EWebView {
	

	public void initDb(String url){
		String path = new EXml().urlToPath(url);
		List<Map<String, String>> fieldList = EXml.read(path, "field");
		EOut.print(fieldList);
	}
	@Test
	public void test(){
//		Node node= EXml.getNodeById("/Users/Vink/easyjava/WebContent/layout/base.xml","base_layout");
//		ReadByChild(node);
		if (DB.connection == null) {
			System.out.println("正在连接数据库");
			DATABASE.DATABASE_LOCATION = "127.0.0.1";
			DATABASE.DATABASE_NAME = "easyjava";
			DATABASE.DATABASE_PORT = "5432";
			DATABASE.DATABASE_USER = "ej";
			DATABASE.DATABASE_PASSWORD = "admin";
			DATABASE.DATABASE_TYPE="postgresql";
			DB.init();
		}
		EViewType et = getNodeByType("/Users/Vink/easyjava/WebContent/pages/forum.xml", "tree");
		String str = ReadByNode(EXml.getNodeById("base_layout"),et);
		System.out.println(et.getDict());
		
//		System.out.println(node.getDict());
	}
	
	private String parseTag(Node node,EViewType et) {
		NamedNodeMap attrs = node.getAttributes();
		String html = "";
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (attr.getNodeName().equals("load")) {
				Node tNode = EXml.getNodeById(attr.getNodeValue());
				if(tNode.hasChildNodes()){
					html += ReadByNode(tNode,et);
				}
			}
			if (attr.getNodeName().equals("t-if")) {
				//TODO:此处添加判断方法
				//暂时表示条件成立
				if(attr.getNodeValue().equals("type=form")){
					if(node.hasChildNodes()){
						html += ReadByNode(node,et);
					}
				}
				
			}
			if (attr.getNodeName().equals("t-read")) {
				String[] doc = attr.getNodeValue().split("\\.");
				if(doc.length>1&&doc[0].equals("this")){
					if(doc[1].equals("form")){
						html += loadTree(et);
					}
				}
				
				
			}
		}
		return html;
	}
	
	public static String loadTree(EViewType et){
		NodeList nodelist = et.getNode().getChildNodes();
		Dict properties = et.getDict();
		List<Map<String, String>> dataset = Self.env.read(properties);
		StringBuilder tbody = new StringBuilder();
		List<String> head = new ArrayList<>(); 
		boolean init_head = true;
		for(Map<String,String> line :dataset){
			tbody.append("<tr>\n");
			for(int i=0;i<nodelist.getLength();i++){
				Node node = nodelist.item(i);
				//TODO:权限屏蔽
				if(node.getNodeType()==Node.ELEMENT_NODE){
					if(node.getNodeName().equals("field")){
						NamedNodeMap attr = node.getAttributes();
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("name")) {
								tbody.append("\t<td>");
								//TODO:对象翻译，读取String
								if(init_head){
									head.add(attNode.getNodeValue());
								}
								tbody.append(ETool.get(line, attNode.getNodeValue()));
								tbody.append("</td>\n");
							}
						}
					}
					if(node.getNodeName().equals("button")){
						tbody.append("\t<td>");
						NamedNodeMap attr = node.getAttributes();
						tbody.append("\n\t\t<button ");
						String val = "";
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("string")) {								
								//TODO:对象翻译，读取String
								val = attNode.getNodeValue();
								if(init_head){
									head.add(val);
								}
							}
							tbody.append(attNode.getNodeName());
							tbody.append("=\"");
							tbody.append(attNode.getNodeValue());
							tbody.append("\" ");
						}
						tbody.append(">");
						tbody.append(val);
						tbody.append("</button>\n");
						tbody.append("</td>\n");
					}
					
				}
			}
			tbody.append("</tr>\n");
			init_head = false;
		}
		StringBuilder thead = new StringBuilder();
		thead.append("<thead>\n\t<tr>");
		for(String h:head){
			thead.append("<th>");
			thead.append(h);
			thead.append("</th>");
		}
		thead.append("\t</tr>\n</thead>");
		StringBuilder table = new StringBuilder();
		table.append("<table class=\"table table-responsive\">\n");
		table.append(thead);
		table.append(tbody);
		table.append("</table>");
		return table.toString();
	}
	
	public static String loadForm(){
		
		return null;
	}
	
	/**
	 * 递归式读取节点
	 * @param node
	 * @return
	 */
	private String ReadByNode(Node node,EViewType et){
		NodeList nodelist = node.getChildNodes();
		String html = "";
		
		for(int i=0;i<nodelist.getLength();i++){
			Node child =  nodelist.item(i);
			if(child.getNodeType()==Node.TEXT_NODE){
				html +=child.getNodeValue();
			}
			if(child.getNodeType()==Node.ELEMENT_NODE){
				if(child.getNodeName().equals("t")){
					html+=parseTag(child,et);
				}
				else{
					//TODO : 这里添加上class条件判断
					html+=" <"+child.getNodeName();
					NamedNodeMap attrs = child.getAttributes();
					for(int j=0;j<attrs.getLength();j++){
						Node attr = attrs.item(j);
						html = html + " "+attr.getNodeName()+"=\""+attr.getNodeValue()+"\"";
					}
					html +=">\n";
					if(child.hasChildNodes()){
						html += ReadByNode(child,et);
					}
					else{
						if(child.getNodeValue()!=null){
							html += child.getNodeValue();
						}
					}
					html = html + "</"+child.getNodeName()+">\n";
				}
			}
		}
		return html;
	}
	public String  loadPage(String url,String type) {
		String path = new EXml().urlToPath(url);
		List<Map<String, String>> fieldList = EXml.read(path, "field");
		if(fieldList ==null)
			return null;
		EViewType et = getNodeByType(path, type);
		Dict property = et.getDict();
		String html ="<!DOCTYPE html>\n"
				+ "<html lang='en'>\n"
				+new BaseHTML().getHeader(url);
		if(property.get("layout").equalsIgnoreCase("none")){
			Node root = EXml.getNodeById(EGlobal.PATH+"/layout/base.xml", "base_layout");
			if(root.hasChildNodes()){
				html += this.ReadByNode(root,et);
				return html;
			}
		}
		System.out.println();
		String layerout = "/layerout/panel2.html";
		System.out.println(layerout);
		BufferedReader reader = null;
		if(layerout.matches("http://.*")){
			reader = new ENetwork().Get(layerout);
		}
		else{
			reader= EFile.getBufferRead(EGlobal.PATH+"/layerout/panel2.html");			
		}
		if (reader==null) 
			return null;
		return  fillLayer(reader, fieldList,url);
		
	}
	
	public String fillLayer(BufferedReader reader,List<Map<String, String>> fieldList ,String url){
		String line ="";	
		String html ="<!DOCTYPE html>\n"
						+ "<html lang='en'>\n"
						+new BaseHTML().getHeader(url)+new BaseHTML().getNav(url);
		try {
			while ((line=reader.readLine()) !=null) {
				if(line.contains("$field_")){
					List<Map<String, String>> ls = EString.groupByCondition("$field_",line);
					line = "";
					for (Map<String, String> li:ls){
						if(li.get("start")!=null){
							html+=li.get("start");
						};
						if(li.get("field")!=null){
							if(li.get("field").equalsIgnoreCase("header")){
								html += new BaseHTML().getFormHeader("edit", "forum", "1");
//								html += Self.env.search("forum","id = 1"); 
							}
							else for(Map<String, String>fl:fieldList){
								 if(fl.get("name").equals(li.get("field"))){
									html+=fl.get("value");
									break;
								}
							}
							
						};
						if(li.get("end")!=null){
							html+=li.get("end")+"\n";
						}
					}
				}
				else{
					html+= line+"\n";					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return html+"\n</html>";
	};
		
	public static EViewType getNodeByType(String path,String type){
		NodeList nodeList = EXml.getNodeList(path);
		EViewType et = new EViewType();
		Dict dt = new Dict();
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE&&node.getNodeName().equalsIgnoreCase("rec")){
				if(node.hasChildNodes()){
					NodeList nd = node.getChildNodes();
					for(int j=0;j<nd.getLength();j++){
						Node n = nd.item(j);
						if(n.getNodeType()==Node.ELEMENT_NODE&&n.hasChildNodes()){
							Node  cr = n.getAttributes().getNamedItem("name");
							if(cr!=null&&!cr.getNodeValue().equalsIgnoreCase("view")){
								dt.update(cr.getNodeValue(),n.getFirstChild().getNodeValue());
							}
							NodeList ndls = n.getChildNodes();
							for(int k=0;k<ndls.getLength();k++){
								Node nds = ndls.item(k);
								if(nds.getNodeType()==Node.ELEMENT_NODE&&nds.getNodeName().equalsIgnoreCase(type)){
									et.setNode(nds);
									et.setDict(dt);
									return et;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

}

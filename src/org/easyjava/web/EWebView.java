package org.easyjava.web;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

import org.easyjava.file.Dict;
import org.easyjava.file.EFile;
import org.easyjava.file.EViewType;
import org.easyjava.file.EXml;
import org.easyjava.network.ENetwork;
import org.easyjava.util.EOut;
import org.easyjava.util.EString;
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
		String str = ReadByNode(EXml.getNodeById("base_layout"));
		int type = 1;
		EWebView c = new EWebView();
		
		System.out.println(str);
		
//		System.out.println(node.getDict());
	}
	
	private String parseTag(Node node) {
		NamedNodeMap attrs = node.getAttributes();
		String html = "";
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (attr.getNodeName().equals("load")) {
				Node tNode = EXml.getNodeById(attr.getNodeValue());
				if(tNode.hasChildNodes()){
					html += ReadByNode(tNode);
				}
			}
			if (attr.getNodeName().equals("t-if")) {
				//TODO:此处添加判断方法
				//暂时表示条件成立
				if(attr.getNodeValue().equals("type=form")){
					if(node.hasChildNodes()){
						html += ReadByNode(node);
					}
				}
				
			}
		}
		return html;
	}
	
	/**
	 * 递归式读取节点
	 * @param node
	 * @return
	 */
	private String ReadByNode(Node node){
		NodeList nodelist = node.getChildNodes();
		String html = "";
		
		for(int i=0;i<nodelist.getLength();i++){
			Node child =  nodelist.item(i);
			if(child.getNodeType()==Node.TEXT_NODE){
				html +=child.getNodeValue();
			}
			if(child.getNodeType()==Node.ELEMENT_NODE){
				if(child.getNodeName().equals("t")){
					html+=parseTag(child);
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
						html += ReadByNode(child);
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
//			et.getNode().
			System.out.println(EGlobal.PATH+"/layout/base.xml");
			Node root = EXml.getNodeById(EGlobal.PATH+"/layout/base.xml", "base_layout");
			if(root.hasChildNodes()){
				html += this.ReadByNode(root);
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

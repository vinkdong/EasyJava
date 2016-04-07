package org.easyjava.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.easyjava.web.EGlobal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EXml {

	public static NodeList getNodeList(String path) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(path);
			try {
				Document doc = builder.parse(file);
				Element root = doc.getDocumentElement();
				return root.getChildNodes();
			} catch (SAXException e) {
				System.err.println("初始化SAX失败");
				return null;
			} catch (IOException e) {
				System.err.println("读取IO失败");
				return null;
			}
		} catch (ParserConfigurationException e) {
			System.err.println("创建对象失败");
			return null;
		}
	}
	

	/**
	 * 得到属性id==xx的node
	 * @param path
	 * @param id
	 * @return
	 */
	public static Node getNodeById(String path,String id){
		NodeList nodeList = getNodeList(path);
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				NamedNodeMap map = node.getAttributes();
				if(map.getNamedItem("id").getNodeValue().equals(id)){
					return node;
				}
			}
		}
		return null;
	}
	
	public static Node getNodeById(String id){
		return getNodeById(EGlobal.PATH+"/layout/base.xml", id);
	}
	

	public static List<Map<String, String>> read(String path,String nodeName){
		
		DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(path);
			try {
				Document doc = builder.parse(file);
				Element root = doc.getDocumentElement();
				NodeList nodeList = root.getChildNodes();
				List<Map<String, String>> list = new ArrayList<>();
				for (int i =0;i<nodeList.getLength();i++){
					Node node = nodeList.item(i);
					HashMap<String, String> value  = new HashMap<>();
					if(node.getNodeType()== Node.ELEMENT_NODE &&node.getNodeName()==nodeName){
						if(node.hasChildNodes()){
							if (node.getChildNodes().getLength()==1){
								Node next = node.getFirstChild();
								if(!next.hasChildNodes()){
									value.put("value", next.getNodeValue().replaceAll("( )+|\r|\t|\n", ""));
									NamedNodeMap attrs = node.getAttributes();
									for (int j = 0; attrs != null && j < attrs.getLength(); j++) {
										value.put(attrs.item(j).getNodeName(), attrs.item(j).getNodeValue());
									}
								}
							}
						}
						else {							
						}
					}
					if(!value.isEmpty()){
						list.add(value);
					}
				}	
				return list;
			} catch (SAXException e) {
				System.err.println("初始化SAX失败");
				return null;
			} catch (IOException e) {
				System.err.println("读取IO失败");
				return null;
			}
		} catch (ParserConfigurationException e) {
			System.err.println("创建对象失败");
			return null;
		}
		
	}
	/**
	 * 通过视图的ID读取记录属性
	 * @param path
	 * @param id
	 * @return
	 */
	public static Dict readProperitesById(String path,String id){
		NodeList nodelist = getNodeList(path);
		String dict_str ="{";
		for(int i=0;i<nodelist.getLength();i++){
			Node node = nodelist.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				if(node.hasChildNodes()&&node.getNodeName().equalsIgnoreCase("rec")){	
					NodeList ndls = node.getChildNodes();
					for (int j=0;j<ndls.getLength();j++){
						Node nd = ndls.item(j);
						if(nd.hasChildNodes()&&nd.getNodeType()==Node.ELEMENT_NODE){
							NamedNodeMap attributes = nd.getAttributes();
							for(int k=0;k<attributes.getLength();k++){
								if(attributes.item(k).getNodeName().equalsIgnoreCase("name")){
									if(attributes.item(k).getNodeValue().equalsIgnoreCase("view")){
										
									}
									else{
										dict_str+=attributes.item(k).getNodeValue()+":"+nd.getFirstChild().getNodeValue()+",";
									}
								}
								
							}
						}
					}
				}
			}
			
		}
		Dict dict  = new Dict();
		dict.update(dict_str+"}");
		return dict;
	}
		
	/**根据请求的URL地址转换成对应的xml文件地址
	 * TODO 扩展
	 * @param url
	 * @return
	 */
	public String urlToPath(String url){
		url = url.replaceAll("(_rpc_add|_rpc_loadview|_rpc_read)", "");
		return EGlobal.PATH+"/pages"+url+".xml";
	}
	
	public List<String> getFieldList(String url){
		String file_path = urlToPath(url);
		List<String> field_list = new ArrayList<>();
		field_list.add("name");
		field_list.add("content");
		return field_list;
	}
}

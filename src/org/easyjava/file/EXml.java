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
	 * TODO 扩展
	 * @param url
	 * @return
	 */
	public String urlToPath(String url){
		return EGlobal.PATH+"/pages"+url+".xml";
	}
	

}

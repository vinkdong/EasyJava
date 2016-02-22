package org.easyjava.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.easyjava.file.file;
import org.easyjava.file.xml;
import org.easyjava.network.network;
import org.easyjava.util.eString;

public class initPage {
	
	public String  loadPage(String url) {
		String path = new xml().urlToPath(url);
		List<Map<String, String>> fieldList = xml.read(path, "field");
		if(fieldList ==null)
			return null;
		String layerout= xml.read(path, "field").get(0).get("layout");
		BufferedReader reader = null;
		if(layerout.matches("http://.*")){
			reader = new network().Get(layerout);
		}
		else{
			reader= file.getBufferRead(global.PATH+layerout);			
		}
		if (reader==null) 
			return null;
		return  fillLayer(reader, fieldList);
		
	}
	
	public String fillLayer(BufferedReader reader,List<Map<String, String>> fieldList ){
		String line ="",html="";	
		try {
			while ((line=reader.readLine()) !=null) {
				if(line.contains("$field_")){
					List<Map<String, String>> ls = eString.groupByCondition("$field_",line);
					line = "";
					for (Map<String, String> li:ls){
						if(li.get("start")!=null){
							html+=li.get("start");
						};
						if(li.get("field")!=null){
							for(Map<String, String>fl:fieldList){
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
		return html;
	};

}

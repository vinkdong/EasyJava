package org.easyjava.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.easyjava.database.DATABASE;
import org.easyjava.database.DB;
import org.easyjava.file.EFile;
import org.easyjava.file.EXml;
import org.easyjava.network.ENetwork;
import org.easyjava.util.EOut;
import org.easyjava.util.EString;

public class Init {
	
	public void initDb(String url){
		String path = new EXml().urlToPath(url);
		List<Map<String, String>> fieldList = EXml.read(path, "field");
		EOut.print(fieldList);
	}
	
	public String  loadPage(String url) {
		String path = new EXml().urlToPath(url);
		List<Map<String, String>> fieldList = EXml.read(path, "field");
		if(fieldList ==null)
			return null;
		String layerout= EXml.read(path, "field").get(0).get("layout");
		BufferedReader reader = null;
		if(layerout.matches("http://.*")){
			reader = new ENetwork().Get(layerout);
		}
		else{
			reader= EFile.getBufferRead(EGlobal.PATH+layerout);			
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

}

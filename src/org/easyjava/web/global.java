package org.easyjava.web;

import java.util.List;
import java.util.Map;

import org.easyjava.file.file;
import org.easyjava.file.xml;

public class global {
	
	public static String PATH = new  file().currentPath() ;
	public static List<Map<String, String>>  LAYEROUT =  xml.read(global.PATH+"/config.xml", "layerout");    	 

}

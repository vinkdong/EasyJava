package org.easyjava.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easyjava.database.Model;
import org.easyjava.file.EFile;
import org.easyjava.file.EXml;

public class EGlobal {
	
	public static String PATH = new  EFile().currentPath() ;
	public static List<Map<String, String>>  LAYEROUT =  EXml.read(EGlobal.PATH+"/config.xml", "layerout");    	 
	public static boolean debug = true;
	public static List<String> overwrite = new ArrayList<>();
	public static Map<String, Map<String,Map<String,String>>> models = new HashMap<>();

}

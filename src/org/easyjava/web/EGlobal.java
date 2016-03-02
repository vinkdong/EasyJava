package org.easyjava.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easyjava.file.EFile;
import org.easyjava.file.EXml;

public class EGlobal {
	
	public static String PATH = new  EFile().currentPath() ;
	public static List<Map<String, String>>  LAYEROUT =  EXml.read(EGlobal.PATH+"/config.xml", "layerout");    	 
	public static boolean debug = false;
	public static List<String> overwrite = new ArrayList<>();

}

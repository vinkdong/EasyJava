package org.easyjava.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class file {
	
	public  String currentPath(){
		return getClass().getResource("/").getPath().replaceAll("/WEB-INF/.*", "");
	}
	
	public List<File> scanFile(String path,String type){
		List<File> ll = getFiles(new File(path),type);
		return ll;
	}
	
	 public List<File> getFiles(File fileDir, String fileType) {
	        List<File> lfile = new ArrayList<File>();
	        File[] fs = fileDir.listFiles();
	        for (File f : fs) {
	            if (f.isFile()) {
	                if (fileType
	                        .equals(f.getName().substring(
	                                f.getName().lastIndexOf(".") + 1,
	                                f.getName().length())))
	                    lfile.add(f);
	            } else {
	                List<File> ftemps = getFiles(f,fileType);
	                lfile.addAll(ftemps);
	            }
	        }
	        return lfile;
	    }
	 
	 public String fileToString(String path){
		 
		return path;
		 
	 }
	 
	 public static BufferedReader  getBufferRead(String path){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			return reader;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		 
	 }
}

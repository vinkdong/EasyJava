package org.easyjava.file;

import java.io.File;
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
}

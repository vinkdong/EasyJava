package org.easyjava.util;

import java.util.ArrayList;
import java.util.List;

public class EList {
	
	public List copy(List ls){
		List res = new ArrayList<>();
		for(Object li:ls){
			res.add(li);
		}
		return res;
	}
	
	public String charListToString(List<Character>ch){
		StringBuilder str = new StringBuilder();
		for(char c :ch){
			str.append(c);
		}
		return str.toString();
	}
	
	public static String[] listToStringArray(List<String> list){
		String[] strArr = new String[list.size()];
		for(int i=0;i<list.size();i++){
			strArr[i] = list.get(i);
		}
		return strArr;
	}

}

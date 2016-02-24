package org.easyjava.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EString {
	
	public static List<Map<String, String>> groupByCondition(String pattern,String str){
		char[] charList =str.toCharArray();
		char[] parttenList = pattern.toCharArray();
		List<Character> current = new ArrayList<>();;
		int matchIndex = 0;
		String state = "start";
		int marchLength = parttenList.length;
		List<Map<String, String>> ls = new ArrayList<>();
		if(marchLength==0){
			return null;
		}	
		for(char x:charList){
			current.add(x);
			if(state=="marching"){
				Pattern p =Pattern.compile("[A-Za-z0-9_]");
				if(!p.matcher(String.valueOf(x)).matches()){
					current.remove(current.size()-1);
					Map<String, String>  currentMap = new HashMap<>();
					currentMap.put("field", new EList().charListToString(current));
					ls.add(currentMap);
					current.clear();
					if(x!=32)
						current.add(x);
					state ="marched";
				}
			}
			else if (x==parttenList[matchIndex]){
				matchIndex++;
				if(matchIndex==marchLength){
					for (int i = 0; i < marchLength; i++) {
						current.remove(current.size()-1);
					}
					Map<String, String>  currentMap = new HashMap<>();
					if(state=="start"){
						currentMap.put("start", new EList().charListToString(current));
					}
					if(state=="marched"){
						currentMap.put("string", new EList().charListToString(current));
					}
					state = "marching";
					ls.add(currentMap);
					matchIndex=0;
					current.clear();
				}
			}
		}
		if(state == "marching"){
			Map<String, String>  currentMap = new HashMap<>();
			currentMap.put("field", new EList().charListToString(current));
			ls.add(currentMap);
			current.clear();
		}
		Map<String, String>  currentMap = new HashMap<>();
		currentMap.put("end", new EList().charListToString(current));
		ls.add(currentMap);
		return ls;
	}

}


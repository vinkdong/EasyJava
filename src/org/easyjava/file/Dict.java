package org.easyjava.file;

import java.util.ArrayList;
import java.util.List;
import org.easyjava.util.EList;
import org.junit.Test;

public class Dict {

	private String dict_str = "";

	@Test
	public void xx() {
		String dict = "{\"jsonrpc\":  \"2,0\",\"params\":{\"id\":\"1\",\"name\":\""
				+ "341\",\"sex\":\"4241\",content:\"42341412\"},\"id\":742983313}";
		Dict dt = new Dict();
		dt.update(dict);
		dt.update("id", dt);
		System.out.println(dt.get("id"));
	}
	
	public String toString(){
		return dict_str;
	}

	public void update(String dict) {
		if (dict_str.equals("")) {
			dict_str = dict;
		}
	}

	/**
	 * update方法添加以key为key，值为dict的字典
	 * 
	 * @param key
	 * @param dict
	 */
	public void update(String key, Dict dict) {
		if(this.hasKey(key)){
//			int[] addr = readAddr(key);
//			dict_str.subSequence(start, start+)
//			
		}
		else
			System.out.println("this don't has key:"+key);
		
	}
	private int[] readAddr(String key){
		char[] attr = dict_str.toCharArray();
		int deep = 0;
		char[] key_char = key.toCharArray();
		int cr = 0;
		int length = key_char.length;
		int start = 0;
		int index = 0;
		for (char a : attr) {
			if (a == '{') {
				deep++;
			}
			if (deep == 1) {
				if(cr==length){
					System.out.print("index:");
					System.out.println(index-length-1);
//					return index;
				}
				if(a==key_char[cr]){
					cr ++ ;
				}
				else{
					cr = 0;
				}
			} else {
				if (a == '}') {
					deep--;
				}
			}
			index ++ ;
		}
		return null;
		
	}
	public String[] read(String dict) {
		char[] attr = dict.toCharArray();
		int deep = 0;
		List<Character> res = new ArrayList<>();
		boolean mind = false;
		boolean need_replace = true;
		for (char a : attr) {
			if (mind){
				if(a == ' '){
					continue;
				}
				else if(a=='"'){
					need_replace = !need_replace;
					if(need_replace){
						mind = false;
					}
				}				
			}
			if (a == '{') {
				deep++;
			}
			if (deep == 1 && a == ','&&need_replace) {
				res.add('$');
				res.add('$');
			} else if (deep == 1 && a == ':'&&need_replace) {
				res.add('*');
				res.add('$');
				mind = true;
			} else {
				res.add(a);
				if (a == '}') {
					deep--;
				}
			}
		}

		dict = new EList().charListToString(res);
		dict = dict.substring(1, dict.length() - 1);
		return dict.split("\\$\\$");
	}

	public Dict getDict(String para) {
		for (String s : read(dict_str)) {
			String[] tuple = s.split("\\*\\$");
			if (tuple[0].equals("\"" + para + "\"")) {
				Dict dict = new Dict();
				dict.update(tuple[1]);
				return dict;
			}
			if (tuple[0].equals(para)) {
				Dict dict = new Dict();
				dict.update(tuple[1]);
				return dict;
			}
		}
		return null;
	}

	public String get(String para) {
		for (String s : read(dict_str)) {
			String[] tuple = s.split("\\*\\$");
			if (tuple[0].equals("\"" + para + "\"")) {
				return tuple[1];
			}
			if (tuple[0].equals(para)) {
				return tuple[1];
			}
		}
		return "";
	}

	public String[] getKeys() {
		List<String> list = new ArrayList<>();
		for (String s : read(dict_str)) {
			list.add(s.split("\\*\\$")[0].replace("\"", ""));
		}
		return EList.listToStringArray(list);
	}

	public boolean hasKey(String key) {
		for (String s : read(dict_str)) {
			if (s.split("\\*\\$")[0].replace("\"", "").equals(key)) {
				return true;
			} else if (s.equals("\"" + key + "\"")) {
				return true;
			}
		}
		return false;
	}
}

package org.easyjava.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easyjava.util.EList;
import org.easyjava.util.EOut;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.sun.xml.internal.fastinfoset.util.CharArray;

public class Dict {
	
	private String dict_str = "";
	
	@Test
	public void xx(){
		String dict = "{\"jsonrpc\":  \"2.0\",\"params\":{\"id\":\"1\",\"name\":\"341\",\"sex\":\"4241\",\"content\":\"42341412\"},\"id\":742983313}";
		Dict dt = new  Dict();
		dt.update(dict);
		Dict id = dt.getDict("params");
		System.out.println(id.get("content"));
	}
	
	public void update(String dict){
		if(dict_str.equals("")){
			dict_str = dict;
		}
	}
	
	public String[] read(String dict){
		char[] attr = dict.toCharArray();
		int deep = 0;
		List<Character> res = new ArrayList<>();
		for(char a:attr){
			if( a=='{'){
				deep ++;
			}
			if(deep==1 && a==','){
					res.add('$');
					res.add('$');
			}
			else if(deep==1 && a==':'){
				res.add('*');
				res.add('$');
			}
			else{
				res.add(a);
				if(a=='}'){
					deep--;
				}
			}
		}
		
		dict = new EList().charListToString(res);
		dict = dict.substring(1, dict.length()-1);
		return dict.split("\\$\\$");
	}
	
	public Dict getDict(String para){
		for(String s:read(dict_str)){
			String[] tuple = s.split("\\*\\$");
			if(tuple[0].equals("\""+para+"\"")){
				Dict dict = new Dict();
				dict.update(tuple[1]);
				return dict;
			}
		}
		return null;	
	}
	
	public String get(String para){
		for(String s:read(dict_str)){
			String[] tuple = s.split("\\*\\$");
			if(tuple[0].equals("\""+para+"\"")){
				return tuple[1];
			}
		}
		return "";	
	}
}

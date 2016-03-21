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
	
	Map<?, ?> map_lv1 = new HashMap<>();
	Map<?, ?> map_lvn = new HashMap<>();
	List<String> deeps = new ArrayList<>();
	
	@Test
	public void xx(){
		String dict = "{\"jsonrpc\":  \"2.0\",\" params\":{\"id\":\"1\",\"name\":\"341\",\"sex\":\"4241\",\"content\":\"42341412\"},\"id\":742983313}";
		update(dict);
	}
	
	public void update(String dict){
		dict = dict.replace(" ", "");
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
			else{
				res.add(a);
				if(a=='}'){
					deep--;
				}
			}
		}
		dict = new EList().charListToString(res);
		System.out.println(dict);
	}
	
	public Dict get(String para){
		
		return null;	
	}

}

package org.easyjava.file;

import java.util.ArrayList;
import java.util.List;
import org.easyjava.util.EList;
import org.easyjava.util.EString;
import org.junit.Test;

public class Dict {

	private String dict_str = "";

	@Test
<<<<<<< Updated upstream
	public void xx() {
		String dict = "{\"jsonrpc\":  \"2,0\",\"params\":{\"id\":\"1\",\"name\":\""
				+ "341\",\"sex\":\"4241\",content:\"42341412\"},,\"id\":\"742983313\"}";
		Dict dt = new Dict();
		dt.update(dict);
		Dict dd = dt.getDict("params");
		dd.delete("id");
		System.out.println(dd);
	    
=======
	public void xx(){
		
		Object x  = "fdaaf";
		Map<String, Object> y = new HashMap<>();
		Map<String, Object> z = new HashMap<>();
		z.put("fadf", y);
		z.put("fdaf", "fefwe");
		System.out.println(z.get("fdaf"));
		System.out.println(x.toString());
		String dict = "{\"jsonrpc\":  \"2.0\",\" params\":{\"id\":\"1\",\"name\":\"341\",\"sex\":\"4241\",\"content\":\"42341412\"},\"id\":742983313}";
		update(dict);
>>>>>>> Stashed changes
	}
	
	public String toString(){
		return dict_str;
	}

	public void update(String dict) {
		if (dict_str.equals("")) {
			dict_str = dict;
		}
		else{
			Dict dt = new Dict();
			dt.update(dict);
			for(String key:dt.getKeys()){
				this.update(key,dt.get(key));
			}
		}
	}
	public void delete(String key){
		if(this.hasKey(key)){
			int[] addr = readAddr(key);
			int next =0;
			while(dict_str.charAt(addr[1]+next)==','||dict_str.charAt(addr[1]+next)==' '){
				next++;
				addr[1]++;
			}
			dict_str = EString.replace(addr[0], addr[1], dict_str,"");
		}
	}
	
	public void update(String key,String val){
		if(dict_str.equals("")){
			dict_str = "{"+key+":"+val+"}";
		}
		else if(this.hasKey(key)){
			int[] addr = readAddr(key);
			dict_str = EString.replace(addr[0]+1, addr[1], dict_str,key+":"+val);
		}
		else{
			String insert_dict = "";
			if(this.getKeys().length>0){
				insert_dict = ","+ key+":"+val;
			}
			else{
				insert_dict = key+":"+val;
			}
			dict_str = EString.insert(dict_str.length()-1, dict_str,insert_dict);
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
			int[] addr = readAddr(key);
			dict_str = EString.replace(addr[0], addr[1], dict_str,key+":"+dict.toString());
		}
		else{
			String insert_dict = "";
			if(this.getKeys().length>0){
				insert_dict = ","+ key+":"+dict.toString();
			}
			else{
				insert_dict = key+":"+dict.toString();
			}
			dict_str = EString.insert(dict_str.length()-1, dict_str,insert_dict);
		}	
	}
	private int[] readAddr(String key){
		char[] attr = dict_str.toCharArray();
		int deep = 0;
		char[] key_char = key.toCharArray();
		boolean mind = false;
		boolean need_replace = true;
		int cr = 0;
		int length = key_char.length;
		int index = 0;
		boolean is_key = true;
		int[] addr = new int[2];
		addr[0]=-1;
		for (char a : attr) {
			if (mind){
				if(a=='"'){
					need_replace = !need_replace;
					if(need_replace){
						mind = false;
					}
				}				
			}
			if (a == '{') {
				deep++;
			}
			if (deep ==1 && is_key && addr[0]<0){
				if(cr==length){
					addr[0]=index-length-1;
					index ++ ;
					continue;
				}
				if(a==key_char[cr]){
					cr ++ ;
				}
				else{
					cr = 0;
				}
			}
			if (deep == 1 && a == ','&&need_replace) {
				if(addr[0]!=-1){
					addr[1]=index;
					return addr;
				}
				is_key = true;
				
			} else if (deep == 1 && a == ':'&&need_replace) {
				is_key = false;
				mind = true;
			} else {
				if (a == '}') {
					deep--;
					if(deep==0&&addr[0]>0){
						addr[1]=index;
						return addr;
					}
				}
			}
			index ++ ;
		}
		return null;
		
	}
	public String[] read(String dict) {
		if(dict.equals("")){
			return null;
		}
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
			if (a == '{'||a=='['){
				deep++;
			}
			if (deep == 1 && a == ','&&need_replace) {
				res.add('$');
				res.add('*');
			} else if (deep == 1 && a == ':'&&need_replace) {
				res.add('*');
				res.add('$');
				mind = true;
			} else {
				res.add(a);
				if (a == '}'||a==']') {
					deep--;
				}
			}
		}
		dict = new EList().charListToString(res);
		dict = dict.substring(1, dict.length() - 1);
		return dict.split("\\$\\*");
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
				if(tuple.length==2){
					return tuple[1];
				}
				else{
					return "";
				}
			}
		}
		return "";
	}

	public String[] getKeys() {
		String[] str_arr = read(dict_str);
		if(str_arr==null){
			return new String[0];
		}
		List<String> list = new ArrayList<>();
		for (String s : str_arr) {
			list.add(s.split("\\*\\$")[0].replace("\"", ""));
		}
		return EList.listToStringArray(list);
	}

	public boolean hasKey(String key) {
		String[] str_arr = read(dict_str);
		if(str_arr==null){
			return false;
		}
		for (String s : str_arr) {
			if (s.split("\\*\\$")[0].replace("\"", "").equals(key)) {
				return true;
			} else if (s.equals("\"" + key + "\"")) {
				return true;
			}
		}
		return false;
	}
}

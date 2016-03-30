package org.easyjava.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ETool {
	
	/**
	 * 读取String Map类型，无key返回“”
	 * @param key
	 * @param map
	 * @return
	 */
	public static String get(Map<String, String> map,String key){
		if(map.containsKey(key)){
			return map.get(key);
		}
		return "";
	}
	
	/**
	 * 读取HttpServlerRequest参数，如果为空返回空字符串
	 * @param key
	 * @param request
	 * @return
	 */
	public static String get(String key,HttpServletRequest request){
		String res = request.getParameter(key);
		if(res !=null) 
			return res;
		return "";
	}

}

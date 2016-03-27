package org.easyjava.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EString {

	public static List<Map<String, String>> groupByCondition(String pattern, String str) {
		char[] charList = str.toCharArray();
		char[] parttenList = pattern.toCharArray();
		List<Character> current = new ArrayList<>();
		int matchIndex = 0;
		String state = "start";
		int marchLength = parttenList.length;
		List<Map<String, String>> ls = new ArrayList<>();
		if (marchLength == 0) {
			return null;
		}
		for (char x : charList) {
			current.add(x);
			if (state == "marching") {
				Pattern p = Pattern.compile("[A-Za-z0-9_]");
				if (!p.matcher(String.valueOf(x)).matches()) {
					current.remove(current.size() - 1);
					Map<String, String> currentMap = new HashMap<>();
					currentMap.put("field", new EList().charListToString(current));
					ls.add(currentMap);
					current.clear();
					if (x != 32)
						current.add(x);
					state = "marched";
				}
			} else if (x == parttenList[matchIndex]) {
				matchIndex++;
				if (matchIndex == marchLength) {
					for (int i = 0; i < marchLength; i++) {
						current.remove(current.size() - 1);
					}
					Map<String, String> currentMap = new HashMap<>();
					if (state == "start") {
						currentMap.put("start", new EList().charListToString(current));
					}
					if (state == "marched") {
						currentMap.put("string", new EList().charListToString(current));
					}
					state = "marching";
					ls.add(currentMap);
					matchIndex = 0;
					current.clear();
				}
			}
		}
		if (state == "marching") {
			Map<String, String> currentMap = new HashMap<>();
			currentMap.put("field", new EList().charListToString(current));
			ls.add(currentMap);
			current.clear();
		}
		Map<String, String> currentMap = new HashMap<>();
		currentMap.put("end", new EList().charListToString(current));
		ls.add(currentMap);
		return ls;
	}

	public static Map<String, String> stringToMap(String str) {
		String[] slst = str.split(",");
		Map<String, String> res = new HashMap<>();
		for (String sl : slst) {
			String[] st = sl.split("=");
			res.put(st[0], st[1]);
		}
		return res;

	}

	/**
	 * 格式化输出字符串 左对齐
	 * 
	 * @param length
	 * @param str
	 * @return
	 */
	public static String lpad(int length, String str) {
		String f = "";

		int i = length - str.length();
		for (int j = 0; j < i; j++) {
			f += " ";
		}
		return str + f;
	}

	/**
	 * 格式化输出字符串 右对齐
	 * 
	 * @param length
	 * @param str
	 * @return
	 */
	public static String rpad(int length, String str) {
		String f = "";

		int i = length - str.length();
		for (int j = 0; j < i; j++) {
			f += " ";
		}
		return f + str;
	}

	/**
	 * 字符串数组转int数组
	 * 
	 * @param strArr
	 * @return
	 */
	public static int[] StringToInt(String[] strArr) {
		int[] aArr = new int[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			aArr[i] = Integer.parseInt(strArr[i]);
		}

		return aArr;
	}

	/**
	 * 复制字符串数组 返回新字符串数组 非引用
	 * 
	 * @param orgArray
	 * @return
	 */
	public static String[][] StringCopy(String[][] orgArray) {

		int length = orgArray.length;
		String[][] copyArr = new String[length][length];

		for (int i = 0; i < length; i++) {

			for (int j = 0; j < length; j++) {
				copyArr[i][j] = orgArray[i][j];
			}
		}

		return copyArr;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param origin_string
	 * @param insert_string
	 * @return
	 */
	public static String replace(int start,int end,String origin_string,String insert_string){
		String first = origin_string.substring(0, start);
		String last = origin_string.substring(end);
		return first+insert_string+last;
	}
	
	public static String insert(int position,String origin_string,String insert_string){
		if(origin_string.equals("")){
			return insert_string;
		}
		return origin_string.substring(0, position)+insert_string+origin_string.substring(position, origin_string.length());
	}

}

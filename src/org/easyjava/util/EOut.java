package org.easyjava.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.easyjava.web.EGlobal;

public class EOut {
	public static void print(String[][] strarr) {
		for (int i = 0; i < strarr.length; i++) {
			for (int j = 0; j < strarr[i].length; j++) {
				System.out.print(" " + strarr[i][j]);
				if (i % 20 == 0)
					System.out.println();
			}
			System.out.println();
		}
	}
	/**
	 * 打印List<Map<String, String>>
	 * @param strarr
	 */
	public static void print(List<Map<String, String>> list) {
		for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
				if (i % 20 == 0)
					System.out.println();
			
			System.out.println();
		}
	}
	public static void print(Integer[] strarr) {
		for (int i = 0; i < strarr.length; i++) {

			if (strarr[i] != 0) {
				System.out.print(" " + strarr[i]);
				if (i % 20 == 0)
					System.out.println();
			}

		}
		System.out.println();
	}

	public static void print(String[] strarr) {

		for (int i = 0; i < strarr.length; i++) {

			System.out.println((i + 1) + ":" + strarr[i]);

		}
	}

	public static void print(int[] strarr) {
		for (int i = 0; i < strarr.length; i++) {

			if (strarr[i] != 0) {
				System.out.print(" " + strarr[i]);
				if (i % 20 == 0)
					System.out.println();
			}

		}
		System.out.println();
	}

	public static void print(String str) {
		if (EGlobal.debug) {
			System.out.println(str);
		}

	}
	
	public static void print(char[] ch){
		for (char a:ch){
			System.out.println(a);
		}
	}

	/**
	 * 打印ResultSet
	 * 
	 * @param rs
	 */
	public static void print(ResultSet rs) {

		try {
			int i = rs.getMetaData().getColumnCount() + 1;
			int[] showl = new int[i];
			int point = 0;
			while (rs.next()) {
				++point;
				for (int j = 1; j < i; j++) {
					if (point == 1) {
						showl[j - 1] = rs.getString(j).length() + 5;
					}
					System.out.print(EString.lpad(showl[j - 1], rs.getString(j)) + "  |   ");
				}

				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package org.easyjava.util;

import java.util.ArrayList;
import java.util.List;

public class eList {
	
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

}

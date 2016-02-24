package org.easyjava.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.easyjava.util.EOut;
import org.easyjava.util.EString;

public class Model {	
	protected String string ="";
	protected String relation ="";
	protected String help ="";
	protected String type = "";
	

	public  static void add(String model_name,String[] fields) {
				
		String sql = "CREATE TABLE "+model_name+"( \n" 
						+ "ID SERIAL PRIMARY KEY  ,\n"  ; 
		for(String s:fields){
			Map<String, String> field = EString.stringToMap(s);
			switch (field.get("type")) {
			case "Char":
			case "char":
				sql +=  " "+field.get("field")+" CHAR(255)  ,\n" ;
				break;
			case "Text":
			case "text":	
				sql +=  " "+field.get("field")+" TEXT  ,\n" ;
				break;		
			case "Float":
			case "Double":
				sql +=  " "+field.get("field")+" REAL  ,\n" ;
				break;
			default:
				break;
			}
		}
		sql = sql.substring(0,sql.length()-2);
		sql += "); ";
		EOut.out(sql);
		//TODO :仅对postgres有效
		 try {
			Statement st = DB.connection.createStatement();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}

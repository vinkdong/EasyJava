package org.easyjava.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.easyjava.util.EOut;

public class DB {
	public static Connection connection=null;
	public static Statement stmt = null;
	public static void init(){
		String url = "jdbc:"+DATABASE.DATABASE_TYPE+"://"+DATABASE.DATABASE_LOCATION+
				":"+DATABASE.DATABASE_PORT+"/"+DATABASE.DATABASE_NAME;
		try {
			Class.forName("org.postgresql.Driver");
			connection=DriverManager.getConnection(url,DATABASE.DATABASE_USER,DATABASE.DATABASE_PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static Boolean add(String table_name,Map<String, String> val){
		String column = " (";
		String val_s = " (";
		for(String key:val.keySet()){
			  column += key + ",";
			  val_s +=  "'"+val.get(key)+"',";
		}
		column = column.substring(0, column.length()-1) + ") ";
		val_s = val_s.substring(0, val_s.length()-1) + ") ";
		String sql = "insert into "+table_name + column +" VALUES "+ val_s ;
		EOut.print(sql);
		try {
			Statement stmt =  connection.createStatement();
			int i = stmt.executeUpdate(sql);
			stmt.close();
			return i != -1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void add(String table,List<Map<String, String>> vals){
		
	}

}

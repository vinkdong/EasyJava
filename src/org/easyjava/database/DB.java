package org.easyjava.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	
	public static int add(String table_name,Map<String, String> val){
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
			while(connection ==null){
				System.out.println("正在连接数据库");
				DATABASE.DATABASE_LOCATION = "127.0.0.1";
				DATABASE.DATABASE_NAME = "easyjava";
				DATABASE.DATABASE_PORT = "5432";
				DATABASE.DATABASE_USER = "ej";
				DATABASE.DATABASE_PASSWORD = "admin";
				DATABASE.DATABASE_TYPE="postgresql";
				DB.init();
			}
			Statement stmt =  connection.createStatement();
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}
			stmt.close();
			return -1;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void add(String table,List<Map<String, String>> vals){
		
	}

}

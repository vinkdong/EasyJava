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
				DATABASE.init();
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
	
	public static int update(String table,Map<String, String> vals,String id){
		StringBuilder sb = new StringBuilder();
		for(String key:vals.keySet()){
			sb.append(key+"='"+vals.get(key)+"',");
		}
		if(sb.length()>0){
			String val = sb.toString();
			val = val.substring(0, val.length()-1);
			while(connection ==null){
				System.out.println("正在连接数据库");
				DATABASE.init();
				DB.init();
			}
			String sql = "update "+table + " set "+val+" where id="+id;
			Statement stmt;
			EOut.print(sql);
			try {
				stmt = connection.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				return Integer.parseInt(id);	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}
		return 1;
	}

}

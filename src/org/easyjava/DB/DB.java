package org.easyjava.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DB {
	public static Connection connection=null;
	public static void init(){
		String url = "jdbc:"+DATABASE.DATABASE_TYPE+"://"+DATABASE.DATABASE_LOCATION+
				":"+DATABASE.DATABASE_PORT+"/"+DATABASE.DATABASE_NAME;
		System.out.println(url);
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
	
	public void define(List<Model> args) {
		
	}
	
	

}

package org.easyjava.database;

public class DATABASE {
	public static String DATABASE_NAME ="";
	public static String DATABASE_PORT="";
	public static String DATABASE_USER = "";
	public static String DATABASE_PASSWORD="";
	public static String DATABASE_LOCATION="";
	public static String DATABASE_TYPE="";
	
	public static void init(){
		DATABASE.DATABASE_LOCATION = "127.0.0.1";
		DATABASE.DATABASE_NAME = "easyjava";
		DATABASE.DATABASE_PORT = "5432";
		DATABASE.DATABASE_USER = "ej";
		DATABASE.DATABASE_PASSWORD = "admin";
		DATABASE.DATABASE_TYPE="postgresql";		
	}
}

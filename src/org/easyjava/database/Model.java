package org.easyjava.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.easyjava.util.EOut;
import org.easyjava.util.EString;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class Model {	
	protected String string ="";
	protected String relation ="";
	protected String help ="";
	protected String type = "";
	

	public  static void add(String model_name,String[] fields) {
		String query_table_exist = "select count(*) from pg_class where relname = '"+model_name+"'";
		String sql = "CREATE TABLE "+model_name+"( \n" 
						+ "ID SERIAL PRIMARY KEY  ,\n"  ; 
		String o2m = "";
		for(String s:fields){
			Map<String, String> field = EString.stringToMap(s);
			String type = field.get("type");
			switch (type) {
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
				Pattern p = Pattern.compile("(o|O)ne2many:.*");
				if(p.matcher(type).matches()){
					//一对多关系,原则不设立
				}
				break;
			}
		}
		sql += o2m;
		sql = sql.substring(0,sql.length()-2);
		sql += "); ";
		//TODO :仅对postgres有效
		 try {
			Statement st = DB.connection.createStatement();
			ResultSet tb_rs = st.executeQuery(query_table_exist);
			tb_rs.next();
			if (tb_rs.getInt(1)==0){
				EOut.print(sql);
				st.executeUpdate(sql);
			};
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static List<Map<String, String>> read(String table_name,String condition){
		try {
			List<Map<String, String>> res = new ArrayList<>();
			String sql = "select * from "+table_name+" where "+condition;
			Statement st = DB.connection.createStatement();
			
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				Map<String, String> map = new HashMap<>();
				for(int i = 0 ;i <rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				res.add(map);
			}
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	

}

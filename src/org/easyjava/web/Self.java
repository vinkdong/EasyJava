package org.easyjava.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easyjava.database.DB;
import org.easyjava.database.Model;
import org.easyjava.file.Dict;
import org.easyjava.file.EXml;
import org.easyjava.util.EOut;

public class Self {
	
	public static String model = "";
	
	static class env{
		
		static class m2m{
			public static String mid = "0";
			public static int add(String model,String field,int[] ids){
				for(int id:ids){
					System.out.println(model);
					Map<String, String> val = new HashMap<>();
					val.put(model+"_id", mid);
					String relation = Model.getRelation(model, field);
					val.put(relation+"_id", String.valueOf(id));
					DB.add(model+"_rel", val);
				}
				return 1;
			}
		}
		public static String search(String model,String domain){
			Model.read(model, domain);
			String  html = "<div class=\"col-md-8\">" +
					 "           <a>用户名</a>" +
					 "            时间" +
					 "        </div>" +
					 "        <div class=\"col-md-8\"><h4>离产品成功，您还差一个靠谱的开发团队</h4>" +
					 "            <p>提交您的项目创意，将由专业项目经理与您一对一沟通，并组建精英团队，项目过程透明可跟踪，帮您快速完成产品</p>" +
					 "        </div>";
			return html;
			
		}
		
		public static Dict rpcToDict(HttpServletRequest request) {
			try {
				BufferedReader br = request.getReader();
				String dict_str = br.readLine();
				String path = new EXml().urlToPath(request.getServletPath());
				Dict dict = new Dict();
				dict.update(dict_str.replaceAll("\"", "&%&").replaceAll("\\\\&%&", "\"").replace("&%&", ""));
				Dict params = dict.getDict("params");
				if(params.hasKey("model")){
					dict.update("path",path+"/"+params.get("model")+".xml");
				}
				return dict;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static int update(Dict params,List<String>file_list){
			Map<String, String> res = new HashMap<>();
			for (String rec : file_list) {
				String type = Model.getType(params.get("model"), rec);
				if((type!=null&&(type.equalsIgnoreCase("one2many")||type.equalsIgnoreCase("many2many")))||rec.equals("id")){
					
				}
				else{
					res.put(rec, params.get(rec));
				}
			}
			if(params.get("id").equals("")||params.get("id").equals("none")){
				return DB.add(params.get("model"), res);
			}
			return DB.update(params.get("model"), res, params.get("id"));
		}
		
		public static int add(HttpServletRequest request) {
			Dict dict = rpcToDict(request);
			Dict params = dict.getDict("params");
			List<String> file_list = new EXml().getFieldList(EGlobal.PATH,params.get("model"));
			
			return update(params,file_list);
		}
		
		public static String read(HttpServletRequest request){
			return new BaseHTML().getFormHeader("view", request.getParameter("model"), request.getParameter("id"));
		}
		
		public static List<Map<String, String>> read(Dict properties){
			if(properties.get("model").equals("")){
				return null;
			}
			else {
				return Model.read(properties.get("model"), "1=1");
			}
		}
		
		public static Map<String,String> browse(int id){
			if(model.equals("")){
				return null;
			}
			else{
				List<Map<String, String>> dataset = Model.read(model, "id ="+id+"");
				if (dataset!=null&&dataset.size()==1){
					return dataset.get(0);
				}
				else
					return null;
			}
		}
		
		public static Map<String,String> browse(String model, int id){
			if(model.equals("")){
				return null;
			}
			else{
				List<Map<String, String>> dataset = Model.read(model, "id ="+id+"");
				if (dataset!=null&&dataset.size()==1){
					return dataset.get(0);
				}
				else
					return null;
			}
		}
		
		public static int unlink(int id){
			if(model.equals("")){
				return -1;
			}
			return Model.unlink(model,id);
		}
		
	}

}

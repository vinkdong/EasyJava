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

public class Self {
	
	public static String model = "";
	
	static class env{
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
				Dict dict = new Dict();
				dict.update(dict_str.replaceAll("\"", "&%&").replaceAll("\\\\&%&", "\"").replace("&%&", ""));
				return dict;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public static int add(HttpServletRequest request) {
			Dict dict = rpcToDict(request);
			Dict params = dict.getDict("params");
			List<String> file_list = new EXml().getFieldList(request.getServletPath());
			Map<String, String> res = new HashMap<>();
			for (String rec : file_list) {
				res.put(rec, params.get(rec));
			}
			return DB.add("forum", res);
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
		
	}

}

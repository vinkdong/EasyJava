package org.easyjava.web;

import java.util.List;
import java.util.Map;

import org.easyjava.database.Model;

public class Self {
	
	static class env{
		public static String search(String model,String domain){
			List<Map<String, String>> res = Model.read(model, domain);
			String  html = "<div class=\"col-md-8\">" +
					 "           <a>用户名</a>" +
					 "            时间" +
					 "        </div>" +
					 "        <div class=\"col-md-8\"><h4>离产品成功，您还差一个靠谱的开发团队</h4>" +
					 "            <p>提交您的项目创意，将由专业项目经理与您一对一沟通，并组建精英团队，项目过程透明可跟踪，帮您快速完成产品</p>" +
					 "        </div>";
			return html;
			
		}
	}

}

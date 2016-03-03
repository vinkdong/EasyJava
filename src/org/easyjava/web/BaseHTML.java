package org.easyjava.web;

import java.util.List;
import java.util.Map;

import org.easyjava.file.EXml;

public class BaseHTML {
		public String initHtml(String html) {
			
			return html;
			
		}
		public String headHtml(String html){
			
			return html;
		}
		
		public String footHtml(String html){
			
			return html;
		}
		
		public String scanCss(String page){
			List<Map<String, String>> list = EXml.read(EGlobal.PATH+"/config.xml","css");
			String css = "";
			String level = "";
		    for (char x :page.toCharArray())
		    	if(x==47) level+="../";
			for(Map<String, String>ls : list){
				for(String x: ls.get("value").split(";"))
				css =css +"\t\t"+String.format("<link rel='stylesheet' href='%s' type='text/css' />\n ",level+x);
			}
			return css;	
		}
		
		public String pageCss(String page){
			List<Map<String, String>> list = EXml.read(EGlobal.PATH+"/config.xml","css-once");
			String css="",level = "";
		    for (char x :page.toCharArray())
		    	if(x==47) level+="../";
			for(Map<String, String>ls : list){
				if(!ls.get("page").equals(page)) continue;
				for(String x: ls.get("value").split(";"))
				css =css +"\t\t"+String.format("<link rel='stylesheet' href='%s' type='text/css' />\n ",level+x);
			}
			return css;		
			
		}
		
		/**
		 * 
		 * @param page \?
		 * @return
		 */
		public String scanJs(String page){
			List<Map<String, String>> list = EXml.read(EGlobal.PATH+"/config.xml","js");
			String js = "";
			String level = "";
		    for (char x :page.toCharArray())
		    	if(x==47) level+="../";
			for(Map<String, String>ls : list){
				for(String x: ls.get("value").split(";"))
				js =js +"\t\t"+String.format("<script type=\"text/javascript\" src=\"%s\"></script>\n",level+x);
			}
			return js;
		}
		
		public String pageJs(String page){
			List<Map<String, String>> list = EXml.read(EGlobal.PATH+"/config.xml","js-once");
			String js = "";
			String level = "";
		    for (char x :page.toCharArray())
		    	if(x==47) level+="../";			
			for(Map<String, String>ls : list){
				if(!ls.get("page").equals(page)) continue;
				for(String x: ls.get("value").split(";"))
				js =js +"\t\t"+String.format("<script type=\"text/javascript\" src=\"%s\"></script>\n ",level+x);
			}
			return js;		
		}
		
		public String completeHTML(String page){			
			page = page.substring(1);
			String html = 
					"<!DOCTYPE html>\n"
					+ "<html lang='en'>\n"
					+ "<head>\n"
					+ scanCss(page)
					+ pageCss(page)
					+ scanJs(page)
					+ pageJs(page)
					+ "\t</head>\n"
					+ "\t<body>\n"
					+ "\t</body>\n"
					+ "</html>\n";
			return html;
		}
		
		public String getHeader(String page){
			page = page.substring(1);
			String html = 
					  "<head>\n"
					+ "<meta charset='utf-8'/>\n"
					+ "<meta name='viewport' content='initial-scale=1'/>\n"
					+ "<meta name='description' content='EasyJava'/>\n"
					+ "<meta name='keywords' content='EasyJava'/>\n"
					+ "<meta name='generator' content='EasyJava'/>\n"
					+ "<title>在这里开启你的网站开发之路</title>\n"
					+ scanCss(page)
					+ pageCss(page)
					+ scanJs(page)
					+ pageJs(page)
					+ "\t</head>\n";
			return html;			
		}
		
		public String getNav(String page){
			String html = "";
			html += "<nav class=\"navbar navbar-default site-header\">\n" +
					"    <div class=\"container overflow\">\n" +
					"        <div class=\"navbar-header\">\n" +
					"            <button type=\"button\" data-toggle=\"collapse\" data-target=\"#navigation\" aria-expanded=\"false\"\n" +
					"                    class=\"navbar-toggle collapsed\"><span class=\"sr-only\">Toggle navigation</span><span\n" +
					"                    class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button>\n" +
					"            <a href=\"/\" class=\"navbar-brand\">\n" +
					"                <img src=\"static/img/logo.png\" width=\"73\" height=\"73\">\n" +
					"            </a>\n" +
					"        </div>\n" +
					"        <div id=\"navigation\" class=\"navbar-collapse collapse\">\n" +
					"            <ul class=\"nav navbar-nav navbar-center\">\n" +
					"                <li><a href=\"/\" class=\"active\">首页</a></li>\n" +
					"                <li><a href=\"/who-we-are\" class=\"\">我们是谁</a></li>\n" +
					"                <li><a href=\"/success-stories\" class=\"\">解决方案</a></li>\n" +
					"                <li><a href=\"/how-it-works\" class=\"\">如何使用</a></li>\n" +
					"                <li><a href=\"/join-us\" class=\"\">加入我们</a></li>\n" +
					"                <li class=\"visible-xs-inline visible-sm-inline\"><a href=\"/user/login\">登录</a></li>\n" +
					"            </ul>\n" +
					"            <ul class=\"nav navbar-nav navbar-right hidden-xs hidden-sm\">\n" +
					"                <!--登录注册按钮-->\n" +
					"                <li class=\"login\"><a href=\"/goLogin\">登录</a></li>\n" +
					"            </ul>\n" +
					"        </div>\n" +
					"    </div>\n" +
					"</nav><br/><br/>";
			return html;
		}
		
		public String getFormHeader(String type,String model,String id){
			String html = "";
			if(type.equals("edit")){
				html  += "<div class=\"row e_form\" data-id=\"none\">\n" +
						"        <div class=\"col-md-8\">\n" +
						"            <p>UserName:<input name=\"name\" type=\"text\"/></p>\n" +
						"            <br/>\n" +
						"            <p>Sex:<input name=\"sex\" type=\"text\"/></p>\n" +
						"            <br/>\n" +
						"           Content:<br/><textarea name=\"content\" type=\"textarea\" class=\"text\" cols=\"50\" rows=\"20\"></textarea></p>\n" +
						"        </div>\n" +
						"        <div class=\"col-md-8\">\n" +
						"            <p><button class=\"btn btn-success e_panel_submit\">Submit</button><span class=\"h3\">or</span>\n" +
						"            <button class=\"btn btn-danger e_panel_cancel\">Cancel</button></p>\n" +
						"        </div>\n" +
						"</div>\n";
				return html;
			}
			return null;
			
		}
}

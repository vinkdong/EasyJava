package org.easyjava.web;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easyjava.database.DATABASE;
import org.easyjava.database.DB;
import org.easyjava.database.Model;
import org.easyjava.file.Dict;
import org.easyjava.file.EViewType;
import org.easyjava.file.EXml;
import org.easyjava.util.EOut;
import org.easyjava.util.EString;
import org.easyjava.util.ETool;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EWebView {
	

//	public void initDb(String url){
//		String path = new EXml().urlToPath(url);
//		List<Map<String, String>> fieldList = EXml.read(path, "field");
//		EOut.print(fieldList);
//	}
	@Test
	public void test(){
//		Node node= EXml.getNodeById("/Users/Vink/easyjava/WebContent/layout/base.xml","base_layout");
//		ReadByChild(node);
		if (DB.connection == null) {
			System.out.println("正在连接数据库");
			DATABASE.DATABASE_LOCATION = "127.0.0.1";
			DATABASE.DATABASE_NAME = "easyjava";
			DATABASE.DATABASE_PORT = "5432";
			DATABASE.DATABASE_USER = "ej";
			DATABASE.DATABASE_PASSWORD = "admin";
			DATABASE.DATABASE_TYPE="postgresql";
			DB.init();
		}
		EViewType et = getNodeByType("/Users/Vink/easyjava/WebContent/pages/forum.xml", "tree");
		et.getDict().update("id","10");
		String str = ReadByNode(EXml.getNodeById("base_layout"),et);
	}
	
	/**
	 * 处理t标签及判断加载何种视图
	 * @param node
	 * @param et
	 * @return
	 */
	private String parseTag(Node node,EViewType et) {
		NamedNodeMap attrs = node.getAttributes();
		String html = "";
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			if (attr.getNodeName().equals("load")) {
				Node tNode = EXml.getNodeById(attr.getNodeValue());
				if(tNode.hasChildNodes()){
					html += ReadByNode(tNode,et);
				}
			}
			if (attr.getNodeName().equals("t-if")) {
				//TODO:此处添加判断方法
				//暂时表示条件成立
				if(attr.getNodeValue().equals("type=form")){
					if(node.hasChildNodes()){
						html += ReadByNode(node,et);
					}
				}
				
			}
			if (attr.getNodeName().equals("t-read")) {
				String[] doc = attr.getNodeValue().split("\\.");
				if(doc.length>1&&doc[0].equals("this")){
					if(doc[1].equals("form")){
						if(et.getDict().get("ttype").equalsIgnoreCase("edit")){
							html += loadForm(et,Integer.parseInt(et.getDict().get("id")),"edit");
						}
						else if(et.getDict().get("ttype").equalsIgnoreCase("form")){
							html += loadForm(et,Integer.parseInt(et.getDict().get("id")));
						}
						else if(et.getDict().get("ttype").equalsIgnoreCase("tree")){
							html += loadTree(et);
						}
						else if(et.getDict().get("id").equalsIgnoreCase("")||et.getDict().get("id").equalsIgnoreCase("undefined")){
							html += loadForm(et);
						}
						else{
							html += loadForm(et, Integer.parseInt(et.getDict().get("id")));
						}
					}
					else {
						html += loadTree(et);
					}
				}
				
				
			}
		}
		return html;
	}
	
	public static String loadTree(EViewType et){
		NodeList nodelist = et.getNode().getChildNodes();
		Dict properties = et.getDict();
		List<Map<String, String>> dataset = Self.env.read(properties);
		StringBuilder tbody = new StringBuilder();
		tbody.append("<tbody>");
		List<String> head = new ArrayList<>(); 
		boolean init_head = true;
		if(dataset!=null){
		
		for(Map<String,String> line :dataset){
			tbody.append("<tr data-id="+ETool.get(line, "id")+">\n");
			for(int i=0;i<nodelist.getLength();i++){
				Node node = nodelist.item(i);
				//TODO:权限屏蔽
				if(node.getNodeType()==Node.ELEMENT_NODE){
					if(node.getNodeName().equals("field")){
						NamedNodeMap attr = node.getAttributes();
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("name")) {
								tbody.append("\t<td>");
								//TODO:对象翻译，读取String
								if(init_head){
									head.add(attNode.getNodeValue());
								}
								String model = et.getDict().get("model");
								String type = Model.getType(model, attNode.getNodeValue());
								if(type!=null&&type.equalsIgnoreCase("many2one")){
									String relation = Model.getRelation(model, attNode.getNodeValue());
									String inverse = Model.getInverse(model, attNode.getNodeValue());
									if(ETool.get(line, attNode.getNodeValue()).matches("[0-9]*")&&Integer.parseInt(ETool.get(line, attNode.getNodeValue()))>0){
										Map<String, String> als = Self.env.browse(relation, Integer.parseInt(ETool.get(line, attNode.getNodeValue())));
										tbody.append("<a>"+als.get(inverse)+"</a>");
									}
								}
								else{
									tbody.append(ETool.get(line, attNode.getNodeValue()));
								}
								tbody.append("</td>\n");
							}
						}
					}
					if(node.getNodeName().equals("button")){
						tbody.append("\t<td>");
						NamedNodeMap attr = node.getAttributes();
						tbody.append("\n\t\t<button ");
						String val = "";
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("string")) {								
								//TODO:对象翻译，读取String
								val = attNode.getNodeValue();
								if(init_head){
									head.add(val);
								}
							}
							tbody.append(attNode.getNodeName());
							tbody.append("=\"");
							tbody.append(attNode.getNodeValue());
							tbody.append("\" ");
						}
						tbody.append(">");
						tbody.append(val);
						tbody.append("</button>\n");
						tbody.append("</td>\n");
					}
					
				}
			}
			tbody.append("</tr>\n");
			init_head = false;
		}
		}
		if(et.getDict().get("add").equals("true")){
			tbody.append("<tr>");
			tbody.append("<td colspan='2'>");
			tbody.append("<a class=\"col-sm-10 e_m2m_add\">添加一个项目</a>");
			tbody.append("</td></tr>");
		}
		tbody.append("</tbody>");
		StringBuilder thead = new StringBuilder();
		thead.append("<thead>\n\t<tr>");
		for(String h:head){
			thead.append("<th>");
			thead.append(h);
			thead.append("</th>");
		}
		thead.append("\t</tr>\n</thead>");
		StringBuilder table = new StringBuilder();
		if(et.getDict().get("field").equals("")){
			table.append("<table class=\"table table-responsive\">\n");
		}
		else{
			table.append("<table class=\"table table-responsive\" field='"+et.getDict().get("field")+"'>\n");
		}
		table.append(thead);
		table.append(tbody);
		table.append("</table>");
		return table.toString();
	}
	
	/**
	 * 仅加载不编辑
	 * @param et
	 * @param id
	 * @return
	 */
	public static String loadForm(EViewType et,int id){
		Self.model = et.getDict().get("model");
		Map<String, String> dataset = Self.env.browse(id);
		if(dataset==null){
			return "";
		}
		Node node = et.getNode();
		if(node.hasChildNodes()){
			StringBuilder form = new StringBuilder();
			form.append("\t<form class=\"form-horizontal\" role=\"form\">\n");
			form.append(loadGeneralView(Self.model, node, id, dataset));
			form.append("</form>");
			return form.toString();
		}
		else{
			return "";
		}		
	}
	
	/**
	 * et中无id则新建
	 * @param et
	 * @param type
	 * @return
	 */
	public static String loadForm(EViewType et){
		if(!et.getDict().get("id").equals("")&&!et.getDict().get("id").equals("undefined")){
			return loadForm(et, Integer.parseInt(et.getDict().get("id")));
		}
		else{
			Node node = et.getNode();
			if(node.hasChildNodes()){
				StringBuilder form = new StringBuilder();
				form.append("\t<form class=\"form-horizontal\" role=\"form\">\n");
				form.append(loadCreateView(et.getDict().get("model"), node));
				form.append("<div class=\"col-md-8 col-md-offset-2 \">\n<p><button class=\"btn btn-success e_panel_submit\">Submit</button>");
				form.append("<span class=\"h3\">or</span><button class=\"btn btn-danger e_panel_cancel\">Cancel</button></p></div>");
				form.append("</form>");
				return form.toString();
			}
			else{
				return "";
			}		
		}
	}
	
	/**
	 * 加载form
	 * @param et
	 * @param id
	 * @param type
	 * @return
	 */
	public static String loadForm(EViewType et,int id, String type){
		if (type.equals("edit") && id > 0) {
			Self.model = et.getDict().get("model");
			Map<String, String> dataset = Self.env.browse(id);
			if (dataset == null) {
				return "";
			}
			Node node = et.getNode();
			if (node.hasChildNodes()) {
				StringBuilder form = new StringBuilder();
				form.append("\t<form class=\"form-horizontal\" role=\"form\">\n");
				form.append(loadEditView(Self.model,node,dataset));
				form.append("<div class=\"col-md-8 col-md-offset-2 \">\n<p><button class=\"btn btn-success e_panel_submit\">Submit</button>");
				form.append("<span class=\"h3\">or</span><button class=\"btn btn-danger e_panel_cancel\">Cancel</button></p></div>");
				form.append("</form>");
				return form.toString();
			} else {
				return "";
			}
		}
		else if(type.equals("view")&&id>0){
			return loadForm(et,id);
		}
		else{
			return loadForm(et);
		}
	}
	
	public static String loadGeneralView(String model,Node node,int id,Map<String, String >dataset){
		NodeList fieldList = node.getChildNodes();
		StringBuilder form = new StringBuilder();
		for(int i=0;i<fieldList.getLength();i++){
			Node field = fieldList.item(i);
			if(field.getNodeType()==Node.ELEMENT_NODE){
				if(field.getNodeName().equals("field")){
					NamedNodeMap attr = field.getAttributes();
					String val = "";
					String ext_class = "";
					form.append("\t\t<div ");
					
					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("name")) {
							val = attNode.getNodeValue();
						}
						if (attNode.getNodeName().equals("class")) {
							ext_class = attNode.getNodeValue();
						}
						else{
							form.append(attNode.getNodeName());
							form.append("=\"");
							form.append(attNode.getNodeValue());
							form.append("\" ");
						}
					}
					form.append("class=\"form-group "+ext_class+"\"");
					form.append(">\n<label class=\"col-sm-2 control-label\">");
					form.append(val);
					form.append("</label>\n");
					String type = Model.getType(model, val);
					if(type!=null&&type.equalsIgnoreCase("one2many")){
						String relation = Model.getRelation(model, val);
						form.append("\t\t<div class=\"col-xs-8 e_o2m\" model=\""+relation+"\" field='"+val+"'>");
						for(int cr:Model.getO2mId(model, id, val)){
							form.append("<br/>\n<div class=\"e_o2m col-sm-10\" data-id='"+cr+"' model='"+relation+"'>");
							EViewType e = new EViewType();
							Dict dt = new Dict();
							dt.update("model",relation);
							e.setDict(dt);
							e.setNode(field);
							form.append(loadForm(e, cr));
							form.append("</div>");
							form.append("<div class=\"e_o2m_op col-sm-2\"  data-id='"+cr+"' model='"+relation+"'>");
							if(true){
								form.append("\t\t\t<a class='e_o2m_edit'>编辑</a>");
								form.append("\t\t\t<a class='e_o2m_submit' style='display:none'>提交</a>");
							}
							if(true){
								form.append("\t\t\t<a class='e_o2m_delete'>删除</a>");
							}
							form.append("\t\t</div>");
						}
						if(true){
							form.append("<a class=\"col-sm-10 e_o2m_add\">添加一个项目</a>");
						}
						form.append("</div></div>");
					}
					else{
						//TODO:对象翻译，读取String
						form.append("\t\t\t<div class=\"col-sm-10 e_field\">");
						if(type!=null&&type.equalsIgnoreCase("many2one")){
							String relation = Model.getRelation(model, val);
							String inverse = Model.getInverse(model, val);
							if(ETool.get(dataset, val).matches("[0-9]*")&&Integer.parseInt(ETool.get(dataset, val))>0){
								Map<String, String> als = Self.env.browse(relation, Integer.parseInt(ETool.get(dataset, val)));
								form.append("<a>"+als.get(inverse)+"</a>");
							}
						}
						else if(type!=null&&type.equalsIgnoreCase("many2many")){
							String relation = Model.getRelation(model, val);
							EViewType e = new EViewType();
							Dict dt = new Dict();
							dt.update("model",relation);
							dt.update("add", "true");
							dt.update("field", val);
							dt.update("mid",String.valueOf(id));
							dt.update("mmodel",model);
							e.setDict(dt);
							e.setNode(field);
							form.append(loadTree(e));
						}
						else if(type!=null&&type.equalsIgnoreCase("boolean")){
							if(ETool.get(dataset, val).equals("t")){
								form.append("&radic;");
							}
							else{
								form.append("✘");
							}
						}
						else{
							form.append(ETool.get(dataset, val));
						}
						form.append("\t\t\t</div>\n\t\t</div>\n");
					}
					
				}
				if(field.getNodeName().equals("button")){
					NamedNodeMap attr = field.getAttributes();
					
					String val = "";
					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("string")) {								
							
							val = attNode.getNodeValue();
							}
						}
	
					}
				}
		}
		return form.toString();
	}
	
	public static String loadCreateView(String model,Node node){
		NodeList fieldList = node.getChildNodes();
		StringBuilder form = new StringBuilder();
		for(int i=0;i<fieldList.getLength();i++){
			Node field = fieldList.item(i);
			if(field.getNodeType()==Node.ELEMENT_NODE){
				if(field.getNodeName().equals("field")){
					NamedNodeMap attr = field.getAttributes();
					String val = "";
					String ext_class = "";
					form.append("\t\t<div ");
					
					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("name")) {
							val = attNode.getNodeValue();
						}
						if (attNode.getNodeName().equals("class")) {
							ext_class = attNode.getNodeValue();
						}
						else{
							form.append(attNode.getNodeName());
							form.append("=\"");
							form.append(attNode.getNodeValue());
							form.append("\" ");
						}
					}
					form.append("class=\"form-group "+ext_class+"\">\n");
					String ttype = Model.getType(model, val);
					if(ttype!=null&&(ttype.equalsIgnoreCase("one2many")||ttype.equalsIgnoreCase("many2many"))){
						
					}
					else{
						form.append("<label class=\"col-sm-2 control-label\">");
						//TODO:对象翻译，读取String
						form.append(val);
						form.append("</label>\n");
						form.append("\t\t\t<div class=\"col-sm-10\">\n");
						if(ttype!=null&&ttype.equalsIgnoreCase("many2one")){
							form.append("\t<div class=\"e_m2o\">");
							form.append("\t\t<div class=\"select\">");
							String relation = Model.getRelation(model, val);
							String inverse = Model.getInverse(model, val);
							form.append("\t\t\t<p data-id=\"0\" name='"+val+"'></p>");
							form.append("\t\t\t\t<ul class=\"col-sm-12\">");
							form.append("\t\t\t\t\t\t<li data-id=\"0\" class=\"Selected\"></li>");
							List<Map<String, String>> lilist = Model.read(relation, " 1=1");
							for(Map<String, String> vl:lilist){
								form.append("\t\t\t\t\t\t<li data-id=\""+vl.get("id")+"\">"+vl.get(inverse)+"</li>");
							}
							form.append("\t\t\t\t</ul>");
							form.append("\t\t</div>");
							form.append("\t</div>");
						}
						else{
							form.append("<input type=\"text\" class=\"form-control\"");
							form.append("name='"+val+"'>");
						}
						form.append("\t\t\t</div>\n\t\t</div>\n");
					}
				}
				if(field.getNodeName().equals("button")){
					NamedNodeMap attr = field.getAttributes();							
					String val = "";
					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("string")) {
							val = attNode.getNodeValue();
							}
						}			
					}
				}
		}
		return form.toString();
	}
	
	public static String loadMany2manyModelView(EViewType et){
		NodeList nodelist = et.getNode().getChildNodes();
		Dict properties = et.getDict();
		List<Map<String, String>> dataset = Self.env.read(properties);
		StringBuilder tbody = new StringBuilder();
		List<String> head = new ArrayList<>(); 
		boolean init_head = true;
		for(Map<String,String> line :dataset){
			tbody.append("<tr data-id="+ETool.get(line, "id")+">\n");
			tbody.append("<td data-id="+ETool.get(line, "id")+"><input type=\"checkbox\" aria-label=\"...\">\n</td>");
			for(int i=0;i<nodelist.getLength();i++){
				Node node = nodelist.item(i);
				//TODO:权限屏蔽
				if(node.getNodeType()==Node.ELEMENT_NODE){
					if(node.getNodeName().equals("field")){
						NamedNodeMap attr = node.getAttributes();
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("name")) {
								tbody.append("\t<td>");
								//TODO:对象翻译，读取String
								if(init_head){
									head.add(attNode.getNodeValue());
								}
								String model = et.getDict().get("model");
								String type = Model.getType(model, attNode.getNodeValue());
								if(type!=null&&type.equalsIgnoreCase("many2one")){
									String relation = Model.getRelation(model, attNode.getNodeValue());
									String inverse = Model.getInverse(model, attNode.getNodeValue());
									if(ETool.get(line, attNode.getNodeValue()).matches("[0-9]*")&&Integer.parseInt(ETool.get(line, attNode.getNodeValue()))>0){
										Map<String, String> als = Self.env.browse(relation, Integer.parseInt(ETool.get(line, attNode.getNodeValue())));
										tbody.append("<a>"+als.get(inverse)+"</a>");
									}
								}
								else{
									tbody.append(ETool.get(line, attNode.getNodeValue()));
								}
								tbody.append("</td>\n");
							}
						}
					}
					if(node.getNodeName().equals("button")){
						tbody.append("\t<td>");
						NamedNodeMap attr = node.getAttributes();
						tbody.append("\n\t\t<button ");
						String val = "";
						for (int j = 0; j < attr.getLength(); j++) {
							Node attNode = attr.item(j);
							if (attNode.getNodeName().equals("string")) {								
								//TODO:对象翻译，读取String
								val = attNode.getNodeValue();
								if(init_head){
									head.add(val);
								}
							}
							tbody.append(attNode.getNodeName());
							tbody.append("=\"");
							tbody.append(attNode.getNodeValue());
							tbody.append("\" ");
						}
						tbody.append(">");
						tbody.append(val);
						tbody.append("</button>\n");
						tbody.append("</td>\n");
					}
					
				}
			}
			tbody.append("</tr>\n");
			init_head = false;
		}
		if(et.getDict().get("add").equals("true")){
			tbody.append("<tr>");
			tbody.append("<td colspan='2'>");
			tbody.append("<a class=\"col-sm-10 e_m2m_add\">添加一个项目</a>");
			tbody.append("</td></tr>");
		}
		StringBuilder thead = new StringBuilder();
		thead.append("<thead>\n\t<tr>");
		thead.append("<th class=\"select-all\"><input type=\"checkbox\" aria-label=\"...\">\n</th>");
		for(String h:head){
			thead.append("<th>");
			thead.append(h);
			thead.append("</th>");
		}
		thead.append("\t</tr>\n</thead>");
		StringBuilder table = new StringBuilder();
		table.append("<table class=\"table table-responsive\">\n");
		table.append(thead);
		table.append(tbody);
		table.append("</table>");
		return table.toString();
	}
	
	public static String loadEditView(String model,Node node,Map<String, String>dataset){
		NodeList fieldList = node.getChildNodes();
		StringBuilder form = new StringBuilder();
		for (int i = 0; i < fieldList.getLength(); i++) {
			Node field = fieldList.item(i);
			if (field.getNodeType() == Node.ELEMENT_NODE) {
				if (field.getNodeName().equals("field")) {
					NamedNodeMap attr = field.getAttributes();
					String val = "";
					String ext_class = "";
					form.append("\t\t<div ");

					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("name")) {
							val = attNode.getNodeValue();
						}
						if (attNode.getNodeName().equals("class")) {
							ext_class = attNode.getNodeValue();
						} else {
							form.append(attNode.getNodeName());
							form.append("=\"");
							form.append(attNode.getNodeValue());
							form.append("\" ");
						}
					}
					form.append("class=\"form-group " + ext_class + "\">\n");
					String ttype = Model.getType(model, val);
					if(ttype!=null&&(ttype.equalsIgnoreCase("one2many")||ttype.equalsIgnoreCase("many2many"))){
						
					}
					else{
						form.append("<label class=\"col-sm-2 control-label\">");
						form.append(val);
						form.append("</label>\n");
						form.append("\t\t\t<div class=\"col-sm-10\">");
						// TODO:对象翻译，读取String
						if(ttype!=null&&ttype.equalsIgnoreCase("many2one")){
							form.append("\t<div class=\"e_m2o\">");
							form.append("\t\t<div class=\"select\">");
							String vval = "";
							String relation = Model.getRelation(model, val);
							String inverse = Model.getInverse(model, val);
							if(ETool.get(dataset, val).matches("[0-9]*")&&Integer.parseInt(ETool.get(dataset, val))>0){
								Map<String, String> als = Self.env.browse(relation, Integer.parseInt(ETool.get(dataset, val)));
								vval = als.get(inverse);
							}
							form.append("\t\t\t<p data-id=\"1\" name='"+val+"'>"+vval+"</p>");
							form.append("\t\t\t\t<ul class=\"col-sm-12\">");
							form.append("\t\t\t\t\t\t<li data-id=\"0\" class=\"Selected\"></li>");
							List<Map<String, String>> lilist = Model.read(relation, " 1=1");
							for(Map<String, String> vl:lilist){
								form.append("\t\t\t\t\t\t<li data-id=\""+vl.get("id")+"\">"+vl.get(inverse)+"</li>");
							}
							form.append("\t\t\t\t</ul>");
							form.append("\t\t</div>");
							form.append("\t</div>");
						}
						else if(ttype!=null&&ttype.equalsIgnoreCase("boolean")){
							if(ETool.get(dataset, val).equals("t")){
								form.append("<input type=\"checkbox\" class=\"form-control\"");
								form.append("name='"+val+"' checked='checked'>");
							}
							else{
								form.append("<input type=\"checkbox\" class=\"form-control\"");
								form.append("name='"+val+"'>");
							}
						}
						else{
							form.append("<input type=\"text\" class=\"form-control\"");
							form.append("name='"+val+"' value='"+ETool.get(dataset, val)+"'>");
						}
						
						form.append("\t\t\t</div>\n\t\t</div>\n");
					}	
				}
				if (field.getNodeName().equals("button")) {
					NamedNodeMap attr = field.getAttributes();
					String val = "";
					for (int j = 0; j < attr.getLength(); j++) {
						Node attNode = attr.item(j);
						if (attNode.getNodeName().equals("string")) {
							val = attNode.getNodeValue();
						}
					}

				}
			}
		}
		return form.toString();
	}
	
	/**
	 * 递归式读取节点
	 * @param node
	 * @return
	 */
	private String ReadByNode(Node node,EViewType et){
		NodeList nodelist = node.getChildNodes();
		String html = "";
		
		for(int i=0;i<nodelist.getLength();i++){
			Node child =  nodelist.item(i);
			if(child.getNodeType()==Node.TEXT_NODE){
				html +=child.getNodeValue();
			}
			if(child.getNodeType()==Node.ELEMENT_NODE){
				if(child.getNodeName().equals("t")){
					html+=parseTag(child,et);
				}
				else{
					//TODO : 这里添加上class条件判断
					html+=" <"+child.getNodeName();
					NamedNodeMap attrs = child.getAttributes();
					for(int j=0;j<attrs.getLength();j++){
						Node attr = attrs.item(j);
						html = html + " "+attr.getNodeName()+"=\"";
						if(attr.getNodeValue().equals("$this.id")){
							if(et.getDict().get("id").equals("")){
								html += "none\"";
							}
							else{
								html += et.getDict().get("id")+"\"";
							}	
						}
						else{
							html += attr.getNodeValue()+"\"";
						}
					}
					html +=">\n";
					if(child.hasChildNodes()){
						html += ReadByNode(child,et);
					}
					else{
						if(child.getNodeValue()!=null){
							html += child.getNodeValue();
						}
					}
					html = html + "</"+child.getNodeName()+">\n";
				}
			}
		}
		return html;
	}
	public String  loadPage(String url) {
		String path = new EXml().urlToPath(url);
		if(url.endsWith("/web")){
			path+="/forum.xml";
		}
		
		List<Map<String, String>> fieldList = EXml.read(path, "field");
		if(fieldList ==null)
			return null;
		EViewType et = getNodeByType(path, "tree");
		Dict property = et.getDict();
		String html ="<!DOCTYPE html>\n"
				+ "<html lang='en'>\n"
				+new BaseHTML().getHeader(url);
		if(property.get("layout").equalsIgnoreCase("none")){
			Node root = EXml.getNodeById(EGlobal.PATH+"/layout/base.xml", "base_layout");
			if(root.hasChildNodes()){
				html += this.ReadByNode(root,et);
				return html;
			}
		}
		return  null;
		
	}
	
	public static String loadPage(Dict dict){
		List<Map<String, String>> fieldList = EXml.read(dict.get("path"), "field");
		if(fieldList ==null)
			return null;
		Dict params = dict.getDict("params");
		EViewType et =  new EViewType();
		if(params.get("type").equalsIgnoreCase("tree")){
			et = getNodeByType(dict.get("path"), "tree");
			et.getDict().update("ttype", "tree");
			Node form =  EXml.getNodeById("base_form");
			return new EWebView().ReadByNode(form, et);
		}
		else{
			et = getNodeByType(dict.get("path"), "tree");
			et.getDict().update("id", params.get("id"));
			et.getDict().update("ttype", params.get("type"));
			Node form =  EXml.getNodeById("base_form");
			return new EWebView().ReadByNode(form, et);
		}
	}
	
	public String fillLayer(BufferedReader reader,List<Map<String, String>> fieldList ,String url){
		String line ="";	
		String html ="<!DOCTYPE html>\n"
						+ "<html lang='en'>\n"
						+new BaseHTML().getHeader(url)+new BaseHTML().getNav(url);
		try {
			while ((line=reader.readLine()) !=null) {
				if(line.contains("$field_")){
					List<Map<String, String>> ls = EString.groupByCondition("$field_",line);
					line = "";
					for (Map<String, String> li:ls){
						if(li.get("start")!=null){
							html+=li.get("start");
						};
						if(li.get("field")!=null){
							if(li.get("field").equalsIgnoreCase("header")){
								html += new BaseHTML().getFormHeader("edit", "forum", "1");
//								html += Self.env.search("forum","id = 1"); 
							}
							else for(Map<String, String>fl:fieldList){
								 if(fl.get("name").equals(li.get("field"))){
									html+=fl.get("value");
									break;
								}
							}
							
						};
						if(li.get("end")!=null){
							html+=li.get("end")+"\n";
						}
					}
				}
				else{
					html+= line+"\n";					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return html+"\n</html>";
	};
	
	public static String loadMany2manyView(Dict dict){
		Dict params = dict.getDict("params");
		Node field = getFieldNode(params.get("model"),params.get("field"),"tree");
		String relation = Model.getRelation(params.get("model"), params.get("field"));
		if(field==null){
			return "";
		}
		EViewType et = new EViewType();
		et.setNode(field);
		Dict dt = new Dict();
		dt.update("model", relation);
		et.setDict(dt);
		if(params.get("type").equals("add")){
			String ids_str = params.get("ids");
			if(ids_str.startsWith("[")&&ids_str.endsWith("]")){
				int[] ids = EString.StringToInt(ids_str.substring(1, ids_str.length()-1).split(","));
				Self.env.m2m.mid = params.get("id");
				Self.env.m2m.add(params.get("model"), params.get("field"), ids);
			}
			EViewType e = new EViewType();
			dt.update("add", "true");
			dt.update("field", params.get("field"));
			dt.update("mid",params.get("id"));
			dt.update("mmodel",params.get("model"));
			e.setDict(dt);
			e.setNode(field);
			return loadTree(e);
		}
		return loadMany2manyModelView(et);
	
	}
	public static String loadO2mItem(Dict dict){
		Dict params = dict.getDict("params");
		if(params.get("field").equals("")){
			if(params.get("type").equals("commit")){
				List<String>field_list = new ArrayList<>();
				for(String key:params.getKeys()){
					if(!key.equalsIgnoreCase("model")&&!key.equalsIgnoreCase("type")){
						field_list.add(key);
					}
				}
				return String.valueOf(Self.env.update(params,field_list));
			}
			if(params.get("type").equals("delete")){
				Self.model = params.get("model");
				Self.env.unlink(Integer.parseInt(params.get("id")));
			}
		}
		Node field = getFieldNode(params.get("model"),params.get("field"),"tree");
		if(field!=null&&field.hasAttributes()){
			if(params.get("type").equals("edit")){
				String relation = Model.getRelation(params.get("model"), params.get("field"));
				Self.model = relation;
				Map<String, String> dataset = Self.env.browse(Integer.parseInt(params.get("id")));
				if(dataset==null){
					return "";
				}
				return loadEditView(relation,field,dataset);
			}
			if(params.get("type").equals("view")){
				String relation = Model.getRelation(params.get("model"), params.get("field"));
				Self.model = relation;
				Map<String, String> dataset = Self.env.browse(Integer.parseInt(params.get("id")));
				if(dataset==null){
					return "";
				}
				return loadGeneralView(relation, field, Integer.parseInt(params.get("id")), dataset);
			}
			if(params.get("type").equals("add")){
				String relation = Model.getRelation(params.get("model"), params.get("field"));
				StringBuilder form = new StringBuilder();
				form.append("<div class=\"e_o2m col-sm-10\" data-id=\"none\" model=\""+relation+"\">\n");
				form.append(loadCreateView(relation, field));
				form.append("</div>");
				form.append("<div class=\"e_o2m_op col-sm-2\"  data-id='none' model='"+relation+"'>");
				if(true){
					form.append("\t\t\t<a class='e_o2m_edit' style='display:none'>编辑</a>");
					form.append("\t\t\t<a class='e_o2m_submit'>提交</a>");
				}
				if(true){
					form.append("\t\t\t<a class='e_o2m_delete'>删除</a>");
				}
				form.append("\t\t</div>");
				
				return form.toString();
			}
		}
		return "";
	}
	
	/**
	 * 根据模型和字段获取该字段的Node
	 * @param dict
	 * @return
	 */
	public static Node getFieldNode(String model,String field,String type){
		EViewType et = getNodeByType(EGlobal.PATH+"/pages/"+model+".xml", type);
		Node form = et.getNode();
		if(form.hasChildNodes()){
			NodeList fieldNodeList = form.getChildNodes();
			for(int i=0;i<fieldNodeList.getLength();i++){
				Node fieldNode = fieldNodeList.item(i);
				if(fieldNode.getNodeType()==Node.ELEMENT_NODE){
					NamedNodeMap attrs = fieldNode.getAttributes();
					for(int j=0;j<attrs.getLength();j++){
						Node attrNode = attrs.item(j);
						if(attrNode.getNodeName().equals("name")&&attrNode.getNodeValue().equalsIgnoreCase(field)){
							return fieldNode;
						}
					}
				}
			}
		}
		return null;
		
	}
	/**
	 * 读取tree 或from 的视图node
	 * @param path
	 * @param type
	 * @return
	 */
	public static EViewType getNodeByType(String path,String type){
		NodeList nodeList = EXml.getNodeList(path);
		EViewType et = new EViewType();
		Dict dt = new Dict();
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE&&node.getNodeName().equalsIgnoreCase("rec")){
				if(node.hasChildNodes()){
					NodeList nd = node.getChildNodes();
					for(int j=0;j<nd.getLength();j++){
						Node n = nd.item(j);
						if(n.getNodeType()==Node.ELEMENT_NODE&&n.hasChildNodes()){
							Node  cr = n.getAttributes().getNamedItem("name");
							if(cr!=null&&!cr.getNodeValue().equalsIgnoreCase("view")){
								dt.update(cr.getNodeValue(),n.getFirstChild().getNodeValue());
							}
							NodeList ndls = n.getChildNodes();
							for(int k=0;k<ndls.getLength();k++){
								Node nds = ndls.item(k);
								if(nds.getNodeType()==Node.ELEMENT_NODE&&nds.getNodeName().equalsIgnoreCase(type)){
									et.setNode(nds);
									et.setDict(dt);
									return et;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

}

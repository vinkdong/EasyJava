/***
 * GPL3.0
 */
package org.easyjava.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easyjava.database.DATABASE;
import org.easyjava.database.DB;
import org.easyjava.database.Model;
import org.easyjava.file.Dict;

/**
 * Servlet Filter implementation class filter
 */
@WebFilter("/filter")
public class EFilter implements Filter {

    /**
     * Default constructor. 
     */
    public EFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String CONTENT_TYPE = "text/html; charset=utf-8";
		response.setContentType(CONTENT_TYPE);
		HttpServletRequest hq = (HttpServletRequest) request;
		HttpServletResponse hr = (HttpServletResponse) response;
		String  url = hq.getServletPath();
		request.setCharacterEncoding("utf-8");
		hr.setCharacterEncoding("utf-8");   
		
		if(url.matches(".*(\\.css|\\.js|upgrade|\\.png|\\.jpg|\\.svg)")){
			chain.doFilter(request, response);
		}
		else{
			PrintWriter out = response.getWriter();
			if(url.endsWith("_rpc_add")){
	        	int id  = Self.env.add(hq);
	        	out.print(id);        	
	        }
			else if(url.endsWith("_rpc_loadview")){
				Dict dict = Self.env.rpcToDict(hq);
				String html = EWebView.loadPage(dict);
				out.print(html);
			}
	        else if(url.endsWith("_rpc_read")){
	        	String form  = Self.env.read(hq);	
	        	out.print(form);        	
	        }
			else if(url.matches(".*index\\.(jsp|gsp|html|htm|asp|php)")){
				hr.sendRedirect("index.esp");
			}
			else{
				if (DB.connection == null) {
//					new EWebView().initDb(url);
					System.out.println("正在连接数据库");
					DATABASE.DATABASE_LOCATION = "127.0.0.1";
					DATABASE.DATABASE_NAME = "easyjava";
					DATABASE.DATABASE_PORT = "5432";
					DATABASE.DATABASE_USER = "ej";
					DATABASE.DATABASE_PASSWORD = "admin";
					DATABASE.DATABASE_TYPE="postgresql";
					DB.init();
				}
				
				String[] fields = new String[3] ;
				fields[0] = "field=name,string=User,type=Text";
				fields[1] = "field=sex,string=Sex,type=Text";
				fields[2] = "field=salary,string=Salary,type=float";
//			    Model.add("hr_employee",fields);
			    
//			    One2Many
				String[]  comment = new String[3];
				comment[0] = "field=name,string=User,type=Text";
				comment[1] = "field=date,string=Date,type=Date";
				comment[2] = "field=content,string=Comment,type=Text";
				Model.add("comment", comment);
				
				String[]  forum = new String[4];
				forum[0] = "field=name,string=Name,type=Text";
				forum[1] = "field=date,string=Date,type=Date";
				forum[2] = "field=content,string=Content,type=Text";
				forum[3] = "field=comment,string=Comment,type=one2many:comment";
				Model.add("forum", forum);				
//				Mo.define("ed",null, true);
				
//				out.print(new baseHTML().completeHTML(url));	
				if (new EWebView().loadPage(url)!=null){
					out.print(new EWebView().loadPage(url));
				}
				else
					out.print(new BaseHTML().completeHTML(url));
			}

		}
		
//		new upgrade().doPost((HttpServletRequest) request, (HttpServletResponse) response);

		// pass the request along the filter chain
//		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
		// TODO Auto-generated method stub
	}

}

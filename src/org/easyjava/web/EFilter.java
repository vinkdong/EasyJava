package org.easyjava.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easyjava.database.DATABASE;
import org.easyjava.database.DB;
import org.easyjava.database.Model;

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
		        
        if(url.endsWith("_rpc")){
        	Self.env.add(hq);	
        }
		if(url.matches(".*(\\.css|\\.js|upgrade|\\.png|\\.jpg|\\.svg)")){
			chain.doFilter(request, response);
		}
		else if(url.matches(".*index\\.(jsp|gsp|html|htm|asp|php)")){
			hr.sendRedirect("index.esp");
		}
		else{
			if (DB.connection == null) {
				System.out.println("正在连接数据库");
				DATABASE.DATABASE_LOCATION = "127.0.0.1";
				DATABASE.DATABASE_NAME = "easyjava";
				DATABASE.DATABASE_PORT = "5432";
				DATABASE.DATABASE_USER = "odoo";
				DATABASE.DATABASE_PASSWORD = "odoo";
				DATABASE.DATABASE_TYPE="postgresql";
				DB.init();
			}
			
			String[] fields = new String[3] ;
			fields[0] = "field=name,string=User,type=Text";
			fields[1] = "field=sex,string=Sex,type=Text";
			fields[2] = "field=salary,string=Salary,type=float";
//		    Model.add("hr_employee",fields);
		    
//		    One2Many
			String[]  comment = new String[3];
			comment[0] = "field=name,string=User,type=Text";
			comment[1] = "field=date,string=Date,type=Date";
			comment[2] = "field=content,string=Comment,type=Text";
			Model.add("comment", comment);
			
			String[]  one2many = new String[4];
			one2many[0] = "field=name,string=Name,type=Text";
			one2many[1] = "field=date,string=Date,type=Date";
			one2many[2] = "field=content,string=Content,type=Text";
			one2many[3] = "field=comment,string=Comment,type=one2many:comment";
			Model.add("forum", one2many);
			
			
			
			
//			Mo.define("ed",null, true);
			PrintWriter out = response.getWriter();
			request.setCharacterEncoding("utf-8");
			hr.setCharacterEncoding("utf-8");
//			out.print(new baseHTML().completeHTML(url));	
			if (new InitPage().loadPage(url)!=null){
				out.print(new InitPage().loadPage(url));
			}
			else
				out.print(new BaseHTML().completeHTML(url));
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

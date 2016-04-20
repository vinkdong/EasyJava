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
			else if(url.endsWith("_rpc_o2m")){
				Dict dict = Self.env.rpcToDict(hq);
				String html = EWebView.loadO2mItem(dict);
				out.println(html);
			}
			else if(url.endsWith("_rpc_m2m")){
				Dict dict = Self.env.rpcToDict(hq);
				String html = EWebView.loadMany2manyView(dict);
				out.println(html);
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
				
				String[] ir_groups = new String[8];
				ir_groups[0] = "field=name,string=Name,type=Text";
				ir_groups[1] = "field=name,string=Name,type=Text";
				this.createModel();			
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
	
	public void createModel(){
		new Model().createModel();
	}

}

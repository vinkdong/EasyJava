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

/**
 * Servlet Filter implementation class filter
 */
@WebFilter("/filter")
public class filter implements Filter {

    /**
     * Default constructor. 
     */
    public filter() {
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
		// place your code here
		String CONTENT_TYPE = "text/html; charset=utf-8";
		response.setContentType(CONTENT_TYPE);
		HttpServletRequest hq = (HttpServletRequest) request;
		HttpServletResponse hr = (HttpServletResponse) response;
		String  url = hq.getServletPath();
		
//		System.out.println(hq.getContextPath());	//  /EasyJava
//		System.out.println(hq.getLocalAddr());		 //   127.0.0.1
//		System.out.println(hq.getLocalName());	 //	localhost
//		System.out.println(hq.getMethod());			//		GET
//		System.out.println(hq.getPathInfo());		//		null
//		System.out.println(hq.getPathTranslated());// null
//		System.out.println(hq.getProtocol());			//		HTTP/1.1
//		System.out.println(hq.getQueryString());	//		null
//		System.out.println(hq.getRemoteAddr());//		127.0.0.1
//		System.out.println(hq.getRemoteHost());//		127.0.0.1		
//		System.out.println(hq.getRemotePort());//		54641
//		System.out.println(hq.getReader());			//		org.apache.catalina.connector.CoyoteReader@4bc4dafb
//		System.out.println(hq.getScheme());			//		http
//		System.out.println(hq.getServletPath());	//		/aa
//		System.out.println(hq.getServletContext());//	org.apache.catalina.core.ApplicationContextFacade@5a51ccf2
//        System.out.println("*******************------------------******************");
		if(url.matches(".*(\\.css|\\.js|upgrade|\\.png|\\.jpg|\\.svg)")){
			chain.doFilter(request, response);
		}
		else if(url.matches(".*index\\.(jsp|gsp|html|htm|asp|php)")){
			hr.sendRedirect("index.esp");
		}
		else{
			
			PrintWriter out = response.getWriter();
			request.setCharacterEncoding("utf-8");
			hr.setCharacterEncoding("utf-8");
//			out.print(new baseHTML().completeHTML(url));	
			if (new initPage().loadPage(url)!=null){
				out.print(new initPage().loadPage(url));
			}
			else
				out.print(new baseHTML().completeHTML(url));
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

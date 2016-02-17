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
		HttpServletRequest hq = (HttpServletRequest) request;
		HttpServletResponse hr = (HttpServletResponse) response;
		
		if(hq.getRequestURI().matches(".*(\\.css|\\.js|upgrade)")){
			chain.doFilter(request, response);
		}
		else if(hq.getRequestURI().matches(".*/upgrade")){
			hr.sendRedirect("upgrade");
		}
		else{
			PrintWriter out = response.getWriter();
			request.setCharacterEncoding("utf-8");
			out.print(new baseHTML().completeHTML());	
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

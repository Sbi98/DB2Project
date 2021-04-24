package db2project.controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.print("Login checker filter executing ...\n");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String loginpath = req.getServletContext().getContextPath() + "/index.html";

		HttpSession s = req.getSession();
		if (s.isNew() || s.getAttribute("user") == null) {
			res.sendRedirect(loginpath);
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	public LoginFilter() { }
	public void destroy() {	}
	public void init(FilterConfig fConfig) throws ServletException { }

}

package db2project.utils;

import db2project.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckAdminPrivileges implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.print("Checking admin permissions...\n");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        User u = (User) req.getSession().getAttribute("user");
        if (!u.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not an admin!");
            return;
        }
        chain.doFilter(request, response);
    }

    public CheckAdminPrivileges() { }
    public void destroy() {	}
    public void init(FilterConfig fConfig) throws ServletException { }
}

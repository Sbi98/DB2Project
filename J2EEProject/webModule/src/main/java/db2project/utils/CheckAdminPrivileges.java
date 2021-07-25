package db2project.utils;

import db2project.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckAdminPrivileges implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession s = req.getSession();
        //If there user is not logged or there's a new session, go to index
        if (s.isNew() || s.getAttribute("user") == null) {
            System.err.print("\nLogin permissions violated\n");
            res.sendRedirect(req.getServletContext().getContextPath() + "/index.html");
            return;
        } else { //Else check if the user in the session is an admin
            User u = (User) req.getSession().getAttribute("user");
            if (!u.isAdmin()) {
                System.err.print("\nAdmin permissions violated\n");
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not an admin!");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public CheckAdminPrivileges() { }
    public void destroy() {	}
    public void init(FilterConfig fConfig) throws ServletException { }
}

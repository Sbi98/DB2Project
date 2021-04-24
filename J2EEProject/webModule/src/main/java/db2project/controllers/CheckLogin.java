package db2project.controllers;

import db2project.entity.User;
import db2project.services.UserService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/UserService")
    private UserService usrService;

    public CheckLogin() {
        super();
    }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //templateResolver.setPrefix("/WEB-INF/");
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usrn = null;
        String pwd = null;
        try {
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }
        try {
            User user = usrService.checkCredentials(usrn, pwd);
            request.getSession().setAttribute("user", user);
            response.sendRedirect(getServletContext().getContextPath()+"/GoToHomePage");
        } catch (Exception e) {
            e.printStackTrace();
            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("errorMsg", e.getMessage());
            templateEngine.process("/index", ctx, response.getWriter());
            return;
        }
    }

    public void destroy() { }
}

package db2project.controllers;

import db2project.entity.User;
import db2project.exceptions.UniqueConstraintViolation;
import db2project.services.UserService;

import java.io.IOException;
import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet(name = "RegisterUser", value = "/RegisterUser")
public class RegisterUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/UserService")
    private UserService usrService;

    public RegisterUser() {
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
        String usrn, pwd, email;
        try {
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            email = StringEscapeUtils.escapeJava(request.getParameter("email"));
            if (usrn == null || pwd == null || email == null || usrn.isEmpty() || pwd.isEmpty() || email.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }
        try {
            User user = usrService.registerUser(usrn,pwd,email);
            request.getSession().setAttribute("user", user);
            response.sendRedirect(getServletContext().getContextPath()+"/user/GoToUserHomePage");
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("\n------------\n"+e.getMessage()+"\n------------\n");
            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("errorMsg2", e.getMessage());
            templateEngine.process("index", ctx, response.getWriter());
        }
    }

    public void destroy() { }
}

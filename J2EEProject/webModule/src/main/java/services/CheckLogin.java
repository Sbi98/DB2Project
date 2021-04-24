package services;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    //@EJB(name = "it.polimi.db2.teaching.services/TeacherService")
    //private TeacherService usrService;

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
            // for debugging only e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }
        Integer userId = null;
        try {
            System.err.println("TODO");
            //TODO
            // query db to authenticate for user
            //userId = usrService.checkCredentials(usrn, pwd);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }
        // If the user exists, add info to the session and go to home page, otherwise
        // show login page with error message
        if (userId == null) {
            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("errorMsg", "Incorrect username or password");
            templateEngine.process("/index", ctx, response.getWriter());
        } else {
            request.getSession().setAttribute("userId", userId);
            response.sendRedirect(getServletContext().getContextPath()+"/GoToHomePage");
        }

    }

    public void destroy() { }
}

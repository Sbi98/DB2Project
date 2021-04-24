package db2project.controllers;
import db2project.entity.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/GoToCreationPage")
public class GoToCreationPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public GoToCreationPage() {
        super();
    }

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/admin/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            User u = (User) request.getSession().getAttribute("user");
            if(u.isAdmin()){
                final WebContext ctx = new WebContext(request, response, getServletContext());
                templateEngine.process("creationPage", ctx, response.getWriter());
            } else {
                System.out.println("L'utente non è un admin!");
                response.sendRedirect(getServletContext().getContextPath()+"/GoToUserHomePage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() { }
}
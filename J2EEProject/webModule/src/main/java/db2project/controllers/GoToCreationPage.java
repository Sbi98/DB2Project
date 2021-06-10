package db2project.controllers;

import db2project.services.CreationService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "GoToCreationPage", value = "/admin/GoToCreationPage")
public class GoToCreationPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    public GoToCreationPage() {
        super();
    }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/admin/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Se si proviene dalla homepage dell'admin, viene reistanziato il creationService.
        try {
            CreationService creationService = (CreationService) new InitialContext().lookup("java:global/package/CreationService!db2project.services.CreationService");
            request.getSession().setAttribute("creationService", creationService);
            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("creationService", creationService);
            templateEngine.process("creationPage", ctx, response.getWriter());
        } catch (NamingException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CreationService not found\n"+e.getMessage());
        }
    }

    public void destroy() { }
}
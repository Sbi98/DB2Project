package db2project.controllers;

import db2project.services.CreationService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "GoToCreationPage", value = "/admin/GoToCreationPage")
public class GoToCreationPage extends HttpServlet {
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
        CreationService creationService = new CreationService();
        request.getSession().setAttribute("creationService", creationService);
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("creationService", creationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }

    public void destroy() { }
}
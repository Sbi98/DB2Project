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

@WebServlet(name = "AddQuestionCreationPage", value = "/admin/AddQuestionCreationPage")
public class AddQuestionCreationPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public AddQuestionCreationPage() {
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
        CreationService newProductService = (CreationService) request.getSession().getAttribute("newProductService");
        if (newProductService == null) {
            // Se non esiste un reviewService, viene creato e salvato nella sessione
            newProductService = new CreationService();
            request.getSession().setAttribute("newProductService", newProductService);
        }
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("creationService", newProductService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }

    public void destroy() { }
}

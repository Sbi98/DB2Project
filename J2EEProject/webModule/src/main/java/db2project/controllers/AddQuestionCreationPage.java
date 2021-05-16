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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreationService newCreationService = (CreationService) request.getSession().getAttribute("newProductService");
        if (newCreationService == null) {
            // Se non esiste un creationService, viene creato e salvato nella sessione
            newCreationService = new CreationService();
            request.getSession().setAttribute("newProductService", newCreationService);
        }

        newCreationService.incrementNumberOfQuestions(1);
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("creationService", newCreationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }

    public void destroy() { }
}

package db2project.controllers;

import db2project.entity.Product;
import db2project.entity.User;
import db2project.services.CreationService;
import db2project.services.NewReviewService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveQuestionCreationPage", value = "/admin/RemoveQuestionCreationPage")
public class RemoveQuestionCreationPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public RemoveQuestionCreationPage() {
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
        CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
        if(creationService != null) {
            creationService.removeQuestionAt(Integer.parseInt(request.getParameter("index")));
        } else
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Contesto non valido: non è " +
                    "presente un creationService nella sessione");

        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("creationService", creationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }
}

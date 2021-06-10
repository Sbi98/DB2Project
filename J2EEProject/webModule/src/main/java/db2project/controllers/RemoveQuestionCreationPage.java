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

@WebServlet(name = "RemoveQuestionCreationPage", value = "/admin/RemoveQuestionCreationPage")
public class RemoveQuestionCreationPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
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
        if (creationService == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Wrong workflow. CreationService not in session");
        }
        creationService.removeQuestionAt(Integer.parseInt(request.getParameter("index")));
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("creationService", creationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }
}

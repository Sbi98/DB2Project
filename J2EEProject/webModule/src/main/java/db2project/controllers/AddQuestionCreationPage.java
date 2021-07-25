package db2project.controllers;

import db2project.services.CreationService;
import db2project.services.ProductService;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@MultipartConfig
@WebServlet(name = "AddQuestionCreationPage", value = "/admin/AddQuestionCreationPage")
public class AddQuestionCreationPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

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
        final WebContext ctx = new WebContext(request, response, getServletContext());
        CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
        if (creationService == null) {
            // Se non esiste un creationService il workflow non è quello giusto (viene creato nella GoToCreationPage)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AddQuestionCreationPage Error: Invalid session!");
            return;
        }
        try {
            String newQuestion = request.getParameter("question");
            if(!newQuestion.matches(".*[a-zA-Z]+.*")) {
                throw new Exception("The new question must contain text!");
            }
            creationService.addQuestion(newQuestion);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            ctx.setVariable("displayMsg", e.getMessage());
            //response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        ctx.setVariable("creationService", creationService);
        templateEngine.process("creationPage", ctx, response.getWriter());
    }

    public void destroy() { }
}

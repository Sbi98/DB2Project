package db2project.controllers;

import db2project.services.CreationService;
import db2project.services.ProductService;
import db2project.utils.Utils;
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
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        try {
            CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
            StringBuilder displayMsg = new StringBuilder();
            if (creationService == null) {
                // Se non esiste un creationService il workflow non è quello giusto (viene creato nella GoToCreationPage)
                throw new Exception("Invalid Session!");
            }
            String newQuestion = request.getParameter("question");
            if (newQuestion == null || !newQuestion.contains("[a-zA-Z]+")){
                displayMsg.append("The question must contain \n");
            }
            if (displayMsg.length() > 0) {
                ctx.setVariable("displayMsg", displayMsg.toString());
            } else {
                creationService.addQuestion(newQuestion);
            }
            ctx.setVariable("creationService", creationService);
            templateEngine.process("creationPage", ctx, response.getWriter());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AddQuestionCreationPage Error: " + e.getMessage());
        }
    }

    public void destroy() { }
}

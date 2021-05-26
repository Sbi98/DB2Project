package db2project.controllers;

import db2project.services.CreationService;
import db2project.utils.ImageUtils;
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

@MultipartConfig
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
        try {
            CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
            if (creationService == null) {
                // Se non esiste un creationService, viene creato e salvato nella sessione
                throw new Exception("Invalid Session!");
            }
            if(creationService.getProductName() == null) {
                creationService.setProductName(request.getParameter("name"));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                creationService.setDate(sdf.parse(request.getParameter("date")));

                Part imgFile = request.getPart("picture");
                InputStream imgContent = imgFile.getInputStream();
                creationService.setImgByteArray(ImageUtils.readImage(imgContent));

                if (creationService.getProductName() == null || creationService.getProductName().isEmpty() || creationService.getImgByteArray().length == 0) {
                    throw new Exception("Invalid Product parameters");
                }
            } else { // Se i dettagli del prodotto sono già stati inseriti:
                creationService.addQuestion(request.getParameter("question"));
            }

            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("creationService", creationService);
            templateEngine.process("creationPage", ctx, response.getWriter());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AddQuestionCreationPage Error: " + e.getMessage());
        }
    }

    public void destroy() { }
}

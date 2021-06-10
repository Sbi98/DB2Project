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
@WebServlet(name = "ConfirmProductCreationPage", value = "/admin/AddQuestionCreationPage")
public class ConfirmProductCreationPage extends HttpServlet {
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public ConfirmProductCreationPage() {
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
            StringBuffer displayMsg = new StringBuffer();
            if (creationService == null) {
                // Se non esiste un creationService il workflow non è quello giusto (viene creato nella GoToCreationPage)
                throw new Exception("Invalid Session!");
            }
            String name = request.getParameter("name");
            if (name == null || name.equals(""))
                displayMsg.append("You must provide a name for the product\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(request.getParameter("date"));
            if (Utils.isBeforeToday(date)) {
                displayMsg.append("You cannot provide a date before today\n");
            }
            if (prodService.getProductOfDay(date) != null) {
                displayMsg.append("There is already a product for the selected day\n");
            }
            Part imgFile = request.getPart("picture");
            if (imgFile == null || imgFile.getSize() == 0) {
                displayMsg.append("You must provide an image for the product\n");
            }
            if (imgFile == null) {
                throw new Exception("Invalid image file!");
            }
            InputStream imgContent = imgFile.getInputStream();
            if (displayMsg.length() > 0) {
                ctx.setVariable("displayMsg", displayMsg.toString());
            } else {
                creationService.setDate(date);
                creationService.setProductName(name);
                creationService.setImgByteArray(Utils.readImage(imgContent));
            }

            ctx.setVariable("creationService", creationService);
            templateEngine.process("creationPage", ctx, response.getWriter());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "AddQuestionCreationPage Error: " + e.getMessage());
        }
    }

    public void destroy() { }
}

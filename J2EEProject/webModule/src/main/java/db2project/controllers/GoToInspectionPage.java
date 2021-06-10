package db2project.controllers;

import db2project.entity.Product;
import db2project.services.ProductService;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

@WebServlet(name = "GoToInspectionPage", value = "/admin/GoToInspectionPage")
public class GoToInspectionPage extends HttpServlet {
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public GoToInspectionPage() { super(); }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/admin/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext());
        templateEngine.process("inspectionPage", ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            if (request.getSession().getAttribute("products") == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GoToInspectionPage POST: Cannot find " +
                        "the products inside session!\n");
            } else {
                Date selectedDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(request.getParameter("date"));
                //System.out.println("Selected date:" + selectedDate.toString());
                final WebContext ctx = new WebContext(request, response, getServletContext());
                ctx.setVariable("selectedDate", selectedDate);
                ctx.setVariable("selectedProduct", prodService.getProductOfDay(selectedDate));
                templateEngine.process("inspectionPage", ctx, response.getWriter());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GoToInspectionPage POST: " + e.getMessage());
        }
    }

    public void destroy() { }

}

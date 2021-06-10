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
import java.util.Date;
import java.util.List;

@WebServlet(name = "GoToDeletionPage", value = "/admin/GoToDeletionPage")
public class GoToDeletionPage extends HttpServlet {
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService productService;

    public GoToDeletionPage() {
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
        List<Product> products = productService.getAllProductsBeforeToday();
        // List<Product> products = productService.getAllProducts();
        request.getSession().setAttribute("products", products);
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("products", products);
        templateEngine.process("deletionPage", ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getSession().getAttribute("products") == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GoToDeletionPage POST: Cannot find " +
                        "the products inside session!\n");
            } else {
                String selectedProductId = request.getParameter("selectedProduct");
                final WebContext ctx = new WebContext(request, response, getServletContext());
                ctx.setVariable("products", request.getSession().getAttribute("products"));
                ctx.setVariable("selectedProduct", productService.getProduct(Integer.parseInt(selectedProductId)));
                templateEngine.process("deletionPage", ctx, response.getWriter());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GoToDeletionPage POST: " + e.getMessage());
        }
    }

    public void destroy() { }
}
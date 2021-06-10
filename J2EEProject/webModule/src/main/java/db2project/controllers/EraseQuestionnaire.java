package db2project.controllers;

import db2project.services.ProductService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "EraseQuestionnaire", value = "/admin/EraseQuestionnaire")
public class EraseQuestionnaire extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    ProductService productService;

    public EraseQuestionnaire() {
        super();
    }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/user/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("product");
        if (productService.eraseQuestionnaireData(Integer.parseInt(productId)))
            response.sendRedirect(getServletContext().getContextPath()+"/admin/GoToAdminHomePage");
        else
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot erase the questionnaire data of the selected product");
    }

    public void destroy() { }
}
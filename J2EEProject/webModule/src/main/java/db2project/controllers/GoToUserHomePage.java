package db2project.controllers;
import db2project.entity.Product;
import db2project.entity.User;
import db2project.services.ProductService;
import db2project.services.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "GoToUserHomePage", value = "/user/GoToUserHomePage")
public class GoToUserHomePage extends HttpServlet {
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public GoToUserHomePage() { super(); }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/user/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product p = prodService.getProductOfToday();
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("pOfTheDay", p);
        templateEngine.process("home", ctx, response.getWriter());
    }

    public void destroy() { }
}

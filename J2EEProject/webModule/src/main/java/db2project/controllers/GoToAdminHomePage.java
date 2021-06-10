package db2project.controllers;

import db2project.entity.Product;
import db2project.services.CreationService;
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


@WebServlet(name = "GoToAdminHomePage", value = "/admin/GoToAdminHomePage")
public class GoToAdminHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public GoToAdminHomePage() { super(); }

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
        // Se l'admin è tornato alla home mentre stava creando un prodotto va rimosso il creationService
        if (creationService != null) {
            creationService.remove();
            request.getSession().removeAttribute("creationService");
        }
        Product productOfTheDay = prodService.getProductOfToday(); //TODO forse dalla traccia non va caricato qui
        request.getSession().setAttribute("pOfTheDay", productOfTheDay);
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("pOfTheDay", productOfTheDay);
        templateEngine.process("adminHome", ctx, response.getWriter());
    }

    public void destroy() { }
}

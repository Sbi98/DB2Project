package db2project.controllers;

import db2project.entity.Product;
import db2project.entity.Review;
import db2project.entity.User;
import db2project.services.NewReviewService;
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


@WebServlet(name = "GoToUserHomePage", value = "/user/GoToUserHomePage")
public class GoToUserHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
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
        final WebContext ctx = new WebContext(request, response, getServletContext());
        NewReviewService rService = (NewReviewService) request.getSession().getAttribute("rService");
        if (rService != null) {
            // Se l'utente è tornato alla home durante l'inserimento della review cancellala rimuovendo il rService
            // dalla session.
            rService.remove();
            request.getSession().removeAttribute("rService");
        }
        Product p = prodService.getProductOfToday();
        request.getSession().setAttribute("pOfTheDay", p);
        // Recupera, se esiste, la review dell'utente al prodotto del giorno
        Review r = (Review) request.getSession().getAttribute("pOfTheDayReview");
        if (r == null && p != null) {
            r = prodService.findReviewOfUser(p.getId(), ((User) request.getSession().getAttribute("user")).getId());
            request.getSession().setAttribute("pOfTheDayReview", r);
        }
        ctx.setVariable("pOfTheDay", p);
        ctx.setVariable("pOfTheDayReview", r);
        // La pagina 'home.html' viene creata e mostrata all'utente
        templateEngine.process("home", ctx, response.getWriter());
    }

    public void destroy() { }
}

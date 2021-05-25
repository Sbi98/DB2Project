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
        if (rService != null) { //se l'utente è tornato alla home mentre stava facendo la review cancellala
            rService.remove();
            request.getSession().removeAttribute("rService");
        }
        Product p = (Product) request.getSession().getAttribute("pOfTheDay");
        if (p == null) {
            p = prodService.getProductOfToday();
            request.getSession().setAttribute("pOfTheDay", p);
        }
        Review r = (Review) request.getSession().getAttribute("pOfTheDayReview");
        if (r == null && p != null) {
            r = prodService.findReview(p.getId(), ((User) request.getSession().getAttribute("user")).getId());
            request.getSession().setAttribute("pOfTheDayReview", r);
        }
        ctx.setVariable("pOfTheDay", p);
        ctx.setVariable("pOfTheDayReview", r);
        // La pagina 'home.html' viene creata e mostrata all'utente
        //TODO un bel check su se la tabella domanda-risposta mostra tutte le risposte di una stessa domanda sotto quella domanda
        templateEngine.process("home", ctx, response.getWriter());
    }

    public void destroy() { }
}

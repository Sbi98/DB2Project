package db2project.controllers;

import db2project.entity.MAnswer;
import db2project.entity.MQuestion;
import db2project.entity.Product;
import db2project.entity.User;
import db2project.services.NewReviewService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "GoToNewReviewPage", value = "/user/GoToNewReviewPage")
public class GoToNewReviewPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public GoToNewReviewPage() {
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
        Product p = (Product) request.getSession().getAttribute("pOfTheDay");
        // Se non è presente nella sessione un prodotto del giorno, restituisce un errore
        if (p == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no product of the day");
            return;
        }
        // Se è presente un prodotto del giorno, ottiene dalla sessione l'attributo 'rService'
        NewReviewService rService = (NewReviewService) request.getSession().getAttribute("rService");
        if (rService == null) {
            // Se non esiste un reviewService, viene creato e salvato nella sessione
            rService = new NewReviewService(p, (User) request.getSession().getAttribute("user"));
            request.getSession().setAttribute("rService", rService);
        }
        //rService è sicuramente diverso da null
        rService.firstPage();
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("pOfTheDay", p);
        ctx.setVariable("rService", rService);
        templateEngine.process("review", ctx, response.getWriter());
    }

    public void destroy() { }
}

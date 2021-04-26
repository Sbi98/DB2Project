package db2project.controllers;

import db2project.entity.MAnswer;
import db2project.entity.MQuestion;
import db2project.entity.Product;
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


@WebServlet(name = "GoToNewReview2ndPage", value = "/user/GoToNewReview2ndPage")
public class GoToNewReview2ndPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public GoToNewReview2ndPage() {
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product p = (Product) request.getSession().getAttribute("pOfTheDay");
        NewReviewService rService = (NewReviewService) request.getSession().getAttribute("rService");
        if (p == null | rService == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong workflow. Are you trying to cheat?");
            return;
        }
        rService.secondPage();
        for (MQuestion q : p.getQuestions()) {
            new MAnswer(rService.getReview(), q, request.getParameter("q"+q.getId()));
        }
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("currentpage", rService.getCurrentPage());
        ctx.setVariable("pOfTheDay", p);
        templateEngine.process("review", ctx, response.getWriter());
    }

    public void destroy() { }
}

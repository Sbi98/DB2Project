package db2project.controllers;

import db2project.entity.Product;
import db2project.entity.User;
import db2project.services.NewReviewService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "GoToNewReviewPage", value = "/user/GoToNewReviewPage")
public class GoToNewReviewPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
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
        if (p == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong workflow. No product of the day in session");
            return;
        }
        NewReviewService rService = (NewReviewService) request.getSession().getAttribute("rService");
        if (rService == null) {
            try {
                rService = (NewReviewService) new InitialContext().lookup("java:global/package/NewReviewService!db2project.services.NewReviewService");
                rService.createReview(p, (User) request.getSession().getAttribute("user"));
                request.getSession().setAttribute("rService", rService);
            } catch (NamingException e) {
                System.err.println(e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                return;
            }
        }
        rService.firstPage();
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("pOfTheDay", p);
        ctx.setVariable("rService", rService);
        templateEngine.process("review", ctx, response.getWriter());
    }

    public void destroy() { }
}

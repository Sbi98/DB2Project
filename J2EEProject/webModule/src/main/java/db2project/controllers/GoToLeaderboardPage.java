package db2project.controllers;
import db2project.entity.Product;
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


@WebServlet(name = "GoToLeaderboardPage", value = "/user/GoToLeaderboardPage")
public class GoToLeaderboardPage extends HttpServlet {
    private TemplateEngine templateEngine;

    public GoToLeaderboardPage() { super(); }

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
        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("pOfTheDay", p);
        templateEngine.process("leaderboard", ctx, response.getWriter());
    }

    public void destroy() { }
}

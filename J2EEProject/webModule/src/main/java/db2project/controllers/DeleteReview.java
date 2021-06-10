package db2project.controllers;

import db2project.entity.Review;
import db2project.entity.User;
import db2project.exceptions.OffensiveWordsException;
import db2project.services.NewReviewService;
import db2project.services.ProductService;
import db2project.services.UserService;
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


@WebServlet(name = "DeleteReview", value = "/user/DeleteReview")
public class DeleteReview extends HttpServlet { //TODO
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    ProductService prodService;

    public DeleteReview() {
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
        Review r = (Review) request.getSession().getAttribute("pOfTheDayReview");
        if (r != null) {
            prodService.deleteReview(r);
            prodService.addRepentedUser(r.getProduct(),r.getUser());
            request.getSession().removeAttribute("pOfTheDayReview");
            request.getSession().setAttribute("pOfTheDay", prodService.getProductOfToday());
        }
        response.sendRedirect(getServletContext().getContextPath()+"/user/GoToUserHomePage");
    }

    public void destroy() { }
}
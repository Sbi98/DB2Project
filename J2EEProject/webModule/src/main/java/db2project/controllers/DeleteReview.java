package db2project.controllers;

import db2project.entity.Review;
import db2project.services.ProductService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "DeleteReview", value = "/user/DeleteReview")
public class DeleteReview extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB(name = "db2project.services/ProductService")
    ProductService prodService;

    public DeleteReview() {
        super();
    }

    public void init() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Review r = (Review) request.getSession().getAttribute("pOfTheDayReview");
        if (r != null) {
            prodService.deleteReview(r);
            // If an user removes a review, then he is one of the "repented users"
            prodService.addRepentedUser(r.getProduct(),r.getUser());
            request.getSession().removeAttribute("pOfTheDayReview");
            //request.getSession().setAttribute("pOfTheDay", prodService.getProductOfToday());
        }
        response.sendRedirect(getServletContext().getContextPath()+"/user/GoToUserHomePage");
    }

    public void destroy() { }
}
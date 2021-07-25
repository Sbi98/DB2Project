package db2project.controllers;

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


@WebServlet(name = "SaveReview", value = "/user/SaveReview")
public class SaveReview extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/UserService")
    UserService userService;
    @EJB(name = "db2project.services/ProductService")
    ProductService pService;

    public SaveReview() {
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
        NewReviewService rService = (NewReviewService) request.getSession().getAttribute("rService");
        try {
            String agestr = request.getParameter("age");
            Integer age = null;
            if (agestr != null && !agestr.isEmpty())
                age = Integer.parseInt(agestr);
            String sex = request.getParameter("sex");
            String level = request.getParameter("level");
            rService.getReview().setAge(age);
            rService.getReview().setSex(sex);
            rService.getReview().setLevel(level);
            // Salva la review dopo aver verificato la presenza di parole offensive
            rService.saveReview();
            // Nel caso l'utente fosse un repented user (aveva inserito una review al prodotto, che poi ha cancellato,
            // all'aggiunta della nuova review del prodotto, rimuovi l'utente dai repented_users
            pService.removeRepentedUser(rService.getReview().getProduct(), (User) request.getSession().getAttribute("user"));

            request.getSession().setAttribute("pOfTheDayReview", rService.getReview());
            request.getSession().removeAttribute("rService");
            final WebContext ctx = new WebContext(request, response, getServletContext());
            templateEngine.process("savedReview", ctx, response.getWriter());
        } catch (OffensiveWordsException owe) {
            System.out.println(owe.getMessage());
            userService.blockUser((User) request.getSession().getAttribute("user"));
            request.getSession().invalidate();
            response.sendRedirect(getServletContext().getContextPath()+"/blockedUser.html");
        }
        rService.remove();
    }

    public void destroy() { }
}
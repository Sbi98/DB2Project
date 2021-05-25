package db2project.controllers;

import db2project.entity.User;
import db2project.exceptions.OffensiveWordsException;
import db2project.services.NewReviewService;
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
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/UserService")
    UserService userService;

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
            rService.saveReview();
            final WebContext ctx = new WebContext(request, response, getServletContext());
            templateEngine.process("savedReview", ctx, response.getWriter());
        } catch (OffensiveWordsException owe) {
            userService.blockUser((User) request.getSession().getAttribute("user"));
            request.getSession().invalidate();
            response.sendRedirect(getServletContext().getContextPath()+"/user/blockedUser.html");
        }
        rService.remove();
        request.getSession().removeAttribute("rService");
    }

    public void destroy() { }
}
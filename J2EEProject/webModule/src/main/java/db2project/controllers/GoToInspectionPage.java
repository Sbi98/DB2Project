package db2project.controllers;

import db2project.services.ProductService;
import db2project.utils.Utils;
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
import java.util.Date;

@WebServlet(name = "GoToInspectionPage", value = "/admin/GoToInspectionPage")
public class GoToInspectionPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;

    public GoToInspectionPage() { super(); }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setPrefix("/WEB-INF/admin/");
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext());
        templateEngine.process("inspectionPage", ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext());
        if (request.getParameter("date") == null) {
            ctx.setVariable("displayMsg", "You must select a date!");
        } else {
            Date selectedDate = Utils.utcDateFromString(request.getParameter("date"));
            if (!Utils.isBeforeToday(selectedDate)) {
                ctx.setVariable("displayMsg", "You must select a past date!");
            } else {
                ctx.setVariable("selectedDate", selectedDate);
                ctx.setVariable("selectedProduct", prodService.getProductOfDay(selectedDate));
            }
            templateEngine.process("inspectionPage", ctx, response.getWriter());
        }
    }

    public void destroy() { }
}

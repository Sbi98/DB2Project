package db2project.controllers;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GoToInspectionPage", value = "/admin/GoToInspectionPage")
public class GoToInspectionPage extends HttpServlet {
    private TemplateEngine templateEngine;

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
        // Rimuove l'attributo 'rService' dalla sessione

        request.getSession().setAttribute("counter", 1);

        final WebContext ctx = new WebContext(request, response, getServletContext());
        ctx.setVariable("counter", 1);
        templateEngine.process("test", ctx, response.getWriter());
    }

    public void destroy() { }

}

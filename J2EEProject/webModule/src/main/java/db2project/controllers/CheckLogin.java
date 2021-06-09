package db2project.controllers;

import db2project.entity.User;
import db2project.services.UserService;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet(name = "CheckLogin", value = "/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "db2project.services/UserService")
    private UserService usrService;

    public CheckLogin() {
        super();
    }

    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //templateResolver.setPrefix("/WEB-INF/");
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Restituisce la sessione corrente, essendo il boolean = false, se non esiste non ne crea una nuova ma restituisce null
        HttpSession session = request.getSession(false);
        // Se era presente una sessione, la invalida. Teoricamente è sempre presente una sessione
        if (session != null) {
            session.invalidate();
        }
        String usrn;
        String pwd;
        try {
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            // Teoricamente impossibile, avendo ogni elemento del form l'attributo 'required'
            if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }
        // Se il form è stato compilato correttamente:
        try {

            // Verifica credenziali
            User user = usrService.checkCredentials(usrn, pwd);
            if (user.isBlocked())
                throw new Exception("This user has been blocked!");
            request.getSession().setAttribute("user", user);
            if (user.isAdmin()) {
                response.sendRedirect(getServletContext().getContextPath()+"/admin/GoToAdminHomePage");
            } else {
                usrService.newAccess(user);
                response.sendRedirect(getServletContext().getContextPath()+"/user/GoToUserHomePage");
            }
        } catch (Exception e) {
            // Lanciata per esempio se l'utente specificato non esiste / password errata
            System.err.println(e.getMessage());
            // Ricrea pagina web mostrando l'error message
            final WebContext ctx = new WebContext(request, response, getServletContext());
            ctx.setVariable("errorMsg", e.getMessage());
            templateEngine.process("/index", ctx, response.getWriter());
        }
    }

    public void destroy() { }
}

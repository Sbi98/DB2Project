package db2project.controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Logout", value = "/Logout")
public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Logout() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Restituisce la sessione corrente, essendo il boolean = false, se non esiste non ne crea una nuova ma restituisce null
        HttpSession session = request.getSession(false);
        // Se era presente una sessione, la invalida. Teoricamente è sempre presente una sessione
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(getServletContext().getContextPath() + "/index.html");
    }
}

package db2project.controllers;

import db2project.exceptions.UniqueConstraintViolation;
import db2project.services.CreationService;
import db2project.services.ProductService;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "CreateProduct", value = "/admin/CreateProduct")
@MultipartConfig
public class CreateProduct extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB(name = "db2project.services/ProductService")
    private ProductService prodService;
    public CreateProduct() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
            if (creationService == null)
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Contesto non valido: non è " +
                        "presente un creationService nella sessione");
            else {
                String productName = creationService.getProductName();
                Date productDate = creationService.getDate();
                byte[] productImgByteArray = creationService.getImgByteArray();
                System.out.println("Data letta: "+productDate.getTime());
                System.out.println("Data ora: "+ Instant.now().getEpochSecond());
                System.out.println("Long time: "+new Date().getTime());
                if (productName == null | productDate == null | productImgByteArray == null | productImgByteArray.length == 0)
                    throw new Exception("Invalid Product parameters");
                if (prodService.newProduct(productName, productDate, productImgByteArray, creationService.getQuestions()) != null) {
                    response.sendRedirect(getServletContext().getContextPath() + "/admin/GoToAdminHomePage");
                } else throw new UniqueConstraintViolation("Esiste già un prodotto per la data specificata!");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Impossibile creare il prodotto:\n" + e.getMessage());
        }
    }
}

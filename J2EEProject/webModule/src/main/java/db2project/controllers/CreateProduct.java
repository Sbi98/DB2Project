package db2project.controllers;

import db2project.services.CreationService;
import db2project.services.ProductService;

import java.io.IOException;
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
        CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
        if (creationService == null)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid workflow. CreationService not in session");
        else {
            String productName = creationService.getProductName();
            Date productDate = creationService.getDate();
            byte[] productImgByteArray = creationService.getImgByteArray();
            if (prodService.newProduct(productName, productDate, productImgByteArray, creationService.getQuestions()) != null) {
                response.sendRedirect(getServletContext().getContextPath() + "/admin/GoToAdminHomePage");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while creating the product");
            }
        }
    }
}

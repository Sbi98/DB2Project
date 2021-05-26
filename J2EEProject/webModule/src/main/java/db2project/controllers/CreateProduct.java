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
    private CreationService creationService;
    public CreateProduct() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CreationService creationService = (CreationService) request.getSession().getAttribute("creationService");
            System.out.println(creationService == null ? "CS NULL" : "CS NON NULL");
            assert creationService != null;
            String productName = creationService.getProductName();
            Date productDate = creationService.getDate();
            byte[] productImgByteArray = creationService.getImgByteArray();
            if (productName == null | productImgByteArray.length == 0) {
                throw new Exception("Invalid Product parameters");
            }
            prodService.newProduct2(productName, productDate, productImgByteArray, creationService.getQuestions());


            response.sendRedirect(getServletContext().getContextPath()+"/admin/GoToAdminHomePage");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "##" + e.getMessage());
        }
    }
}

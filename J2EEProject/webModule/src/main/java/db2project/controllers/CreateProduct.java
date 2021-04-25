package db2project.controllers;

import db2project.services.ProductService;
import db2project.utils.ImageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(request.getParameter("date"));
            Part imgFile = request.getPart("picture");
            InputStream imgContent = imgFile.getInputStream();
            byte[] imgByteArray = ImageUtils.readImage(imgContent); //img max of 4MB
            if (name == null | name.isEmpty() | imgByteArray.length == 0) {
                throw new Exception("Invalid Product parameters");
            }
            prodService.newProduct(name, date, imgByteArray);
            response.sendRedirect(getServletContext().getContextPath()+"/admin/GoToCreationPage");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}

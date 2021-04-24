package db2project.services;

import db2project.entity.Product;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;

@Stateless
public class ProductService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public Product findProductByName(int name) {
        Product p = em.find(Product.class, name);
        return p;
    }

    public void newProduct(String name, Date date, byte[] imgByteArray) {
        Product p = new Product(name, date, imgByteArray);
        em.persist(p);
    }



}

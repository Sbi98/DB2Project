package services;

import entity.Product;

import javax.ejb.Stateless;
import javax.persistence.*;

@Stateless
public class ProductService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public Product findProductByName(int name) {
        Product p = em.find(Product.class, name);
        return p;
    }





}

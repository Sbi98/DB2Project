package db2project.services;

import db2project.entity.Product;
import db2project.entity.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    public Product getProductOfToday() {
        return getProductOfDay(new Date());
    }

    public Product getProductOfDay(Date d) {
        List<Product> products;
        try {
            products = em.createNamedQuery("Product.getOfDay", Product.class)
                    .setParameter(1, d)
                    .getResultList();
            if (!products.isEmpty())
                return products.get(0); //TODO aggiungiamo al DB che la data deve essere unique?
            return null;
        } catch (PersistenceException e) { return null; }
    }

}

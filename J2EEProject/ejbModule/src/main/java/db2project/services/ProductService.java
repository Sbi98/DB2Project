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

    // Restituisce il prodotto con il nome specificato
    public Product findProductByName(int name) {
        Product p = em.find(Product.class, name);
        return p;
    }

    // Crea un nuovo prodotto sul database con le informazioni specificata
    public void newProduct(String name, Date date, byte[] imgByteArray) {
        Product p = new Product(name, date, imgByteArray);
        em.persist(p);
    }

    // Restituisce il prodotto del giorno
    public Product getProductOfToday() {
        return getProductOfDay(new Date());
    }

    // Restituisce il prodotto della data specificata
    public Product getProductOfDay(Date d) {
        List<Product> products;
        try {
            // Esegue la query: "SELECT p FROM Product p WHERE p.date = d"
            products = em.createNamedQuery("Product.getOfDay", Product.class)
                    .setParameter(1, d)
                    .getResultList();
            if (!products.isEmpty())
                return products.get(0);
            // Se non c'è un prodotto del giorno restituisce null
            return null;
        } catch (PersistenceException e) { return null; }
    }

}

package db2project.services;

import db2project.entity.MQuestion;
import db2project.entity.Product;
import db2project.entity.Review;
import db2project.entity.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Stateless
public class ProductService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public List<Product> getAllProducts() {
        return em.createNamedQuery("Product.getAll", Product.class).getResultList();
    }

    // Crea un nuovo prodotto sul database con le informazioni specificata
    public void newProduct(String name, Date date, byte[] imgByteArray) {
        Product p = new Product(name, date, imgByteArray);
        em.persist(p);
    }

    public void newProduct2(String name, Date date, byte[] imgByteArray, List<String> questions){
        Product p = new Product(name, date, imgByteArray);
        for (String q : questions){
            p.addQuestion(new MQuestion(p, q));
        }
        em.persist(p);
    }

    public Review findReview(int pId, int uId) {
        List<Review> result = em.createNamedQuery("Review.findByProductAndUser", Review.class)
                .setParameter(1,pId)
                .setParameter(2,uId)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    //Cancella la review con id revId. Non usata attualmente
    public void deleteReview(int revId) {
        Review r = em.find(Review.class, revId);
        if (r != null) {
            em.remove(r);
        }
    }

    //aggiunge l'utente alla lista di chi ha cancellato la review di quel prodotto
    public void addRepentedUser(Product p, User u) {
        p.addRepentedUser(u);
        em.merge(p);
    }

    //Cancella la review e aggiunge l'utente alla lista di chi ha cancellato la review di quel prodotto
    public void deleteReview(Review r) {
        addRepentedUser(r.getProduct(), r.getUser());
        em.remove(r);
        /*em.flush();
        em.refresh(em.find(User.class, r.getUser().getId()));
        em.refresh(em.find(Product.class, r.getProduct().getId()));*/
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

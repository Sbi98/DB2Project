package db2project.services;

import db2project.entity.MQuestion;
import db2project.entity.Product;
import db2project.entity.Review;
import db2project.entity.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Stateless
public class ProductService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public Product getProduct(int id) {
        return em.find(Product.class, id);
    }

    public List<Product> getAllProducts() {
        return em.createNamedQuery("Product.getAll", Product.class).getResultList();
    }

    public List<Product> getAllProductsBeforeToday() {
        return getAllProductsBeforeDate(new Date());
    }

    public List<Product> getAllProductsBeforeDate(Date date) {
        try {
            return em.createNamedQuery("Product.getBefore", Product.class)
                    .setParameter(1, date)
                    .getResultList();
        } catch (PersistenceException e) { return null; }
    }

    public void newProduct(String name, Date date, byte[] imgByteArray) {
        Product p = new Product(name, date, imgByteArray);
        em.persist(p);
    }

    public Product newProduct(String name, Date date, byte[] imgByteArray, List<String> questions){
        Product p = new Product(name, date, imgByteArray);
        for (String q : questions) {
            new MQuestion(p, q);
        }
        em.persist(p);
        return p;
    }

    public Review findReviewOfUser(int pId, int uId) {
        List<Review> result = em.createNamedQuery("Review.findByProductAndUser", Review.class)
                .setParameter(1,pId)
                .setParameter(2,uId)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Review> getReviewsOfProduct(Product p) {
        return em.createNamedQuery("Review.findByProduct", Review.class)
                .setParameter(1,p.getId())
                .getResultList();
    }

    //aggiunge l'utente alla lista di chi ha cancellato la review di quel prodotto
    public void addRepentedUser(Product p, User u) {
        Product managedP = em.find(Product.class, p.getId());
        managedP.addRepentedUser(u);
    }

    public void removeRepentedUser(Product p, User u) {
        Product managedP = em.find(Product.class, p.getId());
        User managedU = em.find(User.class, u.getId());
        managedP.removeRepentedUser(managedU);
    }

    public boolean isBeforeToday(Date date) {
        long now = Instant.now().getEpochSecond();
        return date.getTime() < (now - now % (24 * 60 * 60))*1000;
    }

    public boolean eraseQuestionnaireData(int productId) {
        try {
            // Recupero il prodotto
            Product p = em.find(Product.class, productId);
            // Verifico sia di data passata alla corrente
            if (!isBeforeToday(p.getDate())) {
                System.out.println("Non è possibile cancellare i dati relativi a questionari in corso o futuri!");
                return false;
            } else {
                // Cancello tutte le review di quel prodotto, azzero quindi il questionario
                List<Review> reviewsToDelete = p.getReviews();
                while(!reviewsToDelete.isEmpty()) {
                    deleteReview(reviewsToDelete.get(0));
                }
                return true;
            }
        } catch (PersistenceException e) { return false; }
    }

    //Cancella la review e aggiunge l'utente alla lista di chi ha cancellato la review di quel prodotto
    public void deleteReview(Review r) {
        Review managedR = em.find(Review.class, r.getId());
        Product managedP = em.find(Product.class, r.getProduct().getId());
        User managedU = em.find(User.class, r.getUser().getId());
        managedP.removeReview(managedR);
        managedU.removeReview(managedR);
        //em.remove(managedR); non serve visto che entrambe le liste hanno l'orphan removal
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
                    .setHint("javax.persistence.cache.storeMode", "REFRESH")
                    .setParameter(1, d)
                    .getResultList();
            if (!products.isEmpty())
                return products.get(0);
            // Se non c'è un prodotto del giorno restituisce null
            return null;
        } catch (PersistenceException e) { return null; }
    }

}

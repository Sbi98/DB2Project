package db2project.services;

import db2project.entity.MQuestion;
import db2project.entity.Product;
import db2project.entity.Review;
import db2project.entity.User;
import db2project.exceptions.UniqueConstraintViolation;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
        List<Product> products;
        try {
            return em.createNamedQuery("Product.getBefore", Product.class)
                    .setParameter(1, date)
                    .getResultList();
        } catch (PersistenceException e) { return null; }
    }

    // Crea un nuovo prodotto sul database con le informazioni specificata
    public void newProduct(String name, Date date, byte[] imgByteArray) {
        Product p = new Product(name, date, imgByteArray);
        em.persist(p);
    }

    public Product newProduct(String name, Date date, byte[] imgByteArray, List<String> questions){
        if (getProductOfDay(date) == null) {
            Product p = new Product(name, date, imgByteArray);
            for (String q : questions) {
                p.addQuestion(new MQuestion(p, q));
            }
            System.out.println("Salvo prodotto" + p == null);
            em.persist(p);
            System.out.println("Prodotto salvato" + p == null);
            return p;
        } else {
            System.out.println("Prodotto già presente! Nome: " + getProductOfDay(date).getName());
            return null;
        }
    }

    public Review findReview(int pId, int uId) {
        List<Review> result = em.createNamedQuery("Review.findByProductAndUser", Review.class)
                .setParameter(1,pId)
                .setParameter(2,uId)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean eraseQuestionnaireData(String productId){
        try{
            // Recupero il prodotto
            Product p = em.find(Product.class, Integer.parseInt(productId));
            long time = new Date().getTime();
            // Verifico sia di data passata alla corrente
            if (p.getDate().compareTo(new Date(time - time % (24 * 60 * 60 * 1000) - 2 * 60 * 60 * 1000)) >= 0) {
                System.out.println("Non è possibile cancellare i dati relativi a questionari in corso o futuri!");
                return false;
            } else {
                // Per ciascuna review decremento i punti dell'utente associato
                for (Review r : p.getReviews()) {
                    User u = r.getUser();
                    u.setPoints(u.getPoints() - r.getPoints());
                    u.getReviews().remove(r);
                    em.persist(u);
                    em.remove(r);
                }
                return true;
            }
        } catch (PersistenceException e) { return false; }

    }

    //Cancella la review con id revId
    public void deleteReview(int revId) {
        Review r = em.find(Review.class, revId);
        if (r != null) {
            em.remove(r);
        }
    }

    //Cancella la review
    public void deleteReview(Review r) {
        em.remove(em.merge(r));
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

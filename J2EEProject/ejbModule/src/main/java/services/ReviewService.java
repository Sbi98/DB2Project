package services;

import entity.Product;
import entity.Review;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class ReviewService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public List<Review> findReviewsByProductName(String productName) {
        Product p = em.find(Product.class, productName);
        return p.getReviews();
    }
}

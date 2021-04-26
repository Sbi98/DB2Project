package db2project.services;

import db2project.entity.*;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.*;
import java.util.List;

@Stateful
public class NewReviewService {
    @PersistenceContext(unitName = "DB2Project", type = PersistenceContextType.EXTENDED)
    private EntityManager em;
    private int currentPage = 1;
    private Review review;

    public NewReviewService() { }

    public NewReviewService(Product p, User u) {
        review = new Review(p, u);
    }

    public int getCurrentPage() { return currentPage; }

    public Review getReview() { return review; }

    public void firstPage() { currentPage = 1; }

    public void secondPage() { currentPage = 2; }

    public void createReview(Product p, User u) {
        review = new Review(p, u);
    }

    @Remove
    public void remove() {}
}

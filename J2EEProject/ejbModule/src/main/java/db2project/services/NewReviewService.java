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


    public NewReviewService(Product p, User u) {
        review = new Review(p, u);
    }

    public String getAnswerTextFor(int question) {
        if (review == null)
            return null;
        List<MAnswer> answers = review.getAnswers();
        if (answers != null) {
            for (MAnswer a : answers) {
                if (a.getQuestion().getId() == question)
                    return a.getText();
            }
        }
        return null;
    }

    public Review getReview() { return review; }

    public int getCurrentPage() { return currentPage; }

    public void firstPage() { currentPage = 1; }

    public void secondPage() { currentPage = 2; }

    @Remove
    public void remove() {}

    public NewReviewService() { }
}

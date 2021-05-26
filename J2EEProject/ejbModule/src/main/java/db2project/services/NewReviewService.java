package db2project.services;

import db2project.entity.*;
import db2project.exceptions.OffensiveWordsException;

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


    public void createReview(Product p, User u) {
        review = new Review(p, u);
    }

    public void saveReview() throws OffensiveWordsException {
        List<OffensiveWords> wList = em.createNamedQuery("OffensiveWords.getAll", OffensiveWords.class).getResultList();
        for (MAnswer a : review.getAnswers()) {
            for (OffensiveWords w : wList)
                if (a.getText().contains(w.getWord()))
                    throw new OffensiveWordsException();
        }
        review.getProduct().removeRepentedUser(review.getUser());
        em.persist(review);
        /*em.flush();
        em.refresh(em.find(User.class, review.getUser().getId()));
        em.refresh(em.find(Product.class, review.getProduct().getId()));*/
    }

    // Restituisce il testo della risposta data alla domanda specificata. Se non esiste, restituisce 'null'
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

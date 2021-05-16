package db2project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CreationService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;
    private int numberOfQuestions = 1;
    private boolean questionCreationPhase = false;
    private String productName;
    private Date date;
    private

    public CreationService() { }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public boolean isQuestionCreationPhase() {
        return questionCreationPhase;
    }

    public void setQuestionCreationPhase(boolean questionCreationPhase) {
        this.questionCreationPhase = questionCreationPhase;
    }

    public void incrementNumberOfQuestions(int numberOfQuestions) {
        ++this.numberOfQuestions;
    }

}

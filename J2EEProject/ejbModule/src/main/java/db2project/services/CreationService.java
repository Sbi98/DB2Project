package db2project.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CreationService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;
    private int numberOfQuestions = 1;

    public CreationService() { }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void incrementNumberOfQuestions(int numberOfQuestions) {
        ++this.numberOfQuestions;
    }

}

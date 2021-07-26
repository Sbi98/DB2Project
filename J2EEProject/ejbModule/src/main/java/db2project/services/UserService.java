package db2project.services;

import db2project.entity.User;
import db2project.entity.Access;
import org.eclipse.persistence.exceptions.EclipseLinkException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public UserService() { }

    // Verifica la validità delle credenziali e restituisce l'utente, altrimenti lancia un'eccezione
    public User checkCredentials(String username, String pwd) throws Exception {
        try {
            List<User> users = em.createNamedQuery("User.checkCredentials", User.class)
                    .setParameter(1, username)
                    .setParameter(2, pwd)
                    .getResultList();
            if (users.size() == 1)
                return users.get(0);
            throw new Exception("Can't find the specified user. Check username and password");
        } catch (PersistenceException e) {
            System.err.println(e.getMessage());
            throw new Exception("Could not verify credentals");
        }
    }

    public void newAccess(User u) {
        em.persist(new Access(u));
    }

    public void blockUser(User u) {
        User managedU = em.find(User.class, u.getId());
        managedU.block();
    }

    // Registra un nuovo utente, purché non violi alcun vincolo (come l'unicità dell'username)
    public User registerUser(String username, String pwd, String email) throws PersistenceException {
        try {
            // Crea l'oggetto utente e lo salva sul database
            User u = new User(username, pwd, email);
            em.persist(u);
            return u;
        } catch (PersistenceException e) {
            // Se il salvataggio sul database non va a buon fine lancia un'eccezione
            String errorMsg;
            try {
                errorMsg = ((EclipseLinkException) e.getCause()).getInternalException().getMessage().replaceAll("\\(conn=[0-9]+\\) ","");
            } catch (Exception ex) {throw e;}
            throw new PersistenceException(errorMsg);
        }
    }


    public List<User> getReviewersForProduct(int pId) {
        return em.createNamedQuery("User.findReviewersForProduct", User.class)
                .setHint("javax.persistence.cache.storeMode", "REFRESH")
                .setParameter(1, pId)
                .getResultList();
    }

}

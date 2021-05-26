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
        List<User> users = null;
        try {
            users = em.createNamedQuery("User.checkCredentials", User.class)
                    .setParameter(1, username)
                    .setParameter(2, pwd)
                    .getResultList();
            if (users.size() == 1) {
                em.clear();
                return users.get(0);
            }
            throw new Exception("Can't find the specified user. Check username and password");
        } catch (PersistenceException e) {
            throw new Exception("Could not verify credentals");
        }
    }

    public List<User> getReviewersForProduct(int pId) {
        em.clear();
        return em.createNamedQuery("User.findReviewersForProduct", User.class)
                .setParameter(1, pId)
                .getResultList();
    }

    public void newAccess(User u) {
        em.persist(new Access(u));
    }

    public void blockUser(User u) {
        u.block();
        em.merge(u);
    }

    public boolean willViolateUniqueConstraints(String username, String email) throws Exception {
        List<User> users = null;
        try {
            users = em.createNamedQuery("User.findByUsernameOrEmail", User.class)
                    .setParameter(1, username)
                    .setParameter(2, email)
                    .getResultList();
            return !users.isEmpty();
        } catch (PersistenceException e) {
            throw new Exception("Could not verify constraints");
        }
    }

    // Registra un nuovo utente, purché non violi alcun vincolo (come l'unicità dell'username)
    public User registerUser(String username, String pwd, String email) throws PersistenceException {
        try {
            // Crea l'oggetto utente e lo salva sul database
            User u = new User(username, pwd, email);
            em.persist(u);
            em.flush();
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

}

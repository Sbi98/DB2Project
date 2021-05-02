package db2project.services;

import db2project.entity.OffensiveWords;
import db2project.entity.User;
import db2project.exceptions.UniqueConstraintViolation;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.EclipseLinkException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public UserService() { }

    public User checkCredentials(String username, String pwd) throws Exception {
        List<User> users = null;
        try {
            users = em.createNamedQuery("User.checkCredentials", User.class)
                    .setParameter(1, username)
                    .setParameter(2, pwd)
                    .getResultList();
            if (users.size() == 1)
                return users.get(0);
            throw new Exception("Can't find the specified user. Check username and password");
        } catch (PersistenceException e) {
            throw new Exception("Could not verify credentals");
        }
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

    public User registerUser(String username, String pwd, String email) throws PersistenceException {
        try {
            User u = new User(username, pwd, email);
            em.persist(u);
            em.flush();
            return u;
        } catch (PersistenceException e) {
            String errorMsg;
            try {
                errorMsg = ((EclipseLinkException) e.getCause()).getInternalException().getMessage().replaceAll("\\(conn=[0-9]+\\) ","");
            } catch (Exception ex) {throw e;}
            throw new PersistenceException(errorMsg);
        }
    }
}

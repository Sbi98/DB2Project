package db2project.services;

import db2project.entity.User;

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
}

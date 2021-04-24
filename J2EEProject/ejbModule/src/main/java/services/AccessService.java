package services;

import entity.*;
import entity.Access;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Stateless
public class AccessService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

    public AccessService() { }

    //Mi sa che non serve
    public List<Access> findAccessesByUser(int userId) {
        User u = em.find(User.class, userId);
        List<Access> accesses = u.getAccesses();
        return accesses;
    }

    public void newAccess(int userId) {
        User u = em.find(User.class, userId);
        Access access = new Access(u, new Date());
        u.addAccess(access);
        em.persist(u);
    }
}

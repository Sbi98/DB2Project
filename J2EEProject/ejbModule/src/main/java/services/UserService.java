package services;

import javax.ejb.Stateless;
import javax.persistence.*;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;

}

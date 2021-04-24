package db2project.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private int id;

    @ManyToOne // (Bi-directional) many-to-one association to User. I am the OWNER.
    @JoinColumn(name = "user") //nome della colonna che ha la FK a User
    private User user;

    @Temporal(TemporalType.DATE)
    private Date at;


    public Access() {}

    public Access(User user, Date at) {
        this.user = user;
        this.at = at;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Date getAt() { return at; }
    public void setAt(Date at) { this.at = at; }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Access access = (Access) o;
        return id == access.id;
    }

    @Override
    public int hashCode() { return id; }
}
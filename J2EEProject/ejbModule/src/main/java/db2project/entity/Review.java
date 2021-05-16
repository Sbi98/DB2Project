package db2project.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "review", schema = "DB2Project")
@NamedQueries({
    @NamedQuery(name = "Review.findByProduct", query = "SELECT r FROM Review r WHERE r.product.id = ?1"),
    @NamedQuery(name = "Review.findByProductAndUser", query = "SELECT r FROM Review r WHERE r.product.id = ?1 and r.user.id = ?2"),
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private int id;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private int points;
    private Integer age;
    private String sex;
    private String level;

    @OneToMany(
        fetch = FetchType.EAGER,
        mappedBy = "review",
        // quando viene effettuata l'operazione X su di me (Review), effettuala anche a questa relazione (MAnswer)
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH },
        orphanRemoval = true //se viene tolta una answer dalla lista, cancella quella answer
    )
    private List<MAnswer> answers;

    public Review() { }

    public Review(Product product, User user) {
        this.product = product;
        this.user = user;
    }

    public List<MAnswer> getAnswers() {
        return answers;
    }

    public void addAnswer(MAnswer a) {
        if (answers == null) {
            answers = new LinkedList<>();
        }
        answers.add(a);
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id;
    }

    @Override
    public int hashCode() { return id; }
}

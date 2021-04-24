package entity;

import javax.persistence.*;

@Entity
@Table(name = "offensive_words", schema = "DB2Project")
public class OffensiveWords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Unique id generated by the persistence provider
    private String word;

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffensiveWords that = (OffensiveWords) o;
        return that.word.equals(this.word);
    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }
}

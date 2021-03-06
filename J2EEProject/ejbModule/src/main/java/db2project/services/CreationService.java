package db2project.services;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Stateful
public class CreationService {
    @PersistenceContext(unitName = "DB2Project")
    private EntityManager em;
    private String productName;
    private Date date;
    private byte[] imageByteArray;
    private String imageBase64;
    private List<String> questions;

    public CreationService() {
        questions = new ArrayList<>();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void removeQuestionAt(int index) {
        questions.remove(index);
    }

    public List<String> getQuestions(){
        return questions;
    }

    public void addQuestion(String newQuestion) {
        questions.add(newQuestion);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getImgByteArray() {
        return imageByteArray;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImgByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
        this.imageBase64 = Base64.getMimeEncoder().encodeToString(imageByteArray);
    }

    @Remove
    public void remove() {
    }
}

package db2project.exceptions;

import javax.persistence.PersistenceException;

public class UniqueConstraintViolation extends PersistenceException {

    public UniqueConstraintViolation() {
    }

    public UniqueConstraintViolation(String message) {
        super(message);
    }
}

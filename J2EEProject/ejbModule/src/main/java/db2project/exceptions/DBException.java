package db2project.exceptions;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.ExceptionHandler;

import javax.persistence.PersistenceException;

public class DBException implements ExceptionHandler {
    public DBException() { }

    @Override
    public Object handleException(RuntimeException e) {
        DatabaseException dbex;
        try {
            dbex = (DatabaseException) e;
        } catch (Exception ex) {throw e;}
        if (dbex.getErrorCode() == 1062) {
            System.out.println("UniqueConstraintViolation");
            throw new UniqueConstraintViolation(dbex.getInternalException().getMessage());
        }
        throw new PersistenceException(e);
    }
}

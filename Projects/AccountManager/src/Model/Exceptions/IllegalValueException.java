package Model.Exceptions;

import javax.security.auth.login.AccountException;

/**
 * Created by AndyTsang on 2017-07-01.
 */
public class IllegalValueException extends AccountException {

    public IllegalValueException() {}

    public IllegalValueException( String msg ) {
        super(msg);
    }
}
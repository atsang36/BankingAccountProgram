package Model.Exceptions;

import javax.security.auth.login.AccountException;

/**
 * Created by AndyTsang on 2017-07-01.
 */
public class NotEnoughMoneyException extends AccountException {

    public NotEnoughMoneyException() {}

    public NotEnoughMoneyException( String msg ) {
        super(msg);
    }
}

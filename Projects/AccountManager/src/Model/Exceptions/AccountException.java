package Model.Exceptions;

/**
 * Created by AndyTsang on 2017-07-01.
 */
public class AccountException extends Exception {

    public AccountException() {}

    public AccountException( String msg) {
        super(msg);
    }
}

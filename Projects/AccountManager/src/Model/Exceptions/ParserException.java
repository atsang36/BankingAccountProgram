package Model.Exceptions;

/**
 * Created by AndyTsang on 2017-07-08.
 */

// Exception thrown if any kind of error occurs while parsing resources
public class ParserException extends Exception {
    public ParserException() {
        super();
    }

    public ParserException(String msg) {
        super(msg);
    }
}
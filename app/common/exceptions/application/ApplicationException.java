package common.exceptions.application;


public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super();
    }

    public ApplicationException(Exception cause) {
        super(cause);
    }

    public ApplicationException(String s, Exception cause) {
        super(s, cause);
    }

    public ApplicationException(String s) {
        super(s);
    }

}

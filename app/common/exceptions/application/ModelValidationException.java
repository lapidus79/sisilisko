package common.exceptions.application;

public class ModelValidationException extends ApplicationException {

    public String msg = null;

    public ModelValidationException(String s) {
        super(s);
        this.msg = s;
    }

    @Override
    public String getMessage() {
        return String.valueOf(msg);
    }
}

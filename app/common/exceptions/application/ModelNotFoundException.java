package common.exceptions.application;

public class ModelNotFoundException extends ApplicationException {

    public Long id = null;

    public ModelNotFoundException() {
        super();
    }

    public ModelNotFoundException(Long id) {
        super();
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "not found: " + String.valueOf(id);
    }
}

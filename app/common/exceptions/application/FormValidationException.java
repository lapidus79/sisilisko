package common.exceptions.application;


import play.data.validation.ValidationError;

import java.util.List;
import java.util.stream.Collectors;

public class FormValidationException extends ApplicationException {

    public List<ValidationError> validationErrors = null;

    public FormValidationException() {
        super();
    }

    public FormValidationException(List<ValidationError> errors) {
        super();
        this.validationErrors = errors;
    }

    public String getMessage() {
        if (validationErrors != null && validationErrors.size() > 0) {
            return validationErrors.stream()
                    .map((e) -> (e != null && e.key() != null) ? e.key() : "")
                    .collect(Collectors.joining(","));
        } else {
            return "invalid input";
        }
    }

}

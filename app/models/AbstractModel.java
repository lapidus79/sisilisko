package models;

import io.ebean.Model;
import common.exceptions.application.ModelValidationException;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractModel extends Model {

    public abstract void validate() throws ModelValidationException;

}

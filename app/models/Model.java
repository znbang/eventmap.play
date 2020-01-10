package models;

import play.db.jpa.GenericModel;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Model extends GenericModel {
    @Id
    public String id;

    public String getId() {
        return id;
    }

    @Override
    public Object _key() {
        return getId();
    }
}

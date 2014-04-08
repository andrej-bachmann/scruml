/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.fixture;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;
import scruml.model.IARModel;

/**
 *
 * @author Kevin
 */
public class TestModel implements IARModel {

    private final IntegerProperty id = new DBSimpleIntegerProperty();
    private final StringProperty firstname = new DBSimpleStringProperty();
    private final StringProperty lastname = new DBSimpleStringProperty();
    
    @Override
    public String getTablename() {
        return "test";
    }

    @Override
    public String getKey() {
        return "id";
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

}

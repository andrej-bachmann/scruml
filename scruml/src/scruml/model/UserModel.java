/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;

/**
 *
 * @author tom
 */
public class UserModel implements IARModel{
     private final IntegerProperty id = new DBSimpleIntegerProperty();
     private final StringProperty name = new DBSimpleStringProperty();
     private final IntegerProperty role = new DBSimpleIntegerProperty();

    @Override
    public String getTablename() {
        return "user";
    }

    @Override
    public String getKey() {
        return "id";
    }
    
    public int getId() {
        return this.id.get();
    }
    public void setId(int id){
        this.id.set(id);
    }
    
    public String getName() {
        return this.name.get();
    }
    public void setName(String name){
        this.name.set(name);
    }
    
    public int getRole(){
        return this.role.get();
    }
    
    public void setRole(int role){
        this.role.set(role);
    }
}

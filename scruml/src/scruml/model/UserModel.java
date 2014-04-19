package scruml.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;

/**
 * This model represents the database table "user".
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class UserModel implements IARModel {
    
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
    
    public String getName() {
        return this.name.get();
    }
    
    public int getRole(){
        return this.role.get();
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setRole(int role){
        this.role.set(role);
    }
    
}

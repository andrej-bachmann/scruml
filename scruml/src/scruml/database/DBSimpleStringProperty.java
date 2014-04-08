package scruml.database;

import javafx.beans.property.SimpleStringProperty;

/**
 * DBSimpleStringProperty subclasses SimpleStringProperty and defines a property
 * that gets populated from a database.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class DBSimpleStringProperty extends SimpleStringProperty {
    
    public void setDBValue(String str) {
        this.set(str);
    }
    
    public String getDBValue() {
        return this.get();
    }
    
}

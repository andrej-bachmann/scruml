package scruml.database;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * DBSimpleIntegerProperty subclasses SimpleIntegerProperty and defines a
 * property that gets populated from a database.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class DBSimpleIntegerProperty extends SimpleIntegerProperty {
    
    public void setDBValue(String str) {
        this.set(Integer.parseInt(str));
    }
    
    public String getDBValue() {
        int num = this.get();
        Integer s = num;
        return (num!=0) ? s.toString() : null;
    }
    
}

package scruml.database;

import javafx.beans.property.SimpleFloatProperty;

/**
 * DBSimpleFloatProperty subclasses SimpleFloatProperty and defines a property
 * that gets populated from a database.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class DBSimpleFloatProperty extends SimpleFloatProperty {
    
    public void setDBValue(String str) {
        this.set(Float.parseFloat(str));
    }
    
    public String getDBValue() {
        float num = this.get();
        Float f = num;
        return (num!=0.0f) ? f.toString() : null;
    }
    
}

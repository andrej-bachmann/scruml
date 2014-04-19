package scruml.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;

/**
 * This model represents the database table "rule".
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RuleModel implements IARModel {

    private final IntegerProperty id = new DBSimpleIntegerProperty();
    private final StringProperty name = new DBSimpleStringProperty();
    private final IntegerProperty role = new DBSimpleIntegerProperty();
    private final IntegerProperty hasAccess = new DBSimpleIntegerProperty();
    
    @Override
    public String getTablename() {
        return "rule";
    }

    @Override
    public String getKey() {
        return "id";
    }
    
}

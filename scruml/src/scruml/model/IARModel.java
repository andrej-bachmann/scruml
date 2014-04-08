package scruml.model;

/**
 * All active record models must implement this interface.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public interface IARModel {
    
    /**
     * This method must be overwritten in the model and must return the table
     * name of this model.
     * @return table name
     */
    public String getTablename();
    
    /**
     * This method must be overwritten in the model and must return the key
     * attribute.
     * @return key attribute name
     */
    public String getKey();
    
}

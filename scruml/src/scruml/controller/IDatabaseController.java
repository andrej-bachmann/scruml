package scruml.controller;

import java.util.List;
import scruml.model.IARModel;

/**
 * All classes that handle a database must implement this interface.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public interface IDatabaseController {
    
    /**
     * This method is used to start the connection to a database.
     * @throws Exception 
     */
    public void connect() throws Exception;
    
    /**
     * This method terminates the connection to a database.
     * @throws Exception 
     */
    public void disconnect() throws Exception;
    
    /**
     * The find method is used to retrieve one record from a database.
     * Note: Even when the where clause would probably retrieve more than one
     * database record only the first one is returned.
     * @param modelClass Model Class which gets populated with data.
     * @param where Where condition that gets applied to the sql statement.
     * @return Instance of modelClass where all properties got populated with
     * data from the database record.
     * @throws Exception 
     */
    public IARModel find(Class modelClass, String where) throws Exception;
    
    /**
     * The findAll method is used to retrieve many records from a database.
     * @param modelClass Model class which gets populated with data
     * @param where Where condition that gets applied to the sql statement.
     * @return List with instances of modelClass where all properties got
     * populated with data from the database record.
     * @throws Exception 
     */
    public List<IARModel> findAll(Class modelClass, String where) throws Exception;
    
    /**
     * The findAll method is used to retrieve many records from a database that
     * should get received in a specific order. It is basically overriding the
     * findAll method while adding an additional parameter order.
     * @param modelClass Model class which gets populated with data
     * @param where Where condition that gets applied to the sql statement.
     * @param order Order parameter that gets applied to the sql statement to
     * retrieve the results in a specific order.
     * @return List with instances of modelClass where all properties got
     * populated with data from the database record.
     * @throws Exception 
     */
    public List<IARModel> findAll(Class modelClass, String where, String order) throws Exception;
    
    /**
     * The save method is used to save a model in the database.
     * It automatically detects if the model is already saved in the database and
     * execute either an INSERT or UPDATE statement.
     * @param model Instance of IARModel that should be saved.
     * @throws Exception 
     */
    public void save(IARModel model) throws Exception;
    
    /**
     * The delete method removes a model from the database.
     * @param model Instance of IARModel that should be removed.
     * @throws Exception 
     */
    public void delete(IARModel model) throws Exception;
            
}

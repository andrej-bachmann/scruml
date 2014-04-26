package scruml.controller;

import java.util.List;
import scruml.model.IARModel;
import scruml.model.UserModel;

/**
 * UserController handles all the user related tasks.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class UserController {
    
    private final IDatabaseController db;
    
    /**
     * Constructor gets the database controller.
     */
    public UserController() {
        this.db = SQLiteDatabaseController.getInstance();
    }
    
    /**
     * This method creates a new user and saves it in the database.
     * @param name Name of the user
     * @param role role of the user (see ROLE_ constants in {@link UserModel})
     * @return UserModel
     * @throws Exception 
     */
    public UserModel createUser (String name, int role) throws Exception {
        UserModel user = new UserModel();
        user.setName(name);
        user.setRole(role);
        this.db.save(user);
        return user;
    }
    
    /**
     * Returns all users that are stored in the database.
     * @return List of UserModel, otherwise null
     */
    public List<IARModel> getAllUser() {
        try {
            return SQLiteDatabaseController.getInstance().findAll(UserModel.class, null);
        }
        catch(Exception e) {
            return null;
        }
    } 

}

package scruml.controller;

import java.util.List;
import scruml.model.IARModel;
import scruml.model.RequirementModel;

/**
 * RequirementController handles all the requirement related tasks.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RequirementController {
    
    /**
     * This method gets all requirement models from database.
     * @return List of RequirementModel
     */
    public List<IARModel> getAllRequirements() {
        try {
            return SQLiteDatabaseController.getInstance().findAll(RequirementModel.class, null);
        }
        catch(Exception e) {
            return null;
        }
    }
    
}

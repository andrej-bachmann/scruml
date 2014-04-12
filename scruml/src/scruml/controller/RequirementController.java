package scruml.controller;

import java.util.List;
import scruml.model.IARModel;
import scruml.model.RequirementModel;

/**
 * RequirementController handles all the requirement related tasks.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RequirementController {
    
    private final IDatabaseController db;
    
    /**
     * Constructor gets the database controller.
     */
    public RequirementController() {
        this.db = SQLiteDatabaseController.getInstance();
    }
    
    /**
     * This method creates a new requirement and saves it in the database.
     * @param title Title of the requirement
     * @param description Description of the requirement
     * @return RequirementModel
     * @throws Exception 
     */
    public RequirementModel createRequirement(String title, String description) throws Exception {
        RequirementModel model = new RequirementModel();
        model.setTitle(title);
        model.setDescription(description);
        model.setPriority(1); //default priority
        this.db.save(model);
        return model;
    }
    
 //   public void changeRequirementPriority(){
 //      reqMod.setPriority(priority);
 //   }
    
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

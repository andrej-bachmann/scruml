package scruml.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import scruml.model.IARModel;
import scruml.model.RequirementModel;
import scruml.view.RequirementViewController;

/**
 * RequirementController handles all the requirement related tasks.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RequirementController {
    
    private final IDatabaseController db;
    private RequirementViewController view;
    private MainSceneController mainSceneController;
    
    /**
     * Constructor gets the database controller.
     */
    public RequirementController(MainSceneController mainSceneController) {
        this.db = SQLiteDatabaseController.getInstance();
        this.mainSceneController = mainSceneController;
        try {
            this.view = new RequirementViewController(this);
            this.view.setProductBacklogWidth(mainSceneController.getMainSceneVC().getProductBacklogWidth());
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * This method creates a new requirement and saves it in the database.
     * @param title Title of the requirement
     * @param description Description of the requirement
     * @return RequirementModel
     * @throws Exception 
     */
    public void createRequirement(String title, String description, int priority) {
        RequirementModel model = new RequirementModel();
        model.setTitle(title);
        model.setDescription(description);
        if (priority == -1)
            model.setPriority(15); //default priority
        else
            model.setPriority(priority); 
        try {
            this.db.save(model);
            view.setRequirementModel(model);
            view.endEditing();
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    
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
    
    public void changePriority(RequirementModel m, int newPriority) {
        try {
            m.priorityProperty().set(newPriority);
            db.save(m);
        } catch (Exception ex) {
            Logger.getLogger(RequirementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteRequirement(RequirementModel m){
        try{
            db.delete(m);
        } catch (Exception ex){
            
        }
    }

    public RequirementViewController getView() {
        return view;
    }
    
    public void dragAndDropStarted(RequirementViewController currentDragRequirement) {
        this.mainSceneController.dragAndDropStarted(currentDragRequirement);
    }
    
    public void dragAndDropStopped() {
        this.mainSceneController.dragAndDropStopped();
    }
    
}

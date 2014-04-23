package scruml.controller;

import java.io.IOException;
import java.util.List;
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
    
    public RequirementController() {
        this.db = SQLiteDatabaseController.getInstance();
    }
    /**
     * Constructor gets the database controller.
     */
    public RequirementController(MainSceneController mainSceneController) {
        this();
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
    public void createRequirement(RequirementModel model) {
        try {
            if(model.getPriority()==null)
                model.setPriority("15");
            this.db.save(model);
            view.setRequirementModel(model);
            view.endEditing();
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    
    public void updateRequirement(RequirementModel model) {
        try {
            this.db.save(model);
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
    
    public void deleteRequirement(RequirementModel m){
        try{
            db.delete(m);
        } catch (Exception ex){
            System.err.println(ex);
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

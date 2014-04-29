package scruml.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import scruml.model.IARModel;
import scruml.model.RequirementModel;
import scruml.model.UserModel;
import scruml.view.MainSceneViewController;
import scruml.view.RequirementViewController;


/** 
 * MainSceneController extends javafx Application class. Inherits the main(String[]args)
 * 
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class MainSceneController extends Application {
    
    private MainSceneViewController mainSceneVC;
    /**
     * start Method connects to Database and creates new MainSceneViewController,
     * which shows the MainScene
     * @param stage MainStage Object
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        IDatabaseController dbController = SQLiteDatabaseController.getInstance();
        
        dbController.connect();
        mainSceneVC = new MainSceneViewController(stage, this); 
        
        this.initRequirements();
        this.initUsers();
        
        mainSceneVC.show();
        
    }
    
    /**
     * This method gets all requirements from the database and triggers the
     * addRequirement method.
     */
    private void initRequirements() {
        RequirementController rc = new RequirementController();
        List<IARModel> models = rc.getAllRequirements();
        for(IARModel model : models) {
            try {
                RequirementController requirementController = new RequirementController(this);
                RequirementViewController requirementVC = requirementController.getView();
                requirementVC.setRequirementModel((RequirementModel) model);
                requirementVC.setViewForProductBacklog();
                mainSceneVC.addRequirement(requirementVC);
            }
            catch(IOException e) {
                System.err.println(e);
            }
        }
    }
    
    /**
     * This method gets all users from the database and adds them to the view.
     */
    private void initUsers() {
        UserController uc = new UserController();
        List<UserModel> users = uc.getAllUser();
        this.mainSceneVC.setUsers(users);
    }
    
    /**
     * This method creates a new requirement and set
     * the view for editing.
     */
    public void newRequirementClicked() {
        try {
            RequirementController rc = new RequirementController(this);
            rc.getView().setViewForEditing();
            mainSceneVC.addRequirement(rc.getView());
        } catch (IOException ex) {
            Logger.getLogger(MainSceneViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method handles the drag and drop events
     * of the requirements.
     */
    public void dragAndDropStarted(RequirementViewController currentDragRequirement) {
        this.mainSceneVC.activateDragAndDrop(currentDragRequirement);
    }
    
    /**
     * The dragAndDropStopped method deactivates the
     * drag and drop modus.
     */
    public void dragAndDropStopped() {
        this.mainSceneVC.deactivateDragAndDrop();
    }
    
    /**
     * This method serves to delete requirements
     * Note: Just the requirements in the column "Product Backolg" can be deleted.
     */
    public void requirementDraggedToTrash(RequirementViewController requirementVC) {
        if(requirementVC.stateProperty().get() == RequirementViewController.STATE_PRODUCT_BACKLOCK) {
            RequirementController rc = new RequirementController();
            rc.deleteRequirement(requirementVC.getRequirementModel());
            this.mainSceneVC.removeRequirement(requirementVC);
        }
    }
    
    /**
     * The requirementDraggedToSprintBacklog is used to move 
     * requirements from the Product Backlog to the Sprint Backlog.
     */
    public void requirementDraggedToSprintBacklog(RequirementViewController requirementVC) {
        if(requirementVC.stateProperty().get() == RequirementViewController.STATE_PRODUCT_BACKLOCK) {
            requirementVC.stateProperty().set(RequirementViewController.STATE_SPRINT_BACKLOCK);
            this.mainSceneVC.moveRequirementToSprintBacklog(requirementVC);
        }
    }
    
    /**
     * Destructor disconnects from Database when MainSceneController Object 
     * gets garbage collected
     * @throws Throwable 
     */
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        IDatabaseController dbController = SQLiteDatabaseController.getInstance();
        dbController.disconnect();
    }
            
    
    /**
     * Main-Method which launches the Application
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @return Object mainSceneVC
     */
    public MainSceneViewController getMainSceneVC() {
        return mainSceneVC;
    }
}

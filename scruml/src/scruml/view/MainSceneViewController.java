package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scruml.controller.RequirementController;
import scruml.model.IARModel;
import scruml.model.RequirementModel;
import sun.font.EAttribute;

/**
 * FXML Controller class
 * 
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class MainSceneViewController implements Initializable {

    @FXML
    private VBox sprintHeaderVBox;
    @FXML
    private Label productBacklogLabel;
    @FXML
    private VBox productBacklogVBox;
    @FXML
    private VBox sprintVBox;
    @FXML
    private HBox headerOpenTasks;
    @FXML
    private HBox headerToDoTasks;
    @FXML
    private HBox headerDoneTasks;
    @FXML
    private Label doneRequirementsLabel;
    @FXML
    private VBox doneRequirementsVBox;
    
    
    private RequirementViewController currentDragRequirement;
    
    /**
     * Initializes the controller class.
     * binds the headers of ProductBacklog, SprintBacklog and Burndown to its content VBoxes.
     * Adds eventhandler to productBacklogLabel for Mouse entered, -exited, and -clicked, to manage "new Requirement" functionality.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productBacklogLabel.minWidthProperty().bind(productBacklogVBox.widthProperty());
        productBacklogLabel.prefWidthProperty().bind(productBacklogVBox.widthProperty());
        
        sprintHeaderVBox.minWidthProperty().bind(sprintVBox.widthProperty());
        sprintHeaderVBox.prefWidthProperty().bind(sprintVBox.widthProperty());
        
        doneRequirementsLabel.minWidthProperty().bind(doneRequirementsVBox.widthProperty());
        doneRequirementsLabel.prefWidthProperty().bind(doneRequirementsVBox.widthProperty());
        
        
        
        productBacklogLabel.addEventHandler(MouseEvent.MOUSE_ENTERED, 
        new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    productBacklogLabel.setText("New Requirement");
                    productBacklogLabel.getStyleClass().add("hover");
                }
        });
        
        
        productBacklogLabel.addEventHandler(MouseEvent.MOUSE_EXITED, 
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    productBacklogLabel.setText("Product Backlog");
                    productBacklogLabel.getStyleClass().remove(productBacklogLabel.getStyleClass().indexOf("hover"));
                }
        });
        
        productBacklogLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, 
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    try {
                        newRequirementClicked();
                    } catch (IOException ex) {
                        Logger.getLogger(MainSceneViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        });
        
        sprintVBox.setUserData(this);
        
    }

    /**
     * Constructor, loads the MainScene and calls initRequirements()
     * @param stage MainStage, where MainScene is shown.
     * @throws Exception 
     */
    public MainSceneViewController(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        loader.setController(this);
        Parent root = (Parent)loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        this.initRequirements();
        
        stage.show(); 
    }
    
    /**
     * Called when User clicks on "New Requirement". Creates a new RequirementViewController, sets its state to "STATE_CREATE" and
     * adds it to ProductBacklog
     * @throws IOException 
     */
    public void newRequirementClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RequirementView.fxml"));
        Parent root = (Parent)loader.load();
        
        RequirementViewController reqController = loader.getController();
        reqController.setViewForCreate(productBacklogVBox.widthProperty(), sprintVBox);
        reqController.getState().set(RequirementViewController.STATE_CREATE);
        
        productBacklogVBox.getChildren().add(reqController.getAnchorPane());
        
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
                this.addRequirement(model);
            }
            catch(IOException e) {
                System.err.println(e);
            }
        }
    }
    
    /**
     * This method adds a requirement to the product backlog pane.
     * @param model RequirementModel that should be added
     */
    private void addRequirement(IARModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RequirementView.fxml"));
        Parent root = (Parent)loader.load();
        
        RequirementViewController reqController = loader.getController();
        reqController.setRequirementModel((RequirementModel)model);
        reqController.setViewForProductBacklog(productBacklogVBox.widthProperty(), sprintVBox);

        reqController.getState().set(RequirementViewController.STATE_PRODUCT_BACKLOCK);
        root.setUserData(reqController);
        productBacklogVBox.getChildren().add(reqController.getAnchorPane());
    }
    
    /**
     * This method adds a priority to the currently selected requirement and 
     * triggers the changeRequirementPriority method.
     */
    private void initRequirementPriority() {
        
        
    }
    
    /**
     * is called when a RequirementView object is in Drag action and gets dropped over sprintVBox.
     * Removes the currentDragRequirement(if not null) from productBacklogVBox and adds it to sprintVBox after setting its State to "STATE_SPRINT_BACKLOG"
     */
    public void moveCurrentDragRequirementToSprintBacklog()
    {
        if (currentDragRequirement != null) {
            if (currentDragRequirement.getState().get() == RequirementViewController.STATE_PRODUCT_BACKLOCK)   {
                currentDragRequirement.setViewForSprintBacklog(headerOpenTasks.widthProperty(), headerToDoTasks.widthProperty(), headerDoneTasks.widthProperty());
                productBacklogVBox.getChildren().remove(currentDragRequirement.getAnchorPane());
                sprintVBox.getChildren().add(currentDragRequirement.getAnchorPane());
                currentDragRequirement.getState().set(RequirementViewController.STATE_SPRINT_BACKLOCK);
            }
        }
    }
    
    /**
     * This Method is called by a requirementView object which is currently in Drag action.
     * @param reqViewController 
     */
    public void setCurrentDragRequirement(RequirementViewController reqViewController)
    {
        currentDragRequirement = reqViewController;
    }
    
}

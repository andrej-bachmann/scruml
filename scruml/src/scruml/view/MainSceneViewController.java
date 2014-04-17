package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scruml.controller.RequirementController;
import scruml.model.IARModel;
import scruml.model.RequirementModel;

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
    private Button newRequirementButton;
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
    @FXML
    private ScrollPane productBacklogScrollPane;    
    @FXML
    private ScrollPane sprintScrollPane;    
    @FXML
    private ScrollPane burndownScrollPane;
    
    
    private RequirementViewController currentDragRequirement;
    
    /**
     * Initializes the controller class.
     * binds the headers of ProductBacklog, SprintBacklog and Burndown to its content VBoxes.
     * Adds eventhandler to productBacklogLabel for Mouse entered, -exited, and -clicked, to manage "new Requirement" functionality.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productBacklogLabel.minWidthProperty().bind(productBacklogScrollPane.widthProperty());
        productBacklogLabel.prefWidthProperty().bind(productBacklogScrollPane.widthProperty());
        
        sprintHeaderVBox.minWidthProperty().bind(sprintScrollPane.widthProperty());
        sprintHeaderVBox.prefWidthProperty().bind(sprintScrollPane.widthProperty());
        
        doneRequirementsLabel.minWidthProperty().bind(burndownScrollPane.widthProperty());
        doneRequirementsLabel.prefWidthProperty().bind(burndownScrollPane.widthProperty());
        
        
        
/*        productBacklogLabel.addEventHandler(MouseEvent.MOUSE_ENTERED, 
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
        });*/
        
        newRequirementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
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
        reqController.stateProperty().set(RequirementViewController.STATE_CREATE);
        
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

        reqController.stateProperty().set(RequirementViewController.STATE_PRODUCT_BACKLOCK);
        root.setUserData(reqController);
        productBacklogVBox.getChildren().add(reqController.getAnchorPane());
    }
    
    /**
     * is called when a RequirementView object is in Drag action and gets dropped over sprintVBox.
     * Removes the currentDragRequirement(if not null) from productBacklogVBox and adds it to sprintVBox after setting its State to "STATE_SPRINT_BACKLOG"
     */
    public void moveCurrentDragRequirementToSprintBacklog()
    {
        if (currentDragRequirement != null) {
            if (currentDragRequirement.stateProperty().get() == RequirementViewController.STATE_PRODUCT_BACKLOCK)   {
                currentDragRequirement.setViewForSprintBacklog(headerOpenTasks.widthProperty(), headerToDoTasks.widthProperty(), headerDoneTasks.widthProperty());
                productBacklogVBox.getChildren().remove(currentDragRequirement.getAnchorPane());
                sprintVBox.getChildren().add(currentDragRequirement.getAnchorPane());
                currentDragRequirement.stateProperty().set(RequirementViewController.STATE_SPRINT_BACKLOCK);
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

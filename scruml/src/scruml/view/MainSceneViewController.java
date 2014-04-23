package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scruml.controller.MainSceneController;

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
    @FXML 
    private ImageView trashIcon;

    private MainSceneController controller;
    private RequirementViewController currentDragRequirement;
    private Stage stage;
    
    /**
     * Initializes the controller class.
     * binds the headers of ProductBacklog, SprintBacklog and Burndown to its content VBoxes.
     * Adds eventhandler to productBacklogLabel for Mouse entered, -exited, and -clicked, to manage "new Requirement" functionality.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Bind heading labels to panes
        productBacklogLabel.minWidthProperty().bind(productBacklogScrollPane.widthProperty());
        productBacklogLabel.maxWidthProperty().bind(productBacklogScrollPane.widthProperty());
        
        sprintHeaderVBox.minWidthProperty().bind(sprintScrollPane.widthProperty());
        sprintHeaderVBox.maxWidthProperty().bind(sprintScrollPane.widthProperty());
        
        doneRequirementsLabel.minWidthProperty().bind(burndownScrollPane.widthProperty());
        doneRequirementsLabel.maxWidthProperty().bind(burndownScrollPane.widthProperty());
        
        //Set stage properties
        stage.setMinWidth(700);
        stage.setMinHeight(400);
        stage.setTitle("scruml ScrumBoard");
        
        //ActionListener
        newRequirementButton.setOnMouseClicked(new requirementButtonClicked());
               
    }

    /**
     * Constructor, loads the MainScene and calls initRequirements()
     * @param stage MainStage, where MainScene is shown.
     * @throws Exception 
     */
    public MainSceneViewController(Stage stage, MainSceneController controller) throws Exception {
        this.stage = stage;
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        loader.setController(this);
        Parent root = (Parent)loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
    }
    
    public void show() {
        stage.show();
    }
    
    /**
     * This method adds a requirement to the product backlog pane.
     * @param model RequirementModel that should be added
     */
    public void addRequirement(RequirementViewController reqController) throws IOException {
        productBacklogVBox.getChildren().add(reqController.getAnchorPane());
    }
    
    /**
     * is called when a RequirementView object is in Drag action and gets dropped over sprintVBox.
     * Removes the currentDragRequirement(if not null) from productBacklogVBox and adds it to sprintVBox after setting its State to "STATE_SPRINT_BACKLOG"
     */
    public void moveRequirementToSprintBacklog(RequirementViewController requirementVC)
    {
        requirementVC.setViewForSprintBacklog(headerOpenTasks.widthProperty(), headerToDoTasks.widthProperty(), headerDoneTasks.widthProperty());
        productBacklogVBox.getChildren().remove(requirementVC.getAnchorPane());
        sprintVBox.getChildren().add(requirementVC.getAnchorPane());
    }
    
    public void removeRequirement(RequirementViewController requirementVC)
    {
        productBacklogVBox.getChildren().remove(requirementVC.getAnchorPane());
        requirementVC.stateProperty().set(RequirementViewController.STATE_TRASH_ICON);  
    }
    
    public void activateDragAndDrop(final RequirementViewController currentDragRequirement) {
        this.currentDragRequirement = currentDragRequirement;
        trashIcon.setVisible(true);
        
        trashIcon.setOnDragOver(new trashIconDragOver());
        trashIcon.setOnDragDropped(new trashIconDragDropped());
        sprintVBox.setOnDragOver(new sprintVBoxDragOver());
        sprintVBox.setOnDragDropped(new sprintVBoxDragDropped());
        
    }
    
    public void deactivateDragAndDrop() {
        this.currentDragRequirement = null;
        trashIcon.setVisible(false);
        
        trashIcon.setOnDragOver(null);
        trashIcon.setOnDragDropped(null);
        sprintVBox.setOnDragOver(null);
        sprintVBox.setOnDragDropped(null);
    }
    
    public ReadOnlyDoubleProperty getProductBacklogWidth() {
        return productBacklogVBox.widthProperty();
    }
    
    class requirementButtonClicked implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent t) {
            controller.newRequirementClicked();
        }
    }
    
    class trashIconDragOver implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            System.out.println("onDragOver");
                
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        }
    }
    
    class trashIconDragDropped implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {

                System.out.println("onDragDropped");

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    success = true;
                }
                controller.requirementDraggedToTrash(currentDragRequirement);

                event.setDropCompleted(success);
                event.consume();
        }
    }
    
    class sprintVBoxDragOver implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            System.out.println("onDragOver");
                
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        }
    }
    
    class sprintVBoxDragDropped implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {

                System.out.println("onDragDropped");

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    success = true;
                }
                controller.requirementDraggedToSprintBacklog(currentDragRequirement);

                event.setDropCompleted(success);
                event.consume();
        }
    }
    
}

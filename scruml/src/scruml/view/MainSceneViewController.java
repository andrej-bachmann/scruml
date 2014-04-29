package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import scruml.model.UserModel;

/**
 * FXML Controller class
 * ViewController to manage MainScene
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
    @FXML
    private HBox productBacklogHeaderHBox;
    @FXML
    private ComboBox<UserModel> userComboBox;
    
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
        productBacklogHeaderHBox.minWidthProperty().bind(productBacklogScrollPane.widthProperty());
        productBacklogHeaderHBox.maxWidthProperty().bind(productBacklogScrollPane.widthProperty());
        
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
        userComboBox.setOnAction(new userComboBoxSelected());
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
    
    /**
     * shows the stage (Main Window)
     */
    public void show() {
        stage.show();
    }
    
    /**
     * This method adds a requirementView object to the product backlog pane as first item.
     * @param requirementVC RequirementViewController object that should be added
     * @throws IOException
     */
    public void addRequirement(RequirementViewController requirementVC) throws IOException {
        productBacklogVBox.getChildren().add(0, requirementVC.getAnchorPane());
    }
    
    /**
     * This method populates the user combobox with data.
     * @param users List of UserModel that gets inserted in the combobox
     */
    public void setUsers(List<UserModel> users) {
        this.userComboBox.getItems().addAll(users);
    }
    
    /**
     * is called when a RequirementView object is in Drag action and gets dropped over sprintVBox.
     * Removes the currentDragRequirement(if not null) from productBacklogVBox and adds it to sprintVBox after setting its State to "STATE_SPRINT_BACKLOG"
     * @param requirementVC
     */
    public void moveRequirementToSprintBacklog(RequirementViewController requirementVC)
    {
        requirementVC.setViewForSprintBacklog(headerOpenTasks.widthProperty(), headerToDoTasks.widthProperty(), headerDoneTasks.widthProperty());
        productBacklogVBox.getChildren().remove(requirementVC.getAnchorPane());
        sprintVBox.getChildren().add(requirementVC.getAnchorPane());
    }
    
    /**
     * is called when a RequirementView object is in Drag action and gets dropped over the trash box.
     * Only works if the requirement is an item of the productBacklogVBox
     * @param requirementVC RequirementViewController object that should be removed
     */
    public void removeRequirement(RequirementViewController requirementVC)
    {
        productBacklogVBox.getChildren().remove(requirementVC.getAnchorPane());
        requirementVC.stateProperty().set(RequirementViewController.STATE_TRASH_ICON);  
    }
    
    /**
     * is called when drag action on a RequirementView object is detected. Sets the currentDragRequirement and shows the trashIcon.
     * Also adds the drag event handler to the targets (trashIcon and sprintVBox) of the drag action.
     * @param currentDragRequirement the RequirementViewController where drag action is detected
     */
    public void activateDragAndDrop(final RequirementViewController currentDragRequirement) {
        this.currentDragRequirement = currentDragRequirement;
        trashIcon.setVisible(true);
        
        trashIcon.setOnDragOver(new trashIconDragOver());
        trashIcon.setOnDragDropped(new trashIconDragDropped());
        sprintVBox.setOnDragOver(new sprintVBoxDragOver());
        sprintVBox.setOnDragDropped(new sprintVBoxDragDropped());
        
    }
    
    /**
     * is called when drag on a RequirementView object is released. Hides trash icon and sets currentDragRequirement to null.
     * Also removes drag event handler from targets.
     */
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
    
    /**
     * Eventhandler to manage Click on "new requirement" button
     */
    class requirementButtonClicked implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent t) {
            controller.newRequirementClicked();
        }
    }
    
    /**
     * EventHandler which gets triggered when an entry in the user combobox gets
     * selected.
     */
    class userComboBoxSelected implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent t) {
            controller.setActiveUser(userComboBox.getValue());
        }
    }
    
    /**
     * Is called when the Drag and Drop of the Requirement is currently at the 
     * trash Icon
     */
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
    
    /**
     * Is called when the Drag and Drop of the Requirement is released above
     * the trash icon
     */
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
    
    /**
     * Eventhandler to manage Requirement Drag action over sprintVBox
     */
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
    
    /**
     * Eventhandler to manage Drop of a Requirement over sprintVBox
     */
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

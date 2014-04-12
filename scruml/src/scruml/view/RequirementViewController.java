package scruml.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import scruml.controller.RequirementController;
import scruml.model.RequirementModel;

/**
 * FXML Controller class
 *
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RequirementViewController implements Initializable {

    public static final int STATE_CREATE = 0;
    public static final int STATE_PRODUCT_BACKLOCK = 1;
    public static final int STATE_SPRINT_BACKLOCK = 2;

    private IntegerProperty state = new SimpleIntegerProperty();
    private RequirementModel requirementModel;
    
    // This reference for event-handlers
    private final RequirementViewController thisObject;
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox vBox;
    @FXML
    private HBox requirementHBox;
    @FXML
    private HBox taskHBox;
    @FXML
    private Pane requirementOpen;
    @FXML
    private Pane requirementToDo;
    @FXML
    private Pane requirementDone;
    @FXML
    private Pane taskOpen;     
    @FXML
    private Pane taskToDo;
    @FXML
    private Pane taskDone;
    @FXML
    private VBox dataVBox;     
    @FXML
    private Label titleLabel;     
    @FXML
    private Label descriptionLabel;
    
    private TextField titleTextField=new TextField("Title");
    private TextField descriptionTextField=new TextField("Description");
    private Button saveButton=new Button("Save Requirement");

    
    public RequirementViewController()
    {
        thisObject=this;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        requirementOpen.setStyle("-fx-background-color: black;");
        requirementToDo.setStyle("-fx-background-color: red;");
        requirementDone.setStyle("-fx-background-color: white;");
        taskToDo.setStyle("-fx-background-color: black;");
        taskOpen.setStyle("-fx-background-color: white;");
        taskDone.setStyle("-fx-background-color: red;");
        
        titleLabel.setStyle("-fx-text-fill: white;");
        descriptionLabel.setStyle("-fx-text-fill: white;");
    }   
    
    public void setViewForCreate(final ReadOnlyDoubleProperty productBacklogWidth, final VBox sprintVBox) {
        this.setViewForProductBacklog(productBacklogWidth, sprintVBox);
        dataVBox.getChildren().remove(titleLabel);
        dataVBox.getChildren().remove(descriptionLabel);
        dataVBox.getChildren().add(titleTextField);
        dataVBox.getChildren().add(descriptionTextField);
        dataVBox.getChildren().add(saveButton);
        
        titleTextField.focusTraversableProperty().set(true);
        descriptionTextField.focusTraversableProperty().set(true);
        
        titleTextField.requestFocus();
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            RequirementController reqController = new RequirementController();
            try {
                thisObject.setRequirementModel(reqController.createRequirement(titleTextField.textProperty().get(), descriptionTextField.textProperty().get()));
                thisObject.setViewForProductBacklog(productBacklogWidth, sprintVBox);
                thisObject.state.set(STATE_PRODUCT_BACKLOCK);
                
                dataVBox.getChildren().remove(titleTextField);
                dataVBox.getChildren().remove(descriptionTextField);
                dataVBox.getChildren().remove(saveButton);  
                dataVBox.getChildren().add(titleLabel);
                dataVBox.getChildren().add(descriptionLabel);
            } catch (Exception ex) {
                Logger.getLogger(RequirementViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    }
    
    public void setViewForProductBacklog(ReadOnlyDoubleProperty productBacklogWidth, VBox sprintVBox) {

        
        
        taskHBox.maxHeightProperty().set(0);
        vBox.maxHeightProperty().set(200);
        requirementHBox.minHeightProperty().bind(vBox.heightProperty());
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
        
        VBox target =  sprintVBox;        
        final MainSceneViewController mcVC = (MainSceneViewController)target.getUserData();
        
        vBox.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");
                
                /* allow any transfer mode */
                Dragboard db = requirementOpen.getParent().startDragAndDrop(TransferMode.ANY);
                mcVC.setCurrentDragRequirement(thisObject);
                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(new String("Requirement"));
                db.setContent(content);
                
                event.consume();
            }
        });
        
        target.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");
                
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                if (event.getGestureSource() != vBox &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                
                event.consume();
            }
        });

        target.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != vBox &&
                        event.getDragboard().hasString()) {
                    //titleLabel.textProperty().set("LOL");
                }
                
                event.consume();
            }
        });

        target.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                //titleLabel.textProperty().set("LOL");
                
                event.consume();
            }
        });
        
        
        target.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    //titleLabel.setText(db.getString());
                    success = true;
                }
                
                mcVC.moveCurrentDragRequirementToSprintBacklog();

                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                
                event.consume();
            }
        });

        requirementOpen.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    //titleLabel.setText("");
                }
                
                event.consume();
            }
        });

    }
    
    public void setViewForSprintBacklog(ReadOnlyDoubleProperty open, ReadOnlyDoubleProperty todo, ReadOnlyDoubleProperty done) {
        taskHBox.setMaxHeight(500);
        taskHBox.minHeightProperty().bind(vBox.heightProperty().divide(2));
        requirementHBox.minHeightProperty().unbind();
        requirementHBox.minHeightProperty().bind(vBox.heightProperty().divide(2));
        
        requirementToDo.minWidthProperty().bind(todo);
        requirementDone.minWidthProperty().bind(done);
        requirementOpen.minWidthProperty().bind(open);
        taskToDo.minWidthProperty().bind(todo);
        taskDone.minWidthProperty().bind(done);
        taskOpen.minWidthProperty().bind(open);
        
        requirementToDo.maxWidthProperty().bind(todo);
        requirementDone.maxWidthProperty().bind(done);
        requirementOpen.maxWidthProperty().bind(open);
        taskToDo.maxWidthProperty().bind(todo);
        taskDone.maxWidthProperty().bind(done);
        taskOpen.maxWidthProperty().bind(open);
        state.set(RequirementViewController.STATE_SPRINT_BACKLOCK);
    }

    public IntegerProperty getState() {
        return state;
    }   
    
    /**
     * This method sets the requirement model and binds properties to the view.
     * @param requirementModel Model that gets bind to the view.
     */
    public void setRequirementModel(RequirementModel requirementModel) {
        this.requirementModel = requirementModel;
        this.titleLabel.textProperty().bind(requirementModel.titleProperty());
        this.descriptionLabel.textProperty().bind(requirementModel.descriptionProperty());
    }
    
    public AnchorPane getAnchorPane()    {
        return anchorPane;
    }
    
}

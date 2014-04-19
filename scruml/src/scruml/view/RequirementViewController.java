package scruml.view;

import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    @FXML
    private Label priorityLabel;
    @FXML
    private ChoiceBox priorityMenu;
    
    
    
    
    private TextField titleTextField=new TextField("Title");
    private TextField descriptionTextField=new TextField("Description");
    private Button saveButton=new Button("Save Requirement");
    
    private RequirementController reqController;
    
    

    
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
        priorityMenu.setItems(FXCollections.observableArrayList
        ("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"));
    }  
    
    /**
     * Sets the RequirementView for ProductBacklog, Height of taskHBox is set to 0,
     * requirementToDo and requirementDone cells width is set to 0, requirementopen cells width is bind to productBacklog labels widthProperty
     * Replaces titleLabel with titleTextField and descriptionLabel with descriptionTextField and adds Save Button in dataVBox.
     * Adds eventHandler to saveButton, which creates RequirementController (which further creates a RequirementModel) on button click.
     * After Button click state is set to STATE_PRODUCTBACKLOG and textfields are replaced with labels.
     * @param productBacklogWidth widthProperty of ProductBacklog label
     * @param sprintVBox The target of Drag and Drop for Requirement
     */
    
    public void setViewForCreate(final ReadOnlyDoubleProperty productBacklogWidth, final VBox sprintVBox) {
        taskHBox.maxHeightProperty().set(0);
        vBox.maxHeightProperty().set(200);
        requirementHBox.minHeightProperty().bind(vBox.heightProperty());
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
        
        dataVBox.getChildren().remove(titleTextField);
        dataVBox.getChildren().remove(descriptionTextField);   
        dataVBox.getChildren().remove(titleLabel);
        dataVBox.getChildren().remove(descriptionLabel);
        dataVBox.getChildren().remove(priorityLabel);
        dataVBox.getChildren().remove(priorityMenu);
        dataVBox.getChildren().remove(saveButton);
        titleTextField.getStyleClass().add("titleTextField");
        dataVBox.getChildren().add(titleTextField);
        dataVBox.getChildren().add(descriptionTextField);
        dataVBox.getChildren().add(priorityLabel);
        dataVBox.getChildren().add(priorityMenu);
        dataVBox.getChildren().add(saveButton);
        
        titleTextField.focusTraversableProperty().set(true);
        descriptionTextField.focusTraversableProperty().set(true);
        
        titleTextField.requestFocus();
        
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            reqController = new RequirementController();
            try {
                if (requirementModel==null) { 
                    if (priorityMenu.getSelectionModel().getSelectedIndex()==-1)
                        thisObject.setRequirementModel(reqController.createRequirement(titleTextField.textProperty().get(),
                            descriptionTextField.textProperty().get(), -1));
                    else
                        thisObject.setRequirementModel(reqController.createRequirement(titleTextField.textProperty().get(),
                            descriptionTextField.textProperty().get(), priorityMenu.getSelectionModel().getSelectedIndex()+1));
                }
                thisObject.setViewForProductBacklog(productBacklogWidth, sprintVBox);
                thisObject.state.set(STATE_PRODUCT_BACKLOCK);
                
                dataVBox.getChildren().remove(titleTextField);
                dataVBox.getChildren().remove(descriptionTextField);                
                dataVBox.getChildren().remove(priorityLabel);
                dataVBox.getChildren().remove(priorityMenu);
                dataVBox.getChildren().remove(saveButton);  
                dataVBox.getChildren().add(titleLabel);
                dataVBox.getChildren().add(descriptionLabel);
                dataVBox.getChildren().add(priorityLabel);
                dataVBox.getChildren().add(priorityMenu);
            } catch (Exception ex) {
                Logger.getLogger(RequirementViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    }
    
    /**
     * Sets the RequirementView for ProductBacklog, Height of taskHBox is set to 0,
     * requirementToDo and requirementDone cells width is set to 0, requirementopen cells width is bind to productBacklog labels widthProperty
     * Drag and Drop functionality added to Requirement and sprintVBox as target of Drag and Drop
     * @param productBacklogWidth widthProperty of ProductBacklog label
     * @param sprintVBox The target of Drag and Drop for Requirement
     */
    public void setViewForProductBacklog(final ReadOnlyDoubleProperty productBacklogWidth, final VBox sprintVBox) {        
        taskHBox.maxHeightProperty().set(0);
        vBox.maxHeightProperty().set(200);
        requirementHBox.minHeightProperty().bind(vBox.heightProperty());
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
        
        requirementOpen.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
               setViewForCreate(productBacklogWidth, sprintVBox);
            }
        });
        
        priorityMenu.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number value, Number new_value){
                if (reqController == null)
                    reqController = new RequirementController();
                reqController.changePriority(requirementModel, new_value.intValue()+1);
            };
        });
        
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
    
    
    /**
     * Sets the RequirementView for sprintBacklog. One Requirement has two rows and three coloums, the second row is where the Tasks will be placed.
     * 
     * Later there will be two ore more views for sprintBacklog, one "collapsed" where the taskHBox is invisible and one "expanded" where it's not.
     * 
     * @param open widthProperty of open Tasks coloumn
     * @param todo widthProperty of to do coloumn
     * @param done widthProperty of done coloumn
     */
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
        
        priorityMenu.disableProperty().setValue(Boolean.TRUE);
        state.set(RequirementViewController.STATE_SPRINT_BACKLOCK);
    }

    public IntegerProperty stateProperty() {
        return state;
    }   
    
    /**
     * This method sets the requirement model and binds properties to the view.
     * @param requirementModel Model that gets bind to the view.
     */
    public void setRequirementModel(final RequirementModel requirementModel) {
        this.requirementModel = requirementModel;
        this.titleLabel.textProperty().bind(requirementModel.titleProperty());
        this.descriptionLabel.textProperty().bind(requirementModel.descriptionProperty()); 
        this.titleTextField.textProperty().bindBidirectional(requirementModel.titleProperty());
        this.descriptionTextField.textProperty().bindBidirectional(requirementModel.descriptionProperty());
        priorityMenu.getSelectionModel().select(requirementModel.priorityProperty().get()-1);
        //Bindings.bind(this.priorityMenu.valueProperty(), requirementModel.priorityProperty(), new NumberStringConverter());
    }
    
     /**
     * This method sets the gets the chosen priority and gives it to the model
     */
/*    public void setChosenPriority (RequirementModel requirementModel) {
        this.priorityMenu.g
        int selectedItem = (int)priorityMenu.getSel
        requirementModel.setPriority(this.priorityMenu.);
    }*/
    
    public AnchorPane getAnchorPane()    {
        return anchorPane;
    }

    private void priorityMenu(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

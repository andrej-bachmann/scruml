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
import javafx.scene.image.ImageView;
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
    public static final int STATE_TRASH_ICON = 3;

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
    private VBox taskOpen;     
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
    
    private boolean isExpanded = false;
    
    
    
    
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
     * Adds eventHandler to saveButton, which creates (if not already exists) RequirementController 
     * (which further creates or saves the RequirementModel) on button click.
     * After Button click state is set to STATE_PRODUCTBACKLOG and textfields are replaced with labels.
     * Also the onDragDetected-handler is set to null, to avoid dragging the requirement when it's in editing mode.
     * @param productBacklogWidth widthProperty of ProductBacklog label
     * @param sprintVBox The target of Drag and Drop for Requirement
     */
    
    public void setViewForEditing(final ReadOnlyDoubleProperty productBacklogWidth, final VBox sprintVBox, final ImageView trashIcon) {
        
        //Do not show: task related stuff, ToDo- and Done-Pane
        taskHBox.setPrefHeight(0);
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        //Bind width of a requirement to width of productBacklog pane
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
        
        anchorPane.onMouseClickedProperty().set(null);
        
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
                thisObject.setViewForProductBacklog(productBacklogWidth, sprintVBox, trashIcon);
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
        vBox.setOnDragDetected(null);
    }    
    
    /**
     * Sets the RequirementView for ProductBacklog, Height of taskHBox is set to 0,
     * requirementToDo and requirementDone cells width is set to 0, requirementopen cells width is bind to productBacklog labels widthProperty
     * Drag and Drop functionality added to Requirement and sprintVBox as target of Drag and Drop
     * @param productBacklogWidth widthProperty of ProductBacklog label
     * @param sprintVBox The target of Drag and Drop for Requirement
     */
    public void setViewForProductBacklog(final ReadOnlyDoubleProperty productBacklogWidth, final VBox sprintVBox, final ImageView trashIcon) {        
        
        //Do not show: task related stuff, ToDo- and Done-Pane
        taskHBox.setPrefHeight(0);
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        
        //Bind width of a requirement to width of productBacklog pane
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
        
        anchorPane.onMouseClickedProperty().set(null);
        
        requirementOpen.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
               setViewForEditing(productBacklogWidth, sprintVBox, trashIcon);
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
        ImageView trash = trashIcon;
        
        
        
        vBox.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");
                mcVC.setTrashIcon(true);
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
        
        vBox.setOnDragDone(new EventHandler <DragEvent>(){
            public void handle (DragEvent event){
                /* drag was released, make trashIcon invisible*/
                mcVC.setTrashIcon(false);
            }
        });
        
        
        
        trash.setOnDragOver(new EventHandler <DragEvent>() {
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
        
        trash.setOnDragDropped(new EventHandler <DragEvent>() {
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
                /*delete Requirement in Backlog */          
                mcVC.moveCurrentDragRequirementToTrash();
                /* delete Requirement Model in Database*/
                if (reqController == null)
                    reqController = new RequirementController();
                if (thisObject.state.get() == STATE_PRODUCT_BACKLOCK){
                    mcVC.moveCurrentDragRequirementToTrash();
                    reqController.deleteRequirement(requirementModel);
                }

                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                
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
        
        //Do not show: task related stuff
        taskHBox.setMinHeight(0);
        taskHBox.setMaxHeight(0);
        
        //Bind minWidth of requirementPanes and taskPanes to corresponding columns
        requirementToDo.minWidthProperty().bind(todo);
        requirementDone.minWidthProperty().bind(done);
        requirementOpen.minWidthProperty().bind(open);
        taskToDo.minWidthProperty().bind(todo);
        taskDone.minWidthProperty().bind(done);
        taskOpen.minWidthProperty().bind(open);
        
        //Bind maxWidth of requirementPanes and taskPanes to corresponding columns
        requirementToDo.maxWidthProperty().bind(todo);
        requirementDone.maxWidthProperty().bind(done);
        requirementOpen.maxWidthProperty().bind(open);
        taskToDo.maxWidthProperty().bind(todo);
        taskDone.maxWidthProperty().bind(done);
        taskOpen.maxWidthProperty().bind(open);
        
        priorityMenu.disableProperty().setValue(Boolean.TRUE);
        state.set(RequirementViewController.STATE_SPRINT_BACKLOCK);
        
        //DummyTasks:
        final Pane task1 = new Pane();
        final Pane task2 = new Pane();
        
        task1.prefHeightProperty().set(50);
        task1.styleProperty().set("-fx-background-color: black;");
        
        task2.prefHeightProperty().set(50);
        task2.styleProperty().set("-fx-background-color: red;");
        
        task1.visibleProperty().set(false);
        task2.visibleProperty().set(false);
                
        taskOpen.getChildren().add(task1);
        taskOpen.getChildren().add(task2);
        
        requirementOpen.onMouseClickedProperty().set(null);
        anchorPane.onMouseClickedProperty().set(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                if (!isExpanded) {                    
                    taskHBox.minHeightProperty().unbind();
                    taskHBox.setMinHeight(100);
                    
                    taskHBox.maxHeightProperty().unbind();
                    taskHBox.setMaxHeight(100);
                    isExpanded = true;
                }
                else {                    
                    taskHBox.minHeightProperty().unbind();
                    taskHBox.setMinHeight(0);
                    
                    taskHBox.maxHeightProperty().unbind();
                    taskHBox.setMaxHeight(0);
                    isExpanded = false;
                }
                task1.visibleProperty().set(isExpanded);
                task2.visibleProperty().set(isExpanded);
            }
        });        
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
    }
    
    public AnchorPane getAnchorPane()    {
        return anchorPane;
    }    

    public void delete() {
        this.requirementModel = requirementModel;
    }
}

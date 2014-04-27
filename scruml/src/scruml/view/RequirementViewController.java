package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    public static final int STATE_TRASH_ICON = 3;

    private RequirementController reqController;
    private RequirementModel requirementModel;
    private ReadOnlyDoubleProperty productBacklogWidth;
    private IntegerProperty state = new SimpleIntegerProperty();
    private TextField titleTextField = new TextField();
    private TextField descriptionTextField = new TextField();
    private Button saveButton = new Button();
    private boolean isExpanded = false;
    
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
    private ChoiceBox<String> priorityMenu;
    @FXML
    private Label effortLabel;
    @FXML
    private ChoiceBox<String> effortMenu;
    @FXML
    private HBox priorityEffortLabelHbox;
    @FXML 
    private HBox priorityEffortMenu;
    
    //DummyTasks:
    private Pane task1 = new Pane();
    private Pane task2 = new Pane();
    
    /**
     * Sets the requirementController and loads the RequirementView.fxml
     * @param reqController RequirementController that gets notified on any user action with the RequirementView object
     * @throws IOException 
     */
    public RequirementViewController(RequirementController reqController) throws IOException {
        this.reqController = reqController;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RequirementView.fxml"));
        loader.setController(this);
        Parent root = (Parent)loader.load();
    }
    
    /**
     * Adds Items to the priorityMenu, sets promptTexts to TextFields, sets the Backgroundcolor of the dummy-tasks
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        priorityMenu.setItems(FXCollections.observableArrayList
        ("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"));
        effortMenu.setItems(FXCollections.observableArrayList
        ("1","2","3","5","8","13"));
        
        titleTextField.setPromptText("Title");
        descriptionTextField.setPromptText("Description");
        saveButton.setText("Save Requirement");
        task1.prefHeightProperty().set(50);
        task1.styleProperty().set("-fx-background-color: black;");
        
        task2.prefHeightProperty().set(50);
        task2.styleProperty().set("-fx-background-color: red;");
        
        task1.visibleProperty().set(false);
        task2.visibleProperty().set(false);
                
        taskOpen.getChildren().add(task1);
        taskOpen.getChildren().add(task2);
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
    
    /**
     * Method to set the RequirementView for editing.
     * Binding the requirementOpen-panes (inherits the dataVBox) with to productBacklogWidth
     * remove all elements and only add the textFields and choiceboxes for data input
     * Adds Eventhandler to savebutton and removes them from anchorPane (mouseclick event for editing)
     * and vBox (onDragDetected event)
     */
    public void setViewForEditing() {
       
        this.state.set(STATE_CREATE);
        if(requirementModel==null)
            this.setRequirementModel(new RequirementModel());
        
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
        dataVBox.getChildren().remove(priorityEffortLabelHbox);
        dataVBox.getChildren().remove(priorityEffortMenu);
        dataVBox.getChildren().remove(saveButton);
        titleTextField.getStyleClass().add("titleTextField");
        dataVBox.getChildren().add(titleTextField);
        dataVBox.getChildren().add(descriptionTextField);
        dataVBox.getChildren().add(priorityEffortLabelHbox);
        dataVBox.getChildren().add(priorityEffortMenu);
        dataVBox.getChildren().add(saveButton);
        
        titleTextField.focusTraversableProperty().set(true);
        descriptionTextField.focusTraversableProperty().set(true);
        
        titleTextField.requestFocus();
        
        anchorPane.onMouseClickedProperty().set(null);
        saveButton.setOnAction(new saveButtonClicked());        
        vBox.setOnDragDetected(null);
    }    
    
    /**
     * calls "setViewForProductBacklog", removes the textFields and adds the labels
     */
    public void endEditing() {
        this.setViewForProductBacklog();
        
        dataVBox.getChildren().remove(titleTextField);
        dataVBox.getChildren().remove(descriptionTextField);                
        dataVBox.getChildren().remove(priorityEffortLabelHbox);
        dataVBox.getChildren().remove(priorityEffortMenu);

        dataVBox.getChildren().remove(saveButton);  
        dataVBox.getChildren().add(titleLabel);
        dataVBox.getChildren().add(descriptionLabel);
        dataVBox.getChildren().add(priorityEffortLabelHbox);
        dataVBox.getChildren().add(priorityEffortMenu);

    }
    
    /**
     * Sets the RequirementView for ProductBacklog, Height of taskHBox is set to 0,
     * requirementToDo and requirementDone cells width is set to 0, requirementopen cells width is bind to productBacklog labels widthProperty
     * Drag and Drop functionality added to Requirement and sprintVBox as target of Drag and Drop
     * @param productBacklogWidth widthProperty of ProductBacklog label
     * @param sprintVBox The target of Drag and Drop for Requirement
     */
    public void setViewForProductBacklog() {        
        
        
        this.state.set(STATE_PRODUCT_BACKLOCK);
        
        //Do not show: task related stuff, ToDo- and Done-Pane
        taskHBox.setPrefHeight(0);
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        
        //Bind width of a requirement to width of productBacklog pane
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
        
        anchorPane.onMouseClickedProperty().set(null);
        requirementOpen.setOnMouseClicked(new requirementClicked());
        priorityMenu.valueProperty().addListener(new priorityMenuChanged());
        effortMenu.valueProperty().addListener(new effortMenuChanged());
        
        vBox.setOnDragDetected(new vBoxDragDetected());
        vBox.setOnDragDone(new vBoxDragDone());
        
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
        effortMenu.disableProperty().setValue(Boolean.TRUE);
        state.set(RequirementViewController.STATE_SPRINT_BACKLOCK);
        
        requirementOpen.onMouseClickedProperty().set(null);
        anchorPane.onMouseClickedProperty().set(new sprintBacklogExpand());      
    }

    public IntegerProperty stateProperty() {
        return state;
    }   
    
    /**
     * This method sets the requirement model and binds properties bidirectional to the view.
     * @param requirementModel Model that gets bind to the view.
     */
    public void setRequirementModel(final RequirementModel requirementModel) {
        this.requirementModel = requirementModel;
        this.titleLabel.textProperty().bind(requirementModel.titleProperty());
        this.descriptionLabel.textProperty().bind(requirementModel.descriptionProperty()); 
        this.titleTextField.textProperty().bindBidirectional(requirementModel.titleProperty());
        this.descriptionTextField.textProperty().bindBidirectional(requirementModel.descriptionProperty());
        this.priorityMenu.valueProperty().bindBidirectional(requirementModel.priorityProperty());
        this.effortMenu.valueProperty().bindBidirectional(requirementModel.effortProperty());
    }

    public RequirementModel getRequirementModel() {
        return requirementModel;
    }
    
    public AnchorPane getAnchorPane() {
        return anchorPane;
    }    

    public void setProductBacklogWidth(ReadOnlyDoubleProperty productBacklogWidth) {
        this.productBacklogWidth = productBacklogWidth;
    }
    
    /**
     * Eventhandler for dragDetected event of vBox.
     * Notifies the requirementController and forwards reference of RequirementView object
     */
    class vBoxDragDetected implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
                
            System.out.println("onDragDetected");

            reqController.dragAndDropStarted(RequirementViewController.this);

            Dragboard db = requirementOpen.getParent().startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(new String("Requirement"));
            db.setContent(content);

            event.consume();
        }
    }
    
    /**
     * Notifies the requirementController about end of drag and drop
     */
    class vBoxDragDone implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent t) {
            reqController.dragAndDropStopped();
        }
    }
    
    /**
     * Eventhandler for click on save
     */
    class saveButtonClicked implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            reqController.createRequirement(requirementModel);
        }
        
    }
    
    /**
     * Eventhandler for click on the requirementView object in productBacklog.
     */
    class requirementClicked implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            setViewForEditing();
        }
        
    }
    
    /**
     * Eventhandler for click on the requirementView object in sprintBacklog
     * Expands the Requirment and shows the Tasks
     */
    class sprintBacklogExpand implements EventHandler<MouseEvent> {
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
    }
    
    /**
     * Changelistener for priorityMenu
     * Notifies the requirementController to update the Requirement
     */
    class priorityMenuChanged implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            reqController.updateRequirement(requirementModel);
        }

    }
    
     /**
     * Changelistener for effortMenu
     * Notifies the requirementController to update the Requirement
     */
    class effortMenuChanged implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            reqController.updateRequirement(requirementModel);
        }

    }

}

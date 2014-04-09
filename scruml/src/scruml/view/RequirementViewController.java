/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author gollerda
 */
public class RequirementViewController implements Initializable {

     public static int STATE_PRODUCT_BACKLOCK = 0;
     public static int STATE_SPRINT_BACKLOCK = 1;
     
     private IntegerProperty state = new SimpleIntegerProperty();
     
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

     
     public RequirementViewController()
     {
         
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
        
        titleLabel.setStyle("-fx-color: white;");
    }    
    
    public void stateChanged(int oldVal, int newVal) {
        
    }
    
    public void setViewForProductBacklog(ReadOnlyDoubleProperty productBacklogWidth) {
        taskHBox.maxHeightProperty().set(0);
        vBox.maxHeightProperty().set(200);
        requirementHBox.minHeightProperty().bind(vBox.heightProperty());
        requirementToDo.setPrefWidth(0);
        requirementDone.setPrefWidth(0);
        requirementOpen.minWidthProperty().bind(productBacklogWidth);
        requirementOpen.maxWidthProperty().bind(productBacklogWidth);
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
    }
    
    public Pane getRequirementOpenPane() {
        return requirementOpen;
    }
    
    public Pane getRequirementToDoPane() {
        return requirementToDo;
    }
    
    public Pane getRequirementDonePane() {
        return requirementDone;
    }
    
    public Pane getTaskOpenPane() {
        return taskOpen;
    }
    
    public Pane getTaskToDoPane() {
        return taskToDo;
    }
    
    public Pane getTaskDonePane() {
        return taskDone;
    }
    
    public HBox getRequirementHBox()
    {
        return requirementHBox;
    }
    
    public IntegerProperty getState() {
        return state;
    }   
}

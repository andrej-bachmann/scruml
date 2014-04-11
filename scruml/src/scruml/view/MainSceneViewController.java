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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    
    /**
     * Initializes the controller class.
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
        
    }

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

        reqController.getState().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if ((int)oldValue==RequirementViewController.STATE_PRODUCT_BACKLOCK && (int)newValue==RequirementViewController.STATE_SPRINT_BACKLOCK)
                    try {
                        moveRequirementClicked(new ActionEvent());
                    } catch (IOException ex) {
                        Logger.getLogger(MainSceneViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });
        root.setUserData(reqController);
        productBacklogVBox.getChildren().add(root);
    }
    
    @FXML
    private void newRequirementClicked(ActionEvent event) throws IOException {
        this.addRequirement(new RequirementModel()); //Adds dummy entry
    }
    
    @FXML
    private void moveRequirementClicked(ActionEvent event) throws IOException {
        if(productBacklogVBox.getChildren().size() > 0) {
            Node n = productBacklogVBox.getChildren().get(0);
            RequirementViewController reqController = (RequirementViewController)n.getUserData();
            reqController.setViewForSprintBacklog(headerOpenTasks.widthProperty(), headerToDoTasks.widthProperty(), headerDoneTasks.widthProperty());
            productBacklogVBox.getChildren().remove(n);
            sprintVBox.getChildren().add(n);
        }
    }
    
}

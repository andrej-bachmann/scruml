package scruml.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 */
public class MainSceneViewController implements Initializable {

    //@FXML
    //private AnchorPane anchorPane;
    /**
     * Initializes the controller class.
     */
    
    public MainSceneViewController(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        //loader.setRoot(this);
        loader.setController(this);
        Parent root = (Parent)loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); 
    }
    
    @FXML
    private void newSprintClicked(ActionEvent event) throws IOException {
        Button button = (Button)event.getSource();
        button.textProperty().set("TROLOOLOLO");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

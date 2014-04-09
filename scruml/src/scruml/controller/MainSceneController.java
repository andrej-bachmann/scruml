package scruml.controller;
import scruml.view.MainSceneViewController;

import javafx.application.Application;
import javafx.stage.Stage;


/** 
 * MainSceneController extends javafx Application class. Inherits the main(String[]args)
 * 
 * @author David Goller
 */
public class MainSceneController extends Application {
    private IDatabaseController dbController;
    
    /**
     * Constructor connects to Database and creates new MainSceneViewController,
     * which shows the MainScene
     * @param stage MainStage Object
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        dbController = SQLiteDatabaseController.getInstance();
        dbController.connect();
        new MainSceneViewController(stage); 
    }
    
    /**
     * Destructor disconnects from Database when MainSceneController Object 
     * gets garbage collected
     * @throws Throwable 
     */
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        dbController.disconnect();
    }
            
    
    /**
     * Main-Method which launches the Application
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

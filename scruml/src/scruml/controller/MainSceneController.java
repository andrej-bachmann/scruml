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
    
    private MainSceneViewController mainSceneVC;
    /**
     * start Method connects to Database and creates new MainSceneViewController,
     * which shows the MainScene
     * @param stage MainStage Object
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        IDatabaseController dbController = SQLiteDatabaseController.getInstance();
        stage.setMinWidth(700);
        stage.setMinHeight(400);
        stage.setTitle("scruml ScrumBoard");
        dbController.connect();
        mainSceneVC = new MainSceneViewController(stage); 
    }
    
    public MainSceneViewController getMainSceneVC()
    {
        return mainSceneVC;
    }
    
    /**
     * Destructor disconnects from Database when MainSceneController Object 
     * gets garbage collected
     * @throws Throwable 
     */
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        IDatabaseController dbController = SQLiteDatabaseController.getInstance();
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

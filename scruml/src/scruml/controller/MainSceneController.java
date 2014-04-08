package scruml.controller;
import scruml.view.MainSceneViewController;

import javafx.application.Application;
import javafx.stage.Stage;


/** 
 * MainSceneController extends javafx application class. Inherits the main(String[]args)
 * 
 * @author David Goller
 */
public class MainSceneController extends Application{
    private IDatabaseController dbController;
    /**
     * 
     * @param stage 
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        dbController = SQLiteDatabaseController.getInstance();
        dbController.connect();
        new MainSceneViewController(stage); 
    }
    
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        dbController.disconnect();
    }
            
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

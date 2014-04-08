/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;
import scruml.view.MainSceneViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/** 
 * MainSceneController extends javafx application class. Inherits the main(String[]args)
 * 
 * @author David Goller
 */
public class MainSceneController extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        new MainSceneViewController(stage); 
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}

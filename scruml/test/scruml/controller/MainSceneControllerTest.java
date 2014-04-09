/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author gollerda
 */
public class MainSceneControllerTest {
    
    public MainSceneControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class MainSceneController.
     */
    @Test
    public void testStart() throws Exception {
        System.out.println("start");
        Stage stage = new Stage();
        MainSceneController instance = new MainSceneController();
        instance.start(stage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of finalize method, of class MainSceneController.
     */
    @Test
    public void testFinalize() throws Exception, Throwable {
        System.out.println("finalize");
        MainSceneController instance = new MainSceneController();
        instance.finalize();
        // TODO review the generated test code and remove the default call to fail.
        assertNull(instance);
    }

    /**
     * Test of main method, of class MainSceneController.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        MainSceneController.main(args);
        // TODO review the generated test code and remove the default call to fail.

    }
    
}

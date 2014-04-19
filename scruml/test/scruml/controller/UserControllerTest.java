/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import scruml.model.UserModel;
import scruml.model.IARModel;

/**
 *
 * @author tom
 */
public class UserControllerTest extends TestWithFixture {
    SQLiteDatabaseController db;
    public UserControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseController db = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        db.connect(super.getDbTestFilenameTmp());
    }
    
    @After
    public void tearDown() throws Exception {
        SQLiteDatabaseController.getInstance().disconnect();
        super.tearDown();
    }

    /**
     * Test of createUser method, of class UserController.
     */
    @Test
    public void testCreateUser() throws Exception {
        System.out.println("createUser");
        UserController uc = new UserController();
        uc.createUser("Max Mustermann", 1);
        
        fail("The test case is a prototype.");
    }
    
}

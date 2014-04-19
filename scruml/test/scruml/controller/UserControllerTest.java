package scruml.controller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class provides unit tests of {@link UserController}
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 * @see UserController
 */
public class UserControllerTest extends TestWithFixture {
    
    public UserControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseController db = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        db.connect(super.getDbTestFilenameTmp());
    }
    
    @Override
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

package scruml.controller;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scruml.model.IARModel;
import scruml.model.UserModel;

/**
 * This class provides unit tests of {@link UserController}
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 * @see UserController
 */
public class UserControllerTest extends TestWithFixture {
    SQLiteDatabaseController db;
    UserModel um;
    UserController uc;
    
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
        db = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
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
        uc = new UserController();
        uc.createUser("Max Mustermann", 1);
        List<IARModel> result = uc.getAllUser();
        um = (UserModel) result.get(0);
        assertEquals(1,um.getId());
        assertEquals("Max Mustermann",um.getName());
        assertEquals(1, um.getRole());
        db.find(UserModel.class,"1");
 //       fail("The test case is a prototype.");
    }
    
//    public void testGetAllUser()
    
}

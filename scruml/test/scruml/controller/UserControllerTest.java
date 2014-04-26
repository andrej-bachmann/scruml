package scruml.controller;

import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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
    
    private SQLiteDatabaseController db;

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
        this.db = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        db.connect(super.getDbTestFilenameTmp());
    }
    
    @Override
    public void tearDown() throws Exception {
        db.disconnect();
        super.tearDown();
    }

    /**
     * Test of createUser method, of class UserController.
     */
    @Test
    public void testCreateUser() {
        System.out.println("createUser");
        
        UserController uc = new UserController();
        try {
            uc.createUser("Max Mustermann", 1);
            UserModel model = (UserModel)db.find(UserModel.class, "id=1");
            
            assertEquals(1, model.getId());
            assertEquals("Max Mustermann", model.getName());
            assertEquals(1, model.getRole());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of getAllUser method, of class UserController.
     */
    @Test
    public void testGetAllUser() {
        System.out.println("getAllUser");
        UserController uc = new UserController();
        try {
            uc.createUser("Max Mustermann", 1);
            uc.createUser("Marion Musterfrau", 2);
            
            List<IARModel> list = uc.getAllUser();
            UserModel model = (UserModel)list.get(0);
            UserModel model2 = (UserModel)list.get(1);
            
            assertEquals(1, model.getId());
            assertEquals("Max Mustermann", model.getName());
            assertEquals(1, model.getRole());
            assertEquals(2, model2.getId());
            assertEquals("Marion Musterfrau", model2.getName());
            assertEquals(2, model2.getRole());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
}

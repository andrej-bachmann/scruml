package scruml.controller;

import java.util.List;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import scruml.fixture.TestModel;
import scruml.model.IARModel;

/**
 * This class provides unit tests of {@link SQLiteDatabaseController}
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 * @see SQLiteDatabaseController
 */
public class SQLiteDatabaseControllerTest extends TestWithFixture {
    
    public SQLiteDatabaseControllerTest() {
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
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getInstance method, of class SQLiteDatabaseController.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        IDatabaseController result = SQLiteDatabaseController.getInstance();
        assertNotNull(result);
        assertThat(result, instanceOf(SQLiteDatabaseController.class));
    }

    /**
     * Test of connect method, of class SQLiteDatabaseController.
     */
    @Test
    public void testConnect_0args() {
        System.out.println("connect");
        IDatabaseController instance = SQLiteDatabaseController.getInstance();
        try {
            instance.connect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            try {
                instance.disconnect();
            }
            catch(Exception e) {}
        }
    }
    
    /**
     * Test of connect method, of class SQLiteDatabaseController.
     */
    @Test
    public void testConnect_String() {
        System.out.println("connect");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(super.getDbTestFilenameTmp());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
        finally {
            try {
                instance.disconnect();
            }
            catch(Exception e) {}
        }
    }

    /**
     * Test of disconnect method, of class SQLiteDatabaseController.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        IDatabaseController instance = SQLiteDatabaseController.getInstance();
        try {
            instance.connect();
            instance.disconnect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of find method, of class SQLiteDatabaseController.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(super.getDbTestFilenameTmp());
            
            TestModel result = (TestModel)instance.find(TestModel.class, "id=1");
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 1);
            assertEquals(result.getFirstname(), "Max");
            assertEquals(result.getLastname(), "Mustermann");
            
            instance.disconnect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of findAll method, of class SQLiteDatabaseController.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(super.getDbTestFilenameTmp());
            List<IARModel> modelList = instance.findAll(TestModel.class, null);
            assertEquals(modelList.size(), 2);
            TestModel result = (TestModel)modelList.get(0);
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 1);
            assertEquals(result.getFirstname(), "Max");
            assertEquals(result.getLastname(), "Mustermann");
            result = (TestModel)modelList.get(1);
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 2);
            assertEquals(result.getFirstname(), "Marion");
            assertEquals(result.getLastname(), "Musterfrau");
            
            //Test order by clause
            modelList = instance.findAll(TestModel.class, null, "id DESC");
            result = (TestModel)modelList.get(0);
            assertEquals(result.getId(), 2);
            
            instance.disconnect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of save method, of class SQLiteDatabaseController.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(super.getDbTestFilenameTmp());
            
            //Save record to database ...
            TestModel model = new TestModel();
            model.setFirstname("John");
            model.setLastname("Doe");
            instance.save(model);
            //... and check if it is there
            TestModel result = (TestModel)instance.find(TestModel.class, "id=3");
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 3);
            assertEquals(result.getFirstname(), "John");
            assertEquals(result.getLastname(), "Doe");
            //Update some fields ...
            result.setFirstname("Matti");
            result.setLastname("Meikäläinen");
            instance.save(result);
            //... and check if the update got saved
            result = (TestModel)instance.find(TestModel.class, "id=3");
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 3);
            assertEquals(result.getFirstname(), "Matti");
            assertEquals(result.getLastname(), "Meikäläinen");
            
            //New created model that gets double saved should only create one result
            model = new TestModel();
            model.setFirstname("David");
            model.setLastname("Goller");
            instance.save(model);
            instance.save(model);
            List<IARModel> modelList = instance.findAll(TestModel.class, "firstname='David' AND lastname='Goller'");
            assertEquals(1, modelList.size());
            
            instance.disconnect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of delete method, of class SQLiteDatabaseController.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(super.getDbTestFilenameTmp());
            
            TestModel result = (TestModel)instance.find(TestModel.class, "id=1");
            instance.delete(result);
            result = (TestModel)instance.find(TestModel.class, "id=1");
            assertNull(result);

            instance.disconnect();
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
}

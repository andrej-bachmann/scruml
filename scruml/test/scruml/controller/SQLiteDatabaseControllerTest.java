package scruml.controller;

import java.io.File;
import java.nio.file.Files;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scruml.fixture.TestModel;

/**
 * This class provides unit tests of {@link SQLiteDatabaseController}
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 * @see SQLiteDatabaseController
 */
public class SQLiteDatabaseControllerTest {
    
    private final String dbTestFilename = "test/scruml/database/db_test.sqlite";
    private final String dbTestFilenameTmp = "test/scruml/database/tmp_db_test.sqlite";
    
    public SQLiteDatabaseControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        File source = new File(this.dbTestFilename);
        File destination = new File(this.dbTestFilenameTmp);
        destination.delete();
        Files.copy(source.toPath(), destination.toPath());
    }
    
    @After
    public void tearDown() {
        File destination = new File(this.dbTestFilenameTmp);
        destination.delete();
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
            instance.connect(this.dbTestFilenameTmp);
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
            instance.connect(this.dbTestFilenameTmp);
            
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
     * Test of save method, of class SQLiteDatabaseController.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        SQLiteDatabaseController instance = (SQLiteDatabaseController) SQLiteDatabaseController.getInstance();
        try {
            instance.connect(this.dbTestFilenameTmp);
            
            //Save record to database ...
            TestModel model = new TestModel();
            model.setFirstname("John");
            model.setLastname("Doe");
            instance.save(model);
            //... and check if it is there
            TestModel result = (TestModel)instance.find(TestModel.class, "id=2");
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 2);
            assertEquals(result.getFirstname(), "John");
            assertEquals(result.getLastname(), "Doe");
            //Update some fields ...
            result.setFirstname("Matti");
            result.setLastname("Meikäläinen");
            instance.save(result);
            //... and check if the update got saved
            result = (TestModel)instance.find(TestModel.class, "id=2");
            assertNotNull(result);
            assertThat(result, instanceOf(TestModel.class));
            assertEquals(result.getId(), 2);
            assertEquals(result.getFirstname(), "Matti");
            assertEquals(result.getLastname(), "Meikäläinen");
            
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
            instance.connect(this.dbTestFilenameTmp);
            
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

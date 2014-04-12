package scruml.controller;

import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import scruml.model.IARModel;
import scruml.model.RequirementModel;

/**
 * This class provides unit tests of {@link RequirementController}
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 * @see RequirementController
 */
public class RequirementControllerTest extends TestWithFixture {
    
    public RequirementControllerTest() {
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
     * Test of getAllRequirements method, of class RequirementController.
     */
    @Test
    public void testGetAllRequirements() {
        System.out.println("getAllRequirements");
        RequirementController rc = new RequirementController();
        List<IARModel> result = rc.getAllRequirements();
        assertNotNull(result);
        assertEquals(2, result.size());
        
        RequirementModel m = (RequirementModel)result.get(0);
        assertEquals(1, m.getId());
        assertEquals("First requirement", m.getTitle());
        assertEquals("With first description", m.getDescription());
        assertEquals(1, m.getPriority());
        
        m = (RequirementModel)result.get(1);
        assertEquals(2, m.getId());
        assertEquals("Second requirement", m.getTitle());
        assertEquals("With second description", m.getDescription());
        assertEquals(2, m.getPriority());
    }
    
}

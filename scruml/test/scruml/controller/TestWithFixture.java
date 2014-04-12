package scruml.controller;

import java.io.File;
import java.nio.file.Files;
import org.junit.After;
import org.junit.Before;

/**
 * This class is the parent class for all test classes that want to use a database
 * file populated with fixed data.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class TestWithFixture {
    
    private final String dbTestFilename = "test/scruml/database/db_test.sqlite";
    private final String dbTestFilenameTmp = "test/scruml/database/tmp_db_test.sqlite";
    
    @Before
    public void setUp() throws Exception {
        File source = new File(this.dbTestFilename);
        File destination = new File(this.dbTestFilenameTmp);
        destination.delete();
        Files.copy(source.toPath(), destination.toPath());
    }
    
    @After
    public void tearDown() throws Exception {
        File destination = new File(this.dbTestFilenameTmp);
        destination.delete();
    }

    public String getDbTestFilenameTmp() {
        return dbTestFilenameTmp;
    }
    
}

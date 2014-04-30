package scruml.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import scruml.model.IARModel;

/**
 * SQLiteDatabaseController handles the connection to a SQLite database.
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class SQLiteDatabaseController implements IDatabaseController {
    
    private static IDatabaseController instance; 
    private Connection conn;
    private final String className = "org.sqlite.JDBC";
    private String dbFilename = "src/scruml/database/db.sqlite";
    
    private SQLiteDatabaseController() { }
    
    /**
     * This method is used to retrieve one instance from SQLiteDatabaseController
     * since this class is a Singleton.
     * @return Instance of SQLiteDatabaseController
     */
    public static synchronized IDatabaseController getInstance() {
        if(instance==null) {
            instance = new SQLiteDatabaseController();
        }
        return instance;
    }

    @Override
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName(this.className);
        this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.dbFilename);
    }
    
    /**
     * This method is used to connect to a specific SQLite database file.
     * @param dbFilename The relative path to the SQLite database file 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void connect(String dbFilename) throws ClassNotFoundException, SQLException {
        this.dbFilename = dbFilename;
        this.connect();
    }

    @Override
    public void disconnect() throws ClassNotFoundException, SQLException {
        this.conn.close();
        this.conn = null;
    }
    
    @Override
    public IARModel find(Class modelClass, String where) throws InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {
        
        try {
            List<IARModel> modelList = this.findAll(modelClass, where);
            return (modelList!=null && modelList.size()>0) ? modelList.get(0) : null;
        }
        catch(Exception e) {
            return null;
        }
    } 
    
    @Override
    public List<IARModel> findAll(Class modelClass, String where) throws InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {
        return this.findAll(modelClass, where, null);
    }
    
    @Override
    public List<IARModel> findAll(Class modelClass, String where, String order) throws InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {
       
        List<IARModel> modelList = new ArrayList<>();
        IARModel model = (IARModel)modelClass.newInstance();
        String queryString = "SELECT * FROM "+model.getTablename();
        if(where!=null && !where.equals("")) {
             queryString += " WHERE "+where;
        }
        if(order!=null && !order.equals("")) {
            queryString += " ORDER BY "+order;
        }
        try(Statement statement = this.conn.createStatement();
            ResultSet resultSet = statement.executeQuery(queryString)) {
            ResultSetMetaData resultSetMeta = resultSet.getMetaData();
            if(resultSet.isClosed())
                return modelList;
            
            while(resultSet.next()) {
                model = (IARModel)modelClass.newInstance();
                
                for(int i=1; i<=resultSetMeta.getColumnCount(); ++i) {
                    String columnName = resultSetMeta.getColumnName(i);
                    String columnValue = resultSet.getString(i);

                    try {
                        Field field = model.getClass().getDeclaredField(columnName);
                        field.setAccessible(true);
                        Object fieldValue = field.get(model);
                        Method setMethod = fieldValue.getClass().getDeclaredMethod("setDBValue", new Class[]{String.class});
                        setMethod.invoke(fieldValue, columnValue);
                    }
                    catch(NoSuchFieldException | NoSuchMethodException | InvocationTargetException e ) {
                        if(columnName.equals(model.getKey()))
                            throw new NoSuchFieldException("Key attribute is missing in model class.");
                    }

                }
                
                this.invokeAfterFind(model);
                modelList.add(model);
            }
            
        }
        return modelList;
    }
    
    @Override
    public void save(IARModel model) throws SQLException, NoSuchFieldException {

        if(this.getKeyValue(model)==null)
            this.insert(model);
        else {
            this.update(model);
        }
        
    }
    
    @Override
    public void delete(IARModel model) throws SQLException {
        
        try(Statement statement = this.conn.createStatement()) {
            String sqlString = "DELETE FROM "+model.getTablename()+" WHERE "+model.getKey()+"="+this.getKeyValue(model);
            statement.execute(sqlString);
        }
        
    }
    
    /**
     * This method gets triggered by the save method and handles an insert to
     * the database.
     * @param model Instance of an IARModel that should be inserted.
     * @throws SQLException
     * @throws NoSuchFieldException 
     */
    private void insert(IARModel model) throws SQLException, NoSuchFieldException {

        ArrayList[] fieldsAndValues = this.generateFieldsAndValues(model, false);
        ArrayList<String> fields = fieldsAndValues[0];
        ArrayList<String> values = fieldsAndValues[1];
        
        try(Statement statement = this.conn.createStatement()) {
            String sqlString = "INSERT INTO "+model.getTablename()+" ("+this.implode(", ", fields.toArray())+") VALUES ("+this.implode(", ", values.toArray())+")";
            statement.execute(sqlString);
        }
        try(Statement statement = this.conn.createStatement()) {
            String sqlString = "SELECT last_insert_rowid() FROM "+model.getTablename();
            try(ResultSet resultSet = statement.executeQuery(sqlString)) {
                if(!resultSet.isClosed()) {
                    String id = resultSet.getString(1);
                    
                    try {
                        Field field = model.getClass().getDeclaredField(model.getKey());
                        field.setAccessible(true);
                        Object fieldValue = field.get(model);
                        Method setMethod = fieldValue.getClass().getDeclaredMethod("setDBValue", new Class[]{String.class});
                        setMethod.invoke(fieldValue, id);
                    }
                    catch(NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
                        throw new NoSuchFieldException("Key attribute is missing in model class.");
                    }
                    
                }
            }
        }
        this.invokeAfterFind(model);
    }
    
    /**
     * This method gets triggered by the save method and handles an update to
     * the database.
     * @param model Instance of an IARModel that should be updated.
     * @throws SQLException
     * @throws NoSuchFieldException 
     */
    private void update(IARModel model) throws SQLException, NoSuchFieldException {
        
        ArrayList[] fieldsAndValues = this.generateFieldsAndValues(model, true);
        ArrayList<String> fields = fieldsAndValues[0];
        ArrayList<String> values = fieldsAndValues[1];
        
        ArrayList<String> updates = new ArrayList<>();
        for(int i=0; i<fields.size(); ++i)
            updates.add(fields.get(i)+"="+values.get(i));
        
        try(Statement statement = this.conn.createStatement()) {
            String sqlString = "UPDATE "+model.getTablename()+" SET "+this.implode(", ", updates.toArray())+" WHERE "+model.getKey()+"="+this.getKeyValue(model);
            statement.execute(sqlString);
        }
        
    }
    
    /**
     * This method is used to get the value of the key attribute of a model.
     * @param model Instance of IARModel
     * @return String containing the key value
     */
    private String getKeyValue(IARModel model) {
        try {
            Field field = model.getClass().getDeclaredField(model.getKey());
            field.setAccessible(true);
            Object fieldValue = field.get(model);
            Method getMethod = fieldValue.getClass().getDeclaredMethod("getDBValue", new Class[]{});
            return (String)getMethod.invoke(fieldValue);
        }
        catch(NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
    
    /**
     * This method is used to generate fields and values of an IARModel.
     * At first it retrieves all fields of the model table in the database and
     * adds them to the field list. Afterwards it searches if the fields are
     * set in the model and populates the value list. 
     * @param model Instance of IARModel of which the fields and values should 
     * be generated.
     * @param removeNullFields This param triggers if null values should be added
     * to the value list or not.
     * @return ArrayList which contains again two ArrayList: At index null the
     * fields list, at index one the values list.
     * @throws SQLException
     * @throws NoSuchFieldException 
     */
    private ArrayList[] generateFieldsAndValues(IARModel model, boolean removeNullFields) throws SQLException, NoSuchFieldException {
        
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        try(Statement statement = this.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM "+model.getTablename()+" LIMIT 0")) {
            ResultSetMetaData resultSetMeta = resultSet.getMetaData();
            
            for(int i=1; i<=resultSetMeta.getColumnCount(); ++i) {
                String columnName = resultSetMeta.getColumnName(i);
                
                try {
                    Field field = model.getClass().getDeclaredField(columnName);
                    field.setAccessible(true);
                    Object fieldValue = field.get(model);
                    Method getMethod = fieldValue.getClass().getDeclaredMethod("getDBValue", new Class[]{});
                    String value = (String)getMethod.invoke(fieldValue);
                    if(!removeNullFields) {
                        fields.add(columnName);
                        if(getMethod.invoke(fieldValue)!=null)
                            values.add("'"+value+"'");
                        else
                            values.add(value);
                    }
                    else {
                        if(getMethod.invoke(fieldValue)!=null) {
                            fields.add(columnName);
                            values.add("'"+value+"'");
                        }
                    }
                }
                catch(NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    if(columnName.equals(model.getKey()))
                        throw new NoSuchFieldException("Key attribute is missing in model class.");
                }
                
            }
        }
        ArrayList[] rtn = {fields, values};
        return rtn;
    }
    
    /**
     * This method triggers the void afterFind() method for all models that got
     * received from the database.
     * @param model Instance of IARModel
     */
    private void invokeAfterFind(IARModel model) {
        try {
            Method afterFindMethod = model.getClass().getDeclaredMethod("afterFind", new Class[]{});
            afterFindMethod.setAccessible(true);
            afterFindMethod.invoke(model, new Object[]{}); 
        }
        catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) { }
    }
    
    /**
     * Java implementation of the php function implode.
     * {@link http://www.php.net/manual/de/function.implode.php}
     * @param glue String that gets used for connecting the objects
     * @param array Contains all objects that should get glued.
     * @return String containing glued objects
     */
    private String implode(String glue, Object[] array) {
        if(array.length == 0)
            return "";
        
        StringBuilder sb = new StringBuilder();
        sb.append(array[0]);

        for(int i=1; i < array.length; ++i) {
            sb.append(glue);
            sb.append(array[i]);
        }

        return sb.toString();
    }

}

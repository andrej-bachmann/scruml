/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import scruml.model.IARModel;

/**
 *
 * @author Kevin
 */
public class SQLiteDatabaseController implements IDatabaseController {
    
    private static IDatabaseController instance; 
    private Connection conn;
    private String className = "org.sqlite.JDBC";
    private String dbFilename = "src/scruml/database/db.sqlite";
    
    private SQLiteDatabaseController() { }
    
    public static synchronized IDatabaseController getInstance() {
        if(instance==null) {
            instance = new SQLiteDatabaseController();
        }
        return instance;
    }

    @Override
    public void connect() throws Exception {
        Class.forName(this.className);
        this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.dbFilename);
    }
    
    public void connect(String dbFilename) throws Exception {
        this.dbFilename = dbFilename;
        this.connect();
    }

    @Override
    public void disconnect() throws Exception {
        this.conn.close();
        this.conn = null;
    }
    
    @Override
    public IARModel find(String modelName, String where) throws Exception {
        
        IARModel model = (IARModel)Class.forName(modelName).newInstance();
        
        Statement statement = this.conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+model.getTablename()+" WHERE "+where);
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        if(resultSet.isClosed())
            return null;
        
        for(int i=1; i<=resultSetMeta.getColumnCount(); ++i) {
            String columnName = resultSetMeta.getColumnName(i);
            String columnValue = resultSet.getString(i);
            
            try {
                Field field = model.getClass().getDeclaredField(columnName);
                field.set(model, columnValue);
            }
            catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {}
              
        }
        
        statement.close();
        return model;
    } 
    
    @Override
    public void save(IARModel model) throws Exception {

        if(this.getKeyValue(model)==null)
            this.insert(model);
        else {
            this.update(model);
        }
        
    }

    private void insert(IARModel model) throws Exception {

        ArrayList[] fieldsAndValues = this.generateFieldsAndValues(model, false);
        ArrayList<String> fields = fieldsAndValues[0];
        ArrayList<String> values = fieldsAndValues[1];
        
        Statement statement = this.conn.createStatement();
        String sqlString = "INSERT INTO "+model.getTablename()+" ("+this.implode(", ", fields.toArray())+") VALUES ("+this.implode(", ", values.toArray())+")";
        statement.execute(sqlString);
        statement.close();
    }
    
    private void update(IARModel model) throws Exception {
        
        ArrayList[] fieldsAndValues = this.generateFieldsAndValues(model, true);
        ArrayList<String> fields = fieldsAndValues[0];
        ArrayList<String> values = fieldsAndValues[1];
        
        ArrayList<String> updates = new ArrayList<>();
        for(int i=0; i<fields.size(); ++i)
            updates.add(fields.get(i)+"="+values.get(i));
        
        Statement statement = this.conn.createStatement();
        String sqlString = "UPDATE "+model.getTablename()+" SET "+this.implode(", ", updates.toArray())+" WHERE "+model.getKey()+"="+this.getKeyValue(model);
        statement.execute(sqlString);
        statement.close();
        
    }
    
    private String getKeyValue(IARModel model) {
        try {
            Field field = model.getClass().getDeclaredField(model.getKey());
            return (String)field.get(model);
        }
        catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }
    
    private ArrayList[] generateFieldsAndValues(IARModel model, boolean removeNullFields) throws Exception {
        
        Statement statement = this.conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+model.getTablename()+" LIMIT 0");
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        
        for(int i=1; i<=resultSetMeta.getColumnCount(); ++i) {
            String columnName = resultSetMeta.getColumnName(i);
            
            try {
                Field field = model.getClass().getDeclaredField(columnName);
                String value = (String)field.get(model);
                if(!removeNullFields) {
                    fields.add(columnName);
                    if(field.get(model)!=null)
                        values.add("'"+value+"'");
                    else
                        values.add(value);
                }
                else {
                    if(field.get(model)!=null) {
                        fields.add(columnName);
                        values.add("'"+value+"'");
                    }
                }
            }
            catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {};    
            
        }
        
        statement.close();
        ArrayList[] rtn = {fields, values};
        return rtn;
    }
    
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

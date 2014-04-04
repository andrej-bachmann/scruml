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
        
        return model;
    } 
    
    @Override
    public void save(IARModel model) throws Exception {

        Field field = model.getClass().getDeclaredField(model.getKey());
        String value = (String)field.get(model);
        if(value==null || value.equals(""))
            this.insert(model);
        else {
            //not implemented yet
        }
        
    }

    private void insert(IARModel model) throws Exception {

        Statement statement = this.conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+model.getTablename()+" LIMIT 1");
        ResultSetMetaData resultSetMeta = resultSet.getMetaData();
        
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        
        for(int i=1; i<=resultSetMeta.getColumnCount(); ++i) {
            String columnName = resultSetMeta.getColumnName(i);
            
            try {
                Field field = model.getClass().getDeclaredField(columnName);
                String value = (String)field.get(model);
                fields.add(columnName);
                if(field.get(model)!=null)
                    values.add("'"+value+"'");
                else
                    values.add(value);
            }
            catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {};    
            
        }
        
        String sqlString = "INSERT INTO "+model.getTablename()+" ("+this.implode(", ", fields.toArray())+") VALUES ("+this.implode(", ", values.toArray())+")";
        statement.execute(sqlString);
        
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

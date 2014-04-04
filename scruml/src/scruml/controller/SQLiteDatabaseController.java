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
    
}

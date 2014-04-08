/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.database;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Kevin
 */
public class DBSimpleStringProperty extends SimpleStringProperty {
    
    public void setDBValue(String str) {
        this.set(str);
    }
    
    public String getDBValue() {
        return this.get();
    }
    
}

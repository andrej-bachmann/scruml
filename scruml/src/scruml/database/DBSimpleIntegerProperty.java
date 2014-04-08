/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.database;

import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Kevin
 */
public class DBSimpleIntegerProperty extends SimpleIntegerProperty {
    
    public void setDBValue(String str) {
        this.set(Integer.parseInt(str));
    }
    
    public String getDBValue() {
        int num = this.get();
        Integer s = num;
        return (num!=0) ? s.toString() : null;
    }
    
}

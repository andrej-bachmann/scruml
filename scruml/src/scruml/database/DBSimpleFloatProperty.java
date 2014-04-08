/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.database;

import javafx.beans.property.SimpleFloatProperty;

/**
 *
 * @author Kevin
 */
public class DBSimpleFloatProperty extends SimpleFloatProperty {
    
    public void setDBValue(String str) {
        this.set(Float.parseFloat(str));
    }
    
    public String getDBValue() {
        float num = this.get();
        Float f = num;
        return (num!=0.0f) ? f.toString() : null;
    }
    
}

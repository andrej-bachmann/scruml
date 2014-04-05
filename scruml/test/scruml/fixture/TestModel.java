/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.fixture;

import scruml.model.IARModel;

/**
 *
 * @author Kevin
 */
public class TestModel implements IARModel {

    public String id;
    public String firstname;
    public String lastname;
    
    @Override
    public String getTablename() {
        return "test";
    }

    @Override
    public String getKey() {
        return "id";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;

import scruml.model.IARModel;

/**
 *
 * @author Kevin
 */
public interface IDatabaseController {
    
    public void connect() throws Exception;
    public void disconnect() throws Exception;
    
    public IARModel find(String modelName, String where) throws Exception;
    public void save(IARModel model) throws Exception;
            
}

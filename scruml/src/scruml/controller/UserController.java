/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;

import scruml.model.UserModel;


/**
 *
 * @author tom
 */
public class UserController {
    
    private final IDatabaseController db;
    
    public UserController(){
        this.db = SQLiteDatabaseController.getInstance();
    }
    
/**
     * This method creates a new user and saves it in the database.
     * @param name Name of the user
     * @param role role of the user
     * 1 = ProductOwner
     * 2 = ScrumMaster
     * 3 = Developer
     * @return UserModel
     * @throws Exception 
     */
    
    public UserModel createUser (String name, int role) throws Exception{
        UserModel user = new UserModel();
        user.setName(name);
        user.setRole(role);
        this.db.save(user);
        return user;
    }
    
//    public boolean checkForScrumMaster(){
//        
//    }
}

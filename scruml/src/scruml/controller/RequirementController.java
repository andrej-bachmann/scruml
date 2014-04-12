/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.controller;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;
import scruml.model.IARModel;
import scruml.view.RequirementViewController;
import scruml.model.RequirementModel;
/**
 * @author Simon Deubzer, Kevin Dietrich, Manuel Fachtan, David Goller, Thomas Kausler
 */
public class RequirementController{
    
    private RequirementModel reqMod;
    IDatabaseController dbController = SQLiteDatabaseController.getInstance();
    private final IntegerProperty id = new DBSimpleIntegerProperty();
    private final StringProperty title = new DBSimpleStringProperty();
    private final StringProperty description = new DBSimpleStringProperty();
    private final IntegerProperty priority = new DBSimpleIntegerProperty();
    
    public void createRequirement(String title, String description)throws Exception{
        reqMod = new RequirementModel();
        reqMod.setTitle(this.title.toString());
        reqMod.setDescription(this.description.toString());
        dbController.save(reqMod);
    }
    
 //   public void changeRequirementPriority(){
 //      reqMod.setPriority(priority);
 //   }
    
     public List<IARModel> getAllRequirements() {
        try {
            return SQLiteDatabaseController.getInstance().findAll(RequirementModel.class, null);
        }
        catch(Exception e) {
            return null;
        }
    }
}

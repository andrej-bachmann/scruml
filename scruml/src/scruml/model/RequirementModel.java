/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scruml.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import scruml.database.DBSimpleIntegerProperty;
import scruml.database.DBSimpleStringProperty;

/**
 *
 * @author scruml
 */
public class RequirementModel implements IARModel{
    
    private final IntegerProperty id = new DBSimpleIntegerProperty();
    private final StringProperty title = new DBSimpleStringProperty();
    private final StringProperty description = new DBSimpleStringProperty();
    private final StringProperty priority = new DBSimpleStringProperty();
    private final StringProperty effort = new DBSimpleStringProperty();

    @Override
    public String getTablename(){
        return "requirement";
    }

    @Override
    public String getKey() {
        return "id";
    }
    
    
    public int getId() {
        return this.id.get();
    }
    public void setId(int id){
        this.id.set(id);
    }
    
    
    public String getTitle() {
        return this.title.get();
    }
    public void setTitle(String title){
        this.title.set(title);
    }

    
    public String getDescription() {
        return this.description.get();
    }
    public void setDescription(String description){
        this.description.set(description);
    }
    
    public String getPriority(){
        return this.priority.get();
    }
    public void setPriority(String priority){
        this.priority.set(priority);
    }
    
    public String getEffort(){
        return this.effort.get();
    }
    public void setEffort(String effort){
        this.effort.set(effort);
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty priorityProperty() {
        return priority;
    }
    
    public StringProperty effortProperty() {
        return effort;
    }
    
    
}

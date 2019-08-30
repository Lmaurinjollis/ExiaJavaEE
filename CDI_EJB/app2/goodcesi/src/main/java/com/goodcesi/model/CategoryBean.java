/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;


import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.Category;
import com.goodcesi.qualifier.CategoryAdded;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asbriglio
 * bean intervenant dans la création d'une catégorie
 */
@Named("categoryModel")
@RequestScoped
public class CategoryBean {
    
    @Inject
    private CatalogManagerLocal catalogManager;
    
    @Inject @CategoryAdded
    private Event<Category> catEvent;
    
    private String title;
    private Category category;
    
    public void createNewCategory(ActionEvent ae){
        category = catalogManager.saveNewCategory(title);
        //si la catégorie a été sauvegardée
        if(category!=null) {
           catEvent.fire(category);//on déclenche l'évènement
        }
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
    
}

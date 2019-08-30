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
 * @author asbriglio bean intervenant dans la création d'une catégorie
 */
@Named("categoryModel")
@RequestScoped
public class CategoryBean {

    @Inject
    private CatalogManagerLocal catalogManager;

    private String title;
    private Category category;

    /*
    * CategoryAdded is a qualifier made during the step 3.4 of the workshop
    * (see com.goodcesi.qualifier -> CategoryAdded)
     */
    @Inject
    @CategoryAdded
    private Event<Category> catEvent;

    public void createNewCategory(ActionEvent ae) {
        category = catalogManager.saveNewCategory(title);

        //If the category has been saved
        if (category != null) {
            catEvent.fire(category);//event will be triggered
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

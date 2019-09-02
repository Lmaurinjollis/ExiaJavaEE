/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.Category;
import com.goodcesi.integration.dao.CrudServiceLocal;
import com.goodcesi.qualifier.CategoryAdded;
import com.goodcesi.qualifier.ScopeMonitor;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asbriglio Cache applicatif chargeant le menu des catégories. les
 * catégories varient peu au fil du temps, on peut donc les cacher
 *
 * Ce sesion bean ne doit jamais être invoquée dans un contexte transactionnel
 */
@Named
@ApplicationScoped
@ScopeMonitor
@Singleton
@Lock(LockType.WRITE)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CategoriesCache {

    private List<Category> categories;

    @Inject
    CatalogManagerLocal catalogManager;

    public CategoriesCache() {
    }

    @PostConstruct
    void init() {
        //chargement des catégories existantes au démarrage de l'appli
        categories = catalogManager.getAllCategories();
    }

    @Lock(LockType.READ)
    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(@Observes @CategoryAdded Category cat) {
        categories.add(cat);
    }

    @Schedule(hour = "*", minute = "*", second = "*/15")
    public void refreshCategories() {
        System.out.println("timer");
        categories = catalogManager.getAllCategories();
    }
}

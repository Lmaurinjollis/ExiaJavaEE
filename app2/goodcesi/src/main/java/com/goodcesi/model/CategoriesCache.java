/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.Category;
import com.goodcesi.qualifier.CategoryAdded;
import com.goodcesi.qualifier.ScopeMonitor;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author asbriglio
 * Cache applicatif chargeant le menu des catégories.
 * les catégories varient peu au fil du temps, on peut donc les cacher
 * 
 * Ce sesion bean ne doit jamais être invoquée dans un contexte transactionnel
 */
@ScopeMonitor
@Named
@ApplicationScoped
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.WRITE)//mode de verrouillage par défaut
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CategoriesCache {
    
    private  List<Category> categories;
    
    @Inject CatalogManagerLocal catalogManager;

    public CategoriesCache() {}
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)       
    void init(){
        //chargement des catégories existantes au démarrage de l'appli
        categories = catalogManager.getAllCategories();
    }
    
    //redéfinition du mode de verrouillage par défaut.
    //la méthode peut être invoquée de façon concurrente
    @Lock(LockType.READ)
    public List<Category> getCategories() {
        return categories;
    }
    
    /*
    les méthodes ci-dessous sont verrouillées en écriture (verrou exclusif). 
    Lors de l'exécution d'une de ces méthodes, aucune autre méthode du singleton ne peut s'exécuter en parallèle.
    */
    
    //méthode publique pour qu'elle soit considérée comme une méthode métier au sens EJB
    //seule une méthode métier d'un ejb Singleton supporte le verrouillage.
    //deplus un méthode observatrice CDI définie dans un session bean doit être une méthode métier ou une méthode statique
    public void addCategory(@Observes @CategoryAdded Category cat){//accepte tout modificateur d'accès 
        categories.add(cat);        
    }
    
    //méthode déclenchée par le service d'horloge toutes les 30 secondes
    //permet de rafraichir la liste des catégories pour le cas où des cats ont été supprimées / ajoutées /modifiées depuis la base.
   // @Schedule(hour = "*",minute = "*",second="*/15")
    public void refreshCategories(){
        System.out.println("timer");
        categories = catalogManager.getAllCategories();
    }
        
}

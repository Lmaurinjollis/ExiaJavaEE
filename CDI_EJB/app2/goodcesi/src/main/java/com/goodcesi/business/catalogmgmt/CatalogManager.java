/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.catalogmgmt;

import com.goodcesi.business.domain.*;
import java.util.*;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import com.goodcesi.integration.dao.CrudServiceLocal;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.interceptor.Interceptors;

/**
 *
 * @author asbriglio
 * Session bean chargé de gérer le catalogue des articles.
 */
@Stateless
//@Interceptors(com.goodcesi.monitor.BeanScopeTracker.class)//technique pour associer un (des) intercepteur(s) à un EJB
public class CatalogManager implements CatalogManagerLocal {

    //@Inject ne permet d'injecter que des composants locaux.c-à-d colocalisés dans la même instance de JVM (donc dans la même archive)
    //on peut aussi utiliser @javax.ejb.EJB pour injecter un session bean
    @EJB
    private CrudServiceLocal dao;
    
    @Inject
    private ItemsSeekerLocal itemsSeeker;
    
    //Création d'une nouvelle catégorie
    @Override
    @RolesAllowed("ADMIN")//méthode accessible qu'aux administrateurs
    public Category saveNewCategory(String title){
        if(title==null||title.trim().isEmpty()){ //le deuxième test vérifie que title ne contient pas que des espaces
            return null;
        } 
        else {
            Category newCat = new Category(title);
            newCat = dao.create(newCat);
            return newCat;
        }    
    }
    
   /**
    * Création d'un nouvel article
    * 
    * @param item
    * @param owner
    * @param categories
    * @return entité sauvegardée
    */
    @Override
    public Item saveNewItem(Item item,User owner, Set<Long> categories) {
        if(item==null||item.getName()==null ||item.getDescription()==null||item.getPrice()==null) return null;
        else{  
          Item managedItem = dao.create(item);
          owner = dao.update(owner); //l'utilisateur est rattaché au contexte de persistance
          
          managedItem.setSeller(owner); //Item est l'entité proprio de la relation
          owner.add(managedItem);//facultatif - mais pour respect de la bidirectionnalité de l'asso
          
          if (!categories.isEmpty()){//si la collection contient des id de catégories
            for(Long id : categories){
                Category managedCat = dao.find(Category.class, id);
                managedItem.add(managedCat);//Item est l'entité proprio de la relation
            }
          }
          return managedItem;//embarque son id généré ainsi que le champ version initialisé
        }
    }
    
    //Récupération des articles non vendus d'une catégorie
    @Override 
    public List<Item> getItemsByCatId(Long catId){
        Category cat = dao.find(Category.class, catId);// 1 requête 
        if(cat!=null){
           List<Item> retrievedItems = itemsSeeker.retrieveUnsoldItemsByCategory(cat);//là encore on génère une seconde requête
           return retrievedItems;
         } else
            return null; // pourrait être remplacé par une levée d'exception
    }
    
    //Récupération de toutes les catégories
    @Override
    public List<Category> getAllCategories() {
        return dao.findAll(Category.class);
    }
    
    //Recherche d'un article en fonction de son id (clé primaire)
    @Override
    public Item getItemById(Long id) {
        //il faudrait gérer le cas d'un item non retrouvé.
        return dao.find(Item.class, id);//null retourné si aucun article ne correspond à l'id.
    }
    
    @Override
    //récupération des articles d'un vendeur agrégés dans une map selon qu'ils sont vendus ou à vendre.
    public Map<String,List<Item>> getItemsFromSeller(Long sellerId){
        User seller = dao.find(User.class, sellerId);
        Map<String,List<Item>> items = new HashMap<>();
        List<Item> soldItems = itemsSeeker.retrieveItemsFromUserByStatus(seller, ItemStatus.BUYED);
        items.put(ItemStatus.BUYED.toString(),soldItems);
        List<Item> itemsForSale = itemsSeeker.retrieveItemsFromUserByStatus(seller, ItemStatus.FOR_SALE);
        items.put(ItemStatus.FOR_SALE.toString(), itemsForSale);
        return items;
    }
    
    @Override
    //modification d'articles
    public void updateItems(List<Item> items){
        for(Item i : items){
            dao.update(i);//on pourrait utiliser un batch update
        }
    }
    
}

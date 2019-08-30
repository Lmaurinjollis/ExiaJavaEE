/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.catalogmgmt;

import com.goodcesi.business.domain.*;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.*;

/**
 *
 * @author asbriglio
 * Chargé de retrouver une liste d'articles en fonction de critères.
 */
@Stateless
public class ItemsSeeker implements ItemsSeekerLocal, SessionBean {
    
    @PersistenceContext
    private EntityManager em;

    /*
     recherche des articles non vendus pour une catégorie donnée.
    */
    @Override
    public List<Item> retrieveUnsoldItemsByCategory(Category category){
        String jpql="SELECT i FROM Item i JOIN i.categories c WHERE c=:cat AND i.status = com.goodcesi.business.domain.ItemStatus.FOR_SALE";
        TypedQuery<Item> query = em.createQuery(jpql, Item.class).setParameter("cat", category);
        return query.getResultList();
        
    }
    /*
    permet de retrouver la liste des articles ayant un statut donné appartenant à un vendeur
    */
    @Override
    public List<Item> retrieveItemsFromUserByStatus(User user, ItemStatus status){
        String jpql="SELECT i FROM User u JOIN u.items i WHERE u=:user AND i.status =:status";
        TypedQuery<Item> query = em.createQuery(jpql, Item.class).setParameter("user", user).setParameter("status", status);
        return query.getResultList();
    }
    
    public void ejbActivate() {
    }
    
    public void ejbPassivate() {
    }
    
    public void ejbRemove() {
    }
    
    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
    }
    
    public void ejbCreate() {
    }
}

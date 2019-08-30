/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.goodcesi.integration.dao;


import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cesi
 * Session bean chargé des opérations de persistance, modification, supression et recherche d'une entité (des données)
 * Par défaut, les méthodes de ce session bean nécessitent qu'une transaction 
 * soit démarrée lorsqu'elles sont invoquées
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CrudService implements CrudServiceLocal{
    
    @PersistenceContext(unitName = "goodcesiPU")//unitName facultatif
    private EntityManager em;
    
    //sauvegarde d'un objet
    @Override
    public <T> T create(T t) {
        em.persist(t);
        return t;
    }
    /*
    Recherche d'un objet sauvegardé en base
    s'exécute dans le contexte transactionnel de l'appelant si celui-ci est transactionnel
    */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public <T> T find(Class<T> type, Long id) {
        return em.find(type, id);
    }
    
    //modification de l'état d'un objet en base.
    @Override
    public <T> T update(T t) {
        t = em.merge(t);
        return t;
    }
    //suppression en base de l'enregistrement (ou les enregistrements) correspondant à un objet
    @Override
    public void delete(Object t) {
        t = em.merge(t);
        em.remove(t);
    }
    /*
    Récupération de l'ensemble  des objets qui ont été persistés en base pour un type donné.
    
    La méthode ne s'exécutera jamais au sein d'une transaction. Si le client invoque cette
    méthode au sein d'une transaction, la transaction du client sera interrompue
    le temps de l'exécution de cette méthode. La transaction du client se poursuivra 
    une fois l'exécution de la méthode terminée.
    
    */
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public <T> List<T> findAll(Class<T> type){
        //utilisation de la généricité quasi impossible avec JPQL
        //on comprend l'un des avantages d'une API "typée" (criteria) pour exécuter des requêtes JPA
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> identificationVariable = cq.from(type);
        cq.select(identificationVariable);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }
    
}

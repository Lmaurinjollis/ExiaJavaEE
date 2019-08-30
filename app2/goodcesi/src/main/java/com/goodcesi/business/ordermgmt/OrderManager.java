/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.ordermgmt;

import com.goodcesi.business.domain.Item;
import com.goodcesi.business.domain.Order;
import com.goodcesi.business.domain.OrderStatus;
import com.goodcesi.business.domain.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.goodcesi.integration.dao.CrudServiceLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author asbriglio
 * 
 * Session bean implémentant la gestion d'une commande
 * 
 * Une nouvelle transaction est démarrée à chaque invocation du session bean.
 * Une nouvelle transaction démarque donc chaque exécution d'une méthode.
 * 
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class OrderManager implements OrderManagerLocal {

    @PersistenceContext
    private EntityManager em;
      
    @Inject
    private CrudServiceLocal dao;
    
//    @Resource//injection d'un service d'horloge
//    private TimerService timerService;
    
    //création d'une commande entrainant la mise à jour de la base de données.
    @Override
    @RolesAllowed("BUYER") //seul un utilisateur associé au rôle logique BUYER est autorisé à invoquer cette méthode
    @Asynchronous//la méthode s'exécute dans un thread séparé.
    public void createOrder(Order order){
      dao.create(order);
      Item i = order.getOrderedItem();//i n'est pas managé car pas de cascade sur la relation.
      dao.update(i);//mise à jour de l'item (pour que le statut soit modifié)-l'item est désormais managé - la relation avec l'acheteur sera persistée.
      
      //ne pas utiliser l'API java.lang.Thread dans un environnement Java EE. Ici c'est pour simuler un traitement long et visualiser l'exécution asynchrone 
      try {
          System.out.println("démarrage d'un traitement long de paiement");
            Thread.sleep(10000L);
            System.out.println("fin du traitement de paiement");
        } catch (InterruptedException ex) {
            Logger.getLogger(OrderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    
      //le service d'horloge sera invoqué une première fois 30 sec après sa création puis toutes les 30 sec.
     // timerService.createTimer(30000L, 30000L, order.getOrderedItem().getId());
    }

    
    /*
    récupération de la liste des commandes en fonction du contexte dans lequel la méthode
    est invoquée. invocation depuis un espace "vendeur" ou "utilisateur"
    
    on redéfine le comportement transactionnel défini pour le bean
    Cette méthode ne s'exécutera qu'au sein d'une transaction déjà existante.
    */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @RolesAllowed({"BUYER","SELLER"})//seuls les vendeurs et acheteurs peuvent invoquer cette méthode
    public List<Order> retrieveOrders(User user,CallerContext context) {
      //l'entité passée n'a pas besoin d'être managé car en coulisse la requête SQL généré se basera sur la clé primaire 
      String jpql =null;
      switch(context){
          case AS_SELLER :
              jpql = "SELECT o From Order o JOIN o.orderedItem i WHERE i.seller=:user";
              break;
          case AS_BUYER :
              jpql = "SELECT o From User u JOIN u.orders o WHERE u=:user";//à tester
              break;    
      }
      
      List<Order> orders = em.createQuery(jpql, Order.class).setParameter("user", user).getResultList();
      return orders;
    }
    
    //passer la commande en état "envoyé"
    @Override
    @RolesAllowed("SELLER") //seul un utilisateur associé au rôle logique SELLER est autorisé à invoquer cette méthode
    public Order putOrderIntoSentState(Long orderId){
        Order order = dao.find(Order.class, orderId); //order est dans un contexte de persistance associé à une transaction et est donc managé
        order.setStatus(OrderStatus.SENT);//changement sauvegardé en base à la fin de la transaction.
        order.setShippingDate(new Date()); //on assigne la date d'envoi
        return order;
    }
    
    //méthode de rappels temporels invoquée quand un intervalle de temps est écoulé
    //méthode chargée de rappeler à un vendeur à intervalles de temps réguliers qu'une commande n'a pas été envoyée.
    @Timeout
    void checkOrderStatus(Timer timer){
        Long id = (Long) timer.getInfo();//on récupère l'id stocké comme info dans le timer
        Order retrievedOrder = dao.find(Order.class, id);//récupération de la commande.
        if(retrievedOrder.getStatus()==OrderStatus.SENT){//si la commande a été envoyée
            timer.cancel();//on annule les rappels temporels
        }else{//sinon le système envoie un "mail" au vendeur.
            //ici on se contente d'un affichage console
            System.out.println("Bonjour "+retrievedOrder.getOrderedItem().getSeller().getFirstname()+ 
                    " n'oubliez pas d'envoyer la commande "+retrievedOrder.getOrderedItem().getName());
        }
    }
}

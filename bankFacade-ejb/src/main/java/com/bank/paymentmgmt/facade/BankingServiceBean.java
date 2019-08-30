/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import com.bank.paymentmgmt.domain.Payment;
import com.bank.paymentmgmt.integration.PaymentDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.jws.WebService;

/**
 *
 * @author ludwig
 */
@Stateless
@LocalBean
@WebService(endpointInterface = "com.bank.paymentmgmt.facade.BankingServiceEndpointInterface", portName = "BankingPort",
        serviceName = "BankingService")
public class BankingServiceBean implements BankingServiceEndpointInterface, BankingServiceRemote {

    @Inject
    private PaymentDAO paymentDAO;

    @Override
    public Boolean createPayment(String ccNumber, Double amount) {
        if (ccNumber.length() == 10) {
            System.out.println("Montant payé : " + amount + " €");
            Payment p = new Payment();
            p.setCcNumber(ccNumber);
            p.setAmount(amount);
            p.setAmount(amount);
            //pour l’instant le retour n’est pas utilisé
            p = paymentDAO.add(p);
            //juste pour tester
            paymentDAO.getAllStoredPayments();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Payment> lookupAllStoredPayments() {
        return paymentDAO.getAllStoredPayments();
    }

    @Override
    public Payment lookupStoredPayment(Long id) {
        return paymentDAO.find(id);
    }

    @Override
    public Payment deleteStoredPayment(Long id) {
        return paymentDAO.delete(id);
    }

}

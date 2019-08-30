/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.business.logic;

import com.store.integration.BankingEndpoint;
import com.store.integration.BankingService;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author ludwig
 */
@Stateless
@Soap
public class SoapPaymentValidator implements PaymentValidator {

    @WebServiceRef(BankingService.class)
    private BankingEndpoint banking;

    @Override
    public Boolean process(String ccNumber, Double amount) {
        Boolean isValid= banking.paymentOperation(ccNumber, amount);
        return isValid;
    }

    //Ici la methode n'a pas besoin de fermer le client
    @Override
    public void connectionClose() {
        return;
    }
}

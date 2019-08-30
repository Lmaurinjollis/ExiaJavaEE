/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.model;

import com.store.business.logic.PaymentValidator;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author ludwig
 */
@Named(value = "paymentModel")
@RequestScoped
public class PaymentBean {

    private String ccNumber;

    private Double amount;

    @Inject
    private PaymentValidator paymentValidator;
    /**
     * Creates a new instance of PaymentBean
     */
    public PaymentBean() {
    }
    
    public String doPaymentWithSoap() {
        System.out.println("Le paiement commence");
        boolean isValid = paymentValidator.process(ccNumber, amount);

        if (isValid == true) {
            return "valid";
        } else {
            return "invalid";
        }
    }

    //get and set for CcNumber and Amount
    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}

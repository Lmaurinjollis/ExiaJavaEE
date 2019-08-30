/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author ludwig
 */
@WebService(name = "BankingEndpoint")
public interface BankingServiceEndpointInterface {
    @WebMethod(operationName = "paymentOperation")
    @WebResult(name = "acceptedPayment")
    Boolean createPayment(@WebParam(name="cardNumber") String ccNumber, @WebParam(name="amountPaid") Double amount);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.domain;

import javax.xml.bind.annotation.XmlEnum;
/**
 *
 * @author ludwig
 */
@XmlEnum
public enum PaymentStatus {
    VALIDATED, CANCELLED
}


package com.store.integration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour paymentOperation complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="paymentOperation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amountPaid" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paymentOperation", propOrder = {
    "cardNumber",
    "amountPaid"
})
public class PaymentOperation {

    protected String cardNumber;
    protected Double amountPaid;

    /**
     * Obtient la valeur de la propriété cardNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Définit la valeur de la propriété cardNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    /**
     * Obtient la valeur de la propriété amountPaid.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAmountPaid() {
        return amountPaid;
    }

    /**
     * Définit la valeur de la propriété amountPaid.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAmountPaid(Double value) {
        this.amountPaid = value;
    }

}

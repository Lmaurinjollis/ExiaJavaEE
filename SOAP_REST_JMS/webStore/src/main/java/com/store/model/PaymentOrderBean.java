/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.store.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author ludwig
 */
@Named(value = "paymentOrderModel")
@RequestScoped
public class PaymentOrderBean {

    private final String baseURL = "http://localhost:11080/bankFacade-war/banking/payment";
    private List<PaymentOrder> paymentOrders = new ArrayList<>();

    @Inject
    private Client client;

    @PostConstruct
    void init() {
        loadAllPayments();
    }

    public List<PaymentOrder> getPaymentOrders() {
        return paymentOrders;
    }

    private void loadAllPayments() {
        paymentOrders.clear();
        WebTarget target = client.target(baseURL).path("payments");
        Response resp = target.request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        String jsonContent = resp.readEntity(String.class);
        resp.close();
        try (JsonReader jreader = Json.createReader(new StringReader(jsonContent));) {
            JsonArray jArray = jreader.readArray();
            for (int i = 0; i < jArray.size(); i++) {
                JsonObject jObject = jArray.getJsonObject(i);
                Long id = jObject.getJsonNumber("id").longValue();
                Double amount = jObject.getJsonNumber("amount").doubleValue();
                String location = baseURL + "/" + id;
                paymentOrders.add(new PaymentOrder(location, amount, id));
            }
        }
    }

    public String cancelPayment(Long id) {
        WebTarget target = client.target(baseURL).path("{id}").resolveTemplate("id", id);
        target.request().delete();
        loadAllPayments();
        return null;
    }
}

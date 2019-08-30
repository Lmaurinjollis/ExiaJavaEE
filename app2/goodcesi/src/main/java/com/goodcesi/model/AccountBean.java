/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.User;

import com.goodcesi.business.usermgmt.UserManagerLocal;
import com.goodcesi.qualifier.ScopeMonitor;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Asbriglio
 * bean chargé de la création d'un compte utilisateur
 */
@Named("accountModel")
@ScopeMonitor
@SessionScoped
public class AccountBean implements AccountModel, Serializable{
    
    @Inject
    private UserManagerLocal userManager;
    
    private String login, pwd, fname, lname, email, address;
    private List<Long> selectedGroupIds;
   
   
    public String createCredential(){
       return "credInfos";
    }
       
    @Override
    public String createUser(){
       //vérification du login
       boolean existingLogin = userManager.checkLogin(login);
       if(existingLogin){//si le login existe déjà
           String msgSummary = "identifiant existant!";
           String msgDetail = "L'identifiant "+login+" est déjà pris. Choisissez-en un autre";
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,msgSummary, msgDetail));//on créé un message d'avertissement
           return "credInfos";//navigation vers la page de saisie des crédentiels.
       }
        
        //on devrait gérer le non renseignement des champs
        User u = new User(fname, lname);
        u.setEmail(email);
        u.setAddress(address);
        u.setLogin(login);
        u.setPassword(pwd);
        
        userManager.create(u,selectedGroupIds);//ici le retour n'a pas d'importance
        
        //On clôture la session en cours
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession currentSession = (HttpSession)ec.getSession(false);
        currentSession.invalidate();
        
        return "index";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPwd() {
        return pwd;
    }

    @Override
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Long> getSelectedGroupIds() {
        return selectedGroupIds;
    }

    public void setSelectedGroupIds(List<Long> selectedGroupIds) {
        this.selectedGroupIds = selectedGroupIds;
    }
    
    
    
}

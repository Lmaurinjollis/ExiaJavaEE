/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model.decorator;

import com.goodcesi.model.AccountBean;
import com.goodcesi.model.AccountModel;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

/**
 *
 * @author Asbriglio
 * un décorateur peut être abstrait pour éviter d'implémenter toutes les méthodes définies dans l'interface
 * 
 */
@Decorator @Priority(Interceptor.Priority.APPLICATION +10)
public abstract class PasswordEncoder implements AccountModel,Serializable{//AccountBean étant "passivation capable", le décorateur doit l'être aussi

    @Inject @Delegate private AccountModel accountBean;//on aurait pu utiliser le type AccountBean
    
    @Override
    public String createUser() {
        String pwd = accountBean.getPwd();//récupération du mot de passe non chiffré
        pwd=encrypt(pwd);//on chiffre le mot de passe
        accountBean.setPwd(pwd);//on assigne le mot de passe chiffré
        return accountBean.createUser();//invocation de la méthode décorée du bean décoré
    }
    
    private String encrypt(String password){
        try {
            //utilisation de la bibliothèque de sécurité Java SE  (Java Cryptography Architecture)
            
            MessageDigest digest = MessageDigest.getInstance("sha-256");//obtention d'un message digest pour le chiffrement en sha-256
            byte[] hash = digest.digest(password.getBytes("UTF-8"));//conversion du mot de passe en tableau d'octet en utilisant UTF-8 puis hachage
            StringBuilder encodedPwd = new StringBuilder();//mutable
            
            for(int i=0; i<hash.length;i++){//pour chaque octet
                //retourne la chaine représentant l'octet sous forme d'entier non signé en hexa( base 16)
                String hexValue = Integer.toHexString(0xff & hash[i]);
                if(hexValue.length() == 1) encodedPwd.append('0');
                encodedPwd.append(hexValue);  
            }
            
            return encodedPwd.toString();
                
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException("impossible de chiffrer le mot de passe");
        }
    }
    
}

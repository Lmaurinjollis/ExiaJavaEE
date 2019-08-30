/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.monitor;

import com.goodcesi.qualifier.MethodInvocationMonitor;
import java.io.Serializable;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author Asbriglio
 * intercepteur des méthodes métiers des bean CDI
 * implémente Serializable pour pouvoir être associé à un bean "sérializable" (bean de scope de type conversion ou session)
 */
@Interceptor 
@MethodInvocationMonitor
@Priority(Interceptor.Priority.APPLICATION+1)
public class BeanInvocationTracker implements Serializable{
    
    @AroundInvoke
    public Object traceMethodCall(InvocationContext ctx)throws Exception{
        String businessMethodName = ctx.getMethod().getName();
        String targetBeanName = ctx.getTarget().getClass().getSimpleName();
        System.out.println("****méthode "+targetBeanName+"#"+businessMethodName+ " invoquée****");
        return ctx.proceed(); //la méthode métier va être invoquée
    }    
}

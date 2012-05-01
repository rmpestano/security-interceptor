/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.cdiinterceptor.observer;

import com.rmpestano.cdiinterceptor.model.Perfil;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;

/**
 *
 * @author rmpestano
 */
@RequestScoped
public class PerfiChangelObserver {
    
    public void onPerfilChange(@Observes PerfilChange perfilChangeEvent){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Perfil.PERFIL_USUARIO,perfilChangeEvent.getPerfis());
    }
            
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.producer;

import com.jsf.conventions.qualifier.UsuarioLogado;
import com.rmpestano.cdiinterceptor.model.Perfil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 *
 * @author rmpestano
 */
public class PerfilProducer implements Serializable{
    
    @Produces @UsuarioLogado @RequestScoped 
    public List<Perfil> perfisUsuarioLogado(){
        List<Perfil> perfis = (List<Perfil>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Perfil.PERFIL_USUARIO);
        if(perfis != null){
            return perfis;
        } 
        return new ArrayList<Perfil>();
    }
    
}

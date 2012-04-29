/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.producer;

import com.jsf.conventions.qualifier.UsuarioLogado;
import com.rmpestano.cdiinterceptor.model.Perfil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author rmpestano
 */
public class PerfilProducer {
    
    @Produces @UsuarioLogado @Named(value="perfis") 
    public List<Perfil> perfisUsuarioLogado(){
        List<Perfil> perfis = (List<Perfil>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Perfil.PERFIL_USUARIO);
        if(perfis != null){
            return perfis;
        } 
        return new ArrayList<Perfil>();
    }
    
}

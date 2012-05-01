/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.cdiinterceptor.observer;

import com.rmpestano.cdiinterceptor.model.Perfil;
import java.util.List;

/**
 *
 * @author rmpestano
 */
public class PerfilChange {
    
    private List<Perfil> perfis;

    public PerfilChange(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<Perfil> perfis) {
        this.perfis = perfis;
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.cdiinterceptor.controller;

import com.jsf.conventions.qualifier.SecurityMethod;
import com.jsf.conventions.qualifier.UsuarioLogado;
import com.jsf.conventions.util.ConstantUtils;
import com.jsf.conventions.util.MessagesController;
import com.rmpestano.cdiinterceptor.model.Perfil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.primefaces.model.DualListModel;

/**
 *
 * @author rmpestano
 */
@ViewAccessScoped
@Named(value = "perfilMBean")
public class PerfilMBean implements Serializable{

    @Inject @UsuarioLogado
    private List<Perfil> perfisUtilizados;
    private List<Perfil> perfisDisponiveis;
    private Perfil perfil;
    private DualListModel<Perfil> perfis;  
    
    @PostConstruct
    public void inicializaPerfis(){
        perfisDisponiveis = new ArrayList<Perfil>();
        perfisDisponiveis.add(new Perfil(ConstantUtils.ADMIN));
        perfisDisponiveis.add(new Perfil(ConstantUtils.VISITANTE));
        perfisDisponiveis.add(new Perfil("Usuário"));
        perfis = new DualListModel<Perfil>(perfisDisponiveis, perfisUtilizados);
        
    }

    public List<Perfil> getPerfisDisponiveis() {
        return perfisDisponiveis;
    }

    public void setPerfisDisponiveis(List<Perfil> perfisDisponiveis) {
        this.perfisDisponiveis = perfisDisponiveis;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    public List<Perfil> getPerfisUtilizados() {
        return perfisUtilizados;
    }

    public DualListModel<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(DualListModel<Perfil> perfis) {
        this.perfis = perfis;
    }

    
    @SecurityMethod(rolesAllowed={ConstantUtils.ADMIN},message="Somente o perfil de administrador tem permissão para executar esta ação")
    public void adicionarPerfil(){
        perfisDisponiveis.add(perfil);
        perfis.getSource().add(perfil);
    }
    
    @SecurityMethod(rolesAllowed=ConstantUtils.ADMIN)
    public void removerPerfil(Perfil p){
        perfisDisponiveis.remove(p);
        perfis.getSource().remove(p);
        
    }

    @SecurityMethod(rolesAllowed=ConstantUtils.ADMIN)
    public void associarPerfis(){
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Perfil.PERFIL_USUARIO,perfis.getTarget());
         MessagesController.addInfo("Perfil alterado com sucesso!");
    }
    
    public void atualizaPicklist(){
         perfis = new DualListModel<Perfil>(perfisDisponiveis, perfisUtilizados);
    }
    
    public String reset(){
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return null;
    }
}

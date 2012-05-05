/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.cdiinterceptor.controller;

import com.rmpestano.cdiinterceptor.qualifier.SecurityMethod;
import com.rmpestano.cdiinterceptor.qualifier.UsuarioLogado;
import com.rmpestano.cdiinterceptor.util.ConstantUtils;
import com.rmpestano.cdiinterceptor.util.MessagesController;
import com.rmpestano.cdiinterceptor.model.Perfil;
import com.rmpestano.cdiinterceptor.observer.PerfilChange;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.event.Event;
import javax.enterprise.inject.New;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author rmpestano
 */
@ViewAccessScoped
@Named(value = "perfilMBean")
public class PerfilMBean implements Serializable{

 
    private List<Perfil> perfisDisponiveis;
    @Inject @New
    private Perfil perfil;
    private DualListModel<Perfil> perfis;  
    @Inject
    private Event<PerfilChange> perfilChangeEvent;
    @Inject @UsuarioLogado
    private List<Perfil> perfisUsuario;
    
    
    @PostConstruct
    public void inicializaPerfis(){
        perfisDisponiveis = new ArrayList<Perfil>();
        List<Perfil> perfisUtilizados = new ArrayList<Perfil>();
        Perfil admin = new Perfil(ConstantUtils.ADMIN);
        perfisUtilizados.add(admin);
        perfisDisponiveis.add(new Perfil(ConstantUtils.VISITANTE));
        perfisDisponiveis.add(new Perfil(ConstantUtils.USER));
        perfis = new DualListModel<Perfil>(perfisDisponiveis, perfisUtilizados);
        perfilChangeEvent.fire(new PerfilChange(perfisUtilizados));
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
    

    public DualListModel<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(DualListModel<Perfil> perfis) {
        this.perfis = perfis;
    }

    
    @SecurityMethod(rolesAllowed={ConstantUtils.ADMIN},message="Somente usuários com perfil de <span style='text-decoration:underline;color: blue;font-size='13px'>administrador</span> tem permissão para executar esta ação.")
    public void adicionarPerfil(){
        if(perfisDisponiveis.contains(perfil) || perfisUsuario.contains(perfil)){
            MessagesController.addError("Perfil já existe.");
            RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);//callback Param to keep dialog open
            return;
        }
        perfisDisponiveis.add(perfil);
        atualizaPicklist();
        perfil = new Perfil();
        MessagesController.addInfo("Perfil incluido com sucesso!");
         
    }
    
    @SecurityMethod(rolesAllowed=ConstantUtils.ADMIN)
    public void removerPerfil(Perfil p){
        perfisDisponiveis.remove(p);
        atualizaPicklist();
        MessagesController.addInfo("Perfil removido com sucesso!");
    }
    
    
    @SecurityMethod(rolesAllowed=ConstantUtils.ADMIN,message="Somente o perfil <span style='text-decoration:underline;color: blue;font-size='13px'>admininistrador</span> pode executar está ação.")
    public void actionPicklist(){
         setPerfisDisponiveis(perfis.getSource());
         atualizaPerfilUsuario();
    }
    
    public void atualizaPicklist(){
        perfis.setSource(perfisDisponiveis);
        perfis.setTarget(perfisUsuario);
    }
     
    
    private void atualizaPerfilUsuario(){
         perfilChangeEvent.fire(new PerfilChange(perfis.getTarget()));
         MessagesController.addInfo("Perfil alterado com sucesso!");
    }
    
    public String reset() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "home.faces?faces-redirect=true";
    }
    
    @SecurityMethod(rolesAllowed=ConstantUtils.ADMIN,message="Você só poderá executar este metodo se o perfil <span style='text-decoration:underline;color: blue;font-size='13px'>administrador</span> estiver entre os perfis associados.")
    public void metodoAdministrador(){
        MessagesController.addInfo("Metodo administrador executado com sucesso");
    }
    
    @SecurityMethod(rolesAllowed={ConstantUtils.USER},message="Você só poderá executar este metodo se o perfil  <span style='text-decoration:underline;color: blue;font-size='13px'>Usuario</span> estiver entre os perfis associados.")
    public void metodoUsuario(){
        MessagesController.addInfo("Metodo usuário executado com sucesso");
    }
    @SecurityMethod(rolesAllowed={ConstantUtils.ADMIN,ConstantUtils.USER},message="Você só poderá executar este metodo se o perfil <span style='text-decoration:underline;color: blue;font-size='13px'>administrador</span> e/ou <span style='text-decoration:underline;color: blue;font-size='13px'>usuario</span> estiverem entre os perfis associados.")
    public void metodoAdministradorUsuario(){
        MessagesController.addInfo("Metodo administrador/usuário executado com sucesso");
    }
}

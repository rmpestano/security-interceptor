/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rmpestano.cdiinterceptor;

import com.jsf.conventions.qualifier.SecurityMethod;
import com.jsf.conventions.qualifier.UsuarioLogado;
import com.jsf.conventions.util.MessagesController;
import com.rmpestano.cdiinterceptor.model.Perfil;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author rmpestano
 */
@Interceptor//anotação que define esta classe como um interceptor
@SecurityMethod //qualificador que define quais metodos o interceptor ira atuar
public class SecurityInterceptor implements Serializable{

    @Inject @UsuarioLogado
    private List<Perfil> perfisUsuario;

    @AroundInvoke
    public Object checkPermission(InvocationContext ic) throws Exception {
        try {
            String[] rolesAllowed = this.extractMethodRoles(ic.getMethod());
            if (rolesAllowed != null && rolesAllowed.length > 0) {
                if (!this.checkUserPermissions(rolesAllowed)) {
                    MessagesController.addFatal(ic.getMethod().getAnnotation(SecurityMethod.class).message());
                    return null;
                }
            }
            return ic.proceed();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * metodo responsável por decidir se o usuário corrente tem permissão para
     * executar um metodo
     *
     * @param lista de nome de perfis que possuem permissão para executar o método
     * @return true se o usuário logado possue permisão e false caso contrario
     */
    public boolean checkUserPermissions(String[] rolesAllowed) {
        
        if (perfisUsuario == null || perfisUsuario.isEmpty()) {
            return false;
        }
        for (String role : rolesAllowed) {
            if (perfisUsuario.contains(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * rotina responsavel pela extração dos perfis de um método anotado com @SecurityMethod
     * 
     * @param metodo
     * @return 
     */
    private String[] extractMethodRoles(Method m) {
        if (m.isAnnotationPresent(SecurityMethod.class)) {//verifica a presença da anotação de segurança
            SecurityMethod securityMethod = m.getAnnotation(SecurityMethod.class);
            return securityMethod.rolesAllowed();//retorna perfis através da propriedade rolesAllowed
        }
        return null;
    }
}

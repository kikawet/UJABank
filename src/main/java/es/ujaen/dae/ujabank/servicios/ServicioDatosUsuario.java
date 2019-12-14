/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.servicios;

import es.ujaen.dae.ujabank.DAO.DAOUsuario;
import es.ujaen.dae.ujabank.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author flo00008
 */
@Component
public class ServicioDatosUsuario implements UserDetailsService{
    
    @Autowired
    private DAOUsuario usuarios;//Inyecto directamente el dao ya que el banco no tiene este tipo de opreaciones

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        
        Usuario usuario = usuarios.buscar(dni);
        
        if(usuario == null){
            throw new UsernameNotFoundException("No existe usuario con ese dni");
        }
        
        return User.withUsername(dni).password(usuario.getContrasena()).roles("DEFAULT").build();
    }
    
    
}

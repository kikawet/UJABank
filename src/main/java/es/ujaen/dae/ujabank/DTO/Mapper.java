/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Tarjeta;
import es.ujaen.dae.ujabank.entidades.Usuario;

/**
 *
 * @author lopez
 */
public class Mapper {
    
    public static Usuario usuarioMapper(DTOUsuario usuario){
        Usuario u = new Usuario();
        u.setDni(usuario.getDni());
        u.setDomicilio(usuario.getDomicilio());
        u.setEmail(usuario.getEmail());
        u.setNombre(usuario.getNombre());
        u.setTelefono(usuario.getTelefono());
        u.setfNacimiento(usuario.getfNacimiento());        
        return u;        
    }
    
    public static Cuenta cuentaMapper(DTOCuenta cuenta){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.      
    }
    
    public static Tarjeta tarjetaMapper(DTOTarjeta tarjeta){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.      
    }
    
    
}

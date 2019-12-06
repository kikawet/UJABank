/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.entidades.Usuario;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author axpos
 */
public interface ServiciosUsuario {

    public boolean registrar(Usuario usuario);

    public boolean login(Usuario usuario);
        
    public boolean borrarUsuario(String dni);
    
//    public boolean logout(String dni);

    public boolean crearCuenta(String dni);

    public List<DTOCuenta> consultarCuentas(String dni);
}

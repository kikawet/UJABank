/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOUsuario;

/**
 *
 * @author axpos
 */
public interface ServiciosUsuario {
    
   public boolean registrar(DTOUsuario usuario);
   public int login(DTOUsuario usuario);
   public boolean crearCuenta(String token);
}
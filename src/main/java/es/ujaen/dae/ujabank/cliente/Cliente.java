/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.cliente;

import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author axpos
 */
public class Cliente {

    public static void run(ApplicationContext contexto) {
        ServiciosUsuario banco = (ServiciosUsuario) contexto.getBean("banco");

        DTOUsuario usuario = new DTOUsuario();
        usuario.setNombre("Pepe");

        System.out.println(banco.registrar(usuario,"DAE"));
    }

}

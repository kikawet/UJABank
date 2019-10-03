/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTarjeta;
import java.util.Date;
import java.util.List;

/**
 *
 * @author axpos
 */
public interface ServiciosTransacciones {

    public boolean ingresar(String token, DTOTarjeta origen, DTOCuenta destino, int cantidad);

    public boolean transferir(String token, DTOCuenta origen, DTOCuenta destino, int cantidad);

    public boolean retirar(String token, DTOCuenta origen, DTOTarjeta destino, int cantidad);

    public List<Transaccion> consultar(String token, DTOCuenta cuenta, Date inicio, Date fin);
}

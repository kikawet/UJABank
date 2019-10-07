/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.entidades.Tarjeta;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author axpos
 */
public interface ServiciosTransacciones {

    public boolean ingresar(UUID token, Tarjeta origen, DTOCuenta destino, float cantidad);

    public boolean transferir(UUID token, DTOCuenta origen, DTOCuenta destino, float cantidad, String concepto);

    public boolean retirar(UUID token, DTOCuenta origen, Tarjeta destino, float cantidad);

    public List<Transaccion> consultar(UUID token, DTOCuenta cuenta, Date inicio, Date fin);
}

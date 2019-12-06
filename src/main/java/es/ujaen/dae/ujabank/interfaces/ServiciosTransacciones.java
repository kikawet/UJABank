/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author axpos
 */
public interface ServiciosTransacciones {

    public boolean ingresar(String id, Tarjeta origen, int idDestino, float cantidad);

    public boolean transferir(String id, int idOrigen, int idDestino, float cantidad, String concepto);

    public boolean retirar(String id, int idOrigen, Tarjeta destino, float cantidad);

    public CompletableFuture<List<DTOTransaccion>> consultar(String id, int idCuenta, Date inicio, Date fin);
}

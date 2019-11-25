/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author axpos
 */
@Entity
public class Ingreso extends Transaccion implements Serializable {

    private int idOrigen;
    private int idDestino;

    public Ingreso() {
    }

    public int getIDOrigen() {
        return idOrigen;
    }

    public void setOrigen(int _idOrigen) {
        this.idOrigen = _idOrigen;
    }

    public int getIDDestino() {
        return idDestino;
    }

    public void setIDDestino(int idCuentaDestino) {
        this.idDestino = idCuentaDestino;
    }

    @Override
    public String toString() {
        return "Ingreso :\n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "Número tarjeta origen: " + this.getIDOrigen() + "\n"
                + "ID cuenta destino: " + this.getIDDestino() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }

    @Override
    public DTOTransaccion.TIPO getTipo() {
        return DTOTransaccion.TIPO.ingreso;
    }

}

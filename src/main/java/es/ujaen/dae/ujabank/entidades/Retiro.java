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
public class Retiro extends Transaccion implements Serializable {
    
    private int idOrigen;
    private int idDestino;

    public Retiro() {
    }

    public int getIDOrigen() {
        return idOrigen;
    }

    public void setIDOrigen(int idCuentaOrigen) {
        this.idOrigen = idCuentaOrigen;
    }

    public int getIDDestino() {
        return idDestino;
    }

    public void setDestino(int _idDestino) {
        this.idDestino = _idDestino;
    }

    @Override
    public String toString() {
        return "Retirada: \n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "ID cuenta origen: " + this.getIDOrigen() + "\n"
                + "Número cuenta destino: " + this.getIDDestino() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }

    @Override
    public DTOTransaccion.TIPO getTipo() {
        return DTOTransaccion.TIPO.retiro;
    }
}

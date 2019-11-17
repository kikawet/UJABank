/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 *
 * @author axpos
 */
@Entity 

public class Transferencia extends Transaccion implements Serializable {
    
    private int idOrigen;
    private int idDestino;
    private String concepto;

    public Transferencia() {

    }
    
    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String _concepto) {
        this.concepto = _concepto;
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

    public void setIDDestino(int idCuentaDestino) {
        this.idDestino = idCuentaDestino;
    }

    @Override
    public String toString() {
        return "Transferencia: \n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "ID cuenta origen: " + this.getIDOrigen() + "\n"
                + "ID cuenta destino: " + this.getIDDestino() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad() + "\n"
                + "Concepto: " + this.getConcepto();
    }

    @Override
    public DTOTransaccion.TIPO getTipo() {
        return DTOTransaccion.TIPO.transferencia;
    }

}

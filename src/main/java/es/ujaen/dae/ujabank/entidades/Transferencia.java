/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.interfaces.Transaccion;

/**
 *
 * @author axpos
 */
public class Transferencia extends Transaccion {

    private int _idOrigen;
    private int _idDestino;
    private String _concepto;

    public Transferencia() {

    }

    public String getConcepto() {
        return _concepto;
    }

    public void setConcepto(String _concepto) {
        this._concepto = _concepto;
    }

    public int getIDOrigen() {
        return _idOrigen;
    }

    public void setIDOrigen(int idCuentaOrigen) {
        this._idOrigen = idCuentaOrigen;
    }

    public int getIDDestino() {
        return _idDestino;
    }

    public void setIDDestino(int idCuentaDestino) {
        this._idDestino = idCuentaDestino;
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

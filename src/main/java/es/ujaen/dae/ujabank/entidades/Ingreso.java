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
public class Ingreso extends Transaccion {

    private int _idOrigen;
    private int _idDestino;

    public Ingreso() {
    }

    public int getIDOrigen() {
        return _idOrigen;
    }

    public void setOrigen(int _idOrigen) {
        this._idOrigen = _idOrigen;
    }

    public int getIDDestino() {
        return _idDestino;
    }

    public void setIDDestino(int idCuentaDestino) {
        this._idDestino = idCuentaDestino;
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

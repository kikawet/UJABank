/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.interfaces.Transaccion;

/**
 *
 * @author axpos
 */
public class Ingreso extends Transaccion {

    private Tarjeta _origen;
    private int _idDestino;

    public Ingreso() {
    }

    public Tarjeta getOrigen() {
        return _origen;
    }

    public void setOrigen(Tarjeta _origen) {
        this._origen = _origen;
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
                + "Número tarjeta origen: " + this.getOrigen().getNumero() + "\n"
                + "ID cuenta destino: " + this.getIDDestino() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }

    @Override
    public TIPO getTipo() {
        return TIPO.ingreso;
    }

}

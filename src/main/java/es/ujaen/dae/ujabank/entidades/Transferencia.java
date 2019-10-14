/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.interfaces.Transaccion;

/**
 *
 * @author axpos
 */
public class Transferencia extends Transaccion {

    private Cuenta _origen;
    private Cuenta _destino;
    private String _concepto;

    public Transferencia() {

    }

    public String getConcepto() {
        return _concepto;
    }

    public void setConcepto(String _concepto) {
        this._concepto = _concepto;
    }

    public Cuenta getOrigen() {
        return _origen;
    }

    public void setOrigen(Cuenta _origen) {
        this._origen = _origen;
    }

    public Cuenta getDestino() {
        return _destino;
    }

    public void setDestino(Cuenta _destino) {
        this._destino = _destino;
    }

    @Override
    public String toString() {
        return "Transferencia: \n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "ID cuenta origen: " + this.getOrigen().getId() + "\n"
                + "ID cuenta destino: " + this.getDestino().getId() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad() + "\n"
                + "Concepto: " + this.getConcepto();
    }

}

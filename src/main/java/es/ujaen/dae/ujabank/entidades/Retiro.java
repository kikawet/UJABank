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
public class Retiro extends Transaccion {

    private Cuenta _origen;
    private Tarjeta _destino;

    public Retiro() {
    }

    public Cuenta getOrigen() {
        return _origen;
    }

    public void setOrigen(Cuenta _origen) {
        this._origen = _origen;
    }

    public Tarjeta getDestino() {
        return _destino;
    }

    public void setDestino(Tarjeta _destino) {
        this._destino = _destino;
    }

    @Override
    public String toString() {
        return "Retirada: \n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "ID cuenta origen: " + this.getOrigen().getId() + "\n"
                + "Número cuenta destino: " + this.getDestino().getNumero() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }
}

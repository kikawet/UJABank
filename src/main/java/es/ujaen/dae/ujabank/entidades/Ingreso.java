/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.interfaces.Transaccion;
import java.util.logging.Logger;

/**
 *
 * @author axpos
 */
public class Ingreso extends Transaccion {

    private Tarjeta _origen;
    private Cuenta _destino;

    public Ingreso() {
    }

    public Tarjeta getOrigen() {
        return _origen;
    }

    public void setOrigen(Tarjeta _origen) {
        this._origen = _origen;
    }

    public Cuenta getDestino() {
        return _destino;
    }

    public void setDestino(Cuenta _destino) {
        this._destino = _destino;
    }

}

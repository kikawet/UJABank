/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import java.util.Date;

/**
 *
 * @author axpos
 */
public abstract class Transaccion {

    protected float _cantidad;
    protected Date _fecha;

    public float getCantidad() {
        return _cantidad;
    }

    public void setCantidad(float _cantidad) {
        this._cantidad = _cantidad;
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }
}

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
public class Retiro extends Transaccion {

    private int _idOrigen;
    private Tarjeta _destino;
    
    public Retiro() {
    }

    public int getIDOrigen() {
        return _idOrigen;
    }

    public void setIDOrigen(int idCuentaOrigen) {
        this._idOrigen = idCuentaOrigen;
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
                + "ID cuenta origen: " + this.getIDOrigen() + "\n"
                + "Número cuenta destino: " + this.getDestino().getNumero() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }

    @Override
    public TIPO getTipo() {
        return TIPO.retiro;
    }
}

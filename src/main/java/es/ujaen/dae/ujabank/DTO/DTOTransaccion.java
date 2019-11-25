/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import java.util.Date;

/**
 *
 * @author axpos
 */
public class DTOTransaccion {

    public static enum TIPO {
        ingreso,
        retiro,
        transferencia
    }

    private TIPO _tipo;
    private int _origen;
    private Date _fecha;
    private int _destino;
    private String _concepto;
    private double _cantidad;

    public DTOTransaccion() {

        this._tipo = null;
        this._cantidad = 0;
        this._concepto = null;
        this._origen = 0;
        this._destino = 0;
        this._fecha = null;

    }

    public TIPO getTipo() {
        return _tipo;
    }

    public void setTipo(TIPO _tipo) {
        this._tipo = _tipo;
    }

    public int getOrigen() {
        return _origen;
    }

    public void setOrigen(int _origen) {
        this._origen = _origen;
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public int getDestino() {
        return _destino;
    }

    public void setDestino(int _destino) {
        this._destino = _destino;
    }

    public String getConcepto() {
        return _concepto;
    }

    public void setConcepto(String _concepto) {
        this._concepto = _concepto;
    }

    public double getCantidad() {
        return _cantidad;
    }

    public void setCantidad(double _cantidad) {
        this._cantidad = _cantidad;
    }

}

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
public class DTOTarjeta {

    private String _titular;
    private String _numero;
    private Date _fCaducidad;
    private String _cvv;

    public String getTitular() {
        return _titular;
    }

    public void setTitular(String _titular) {
        this._titular = _titular;
    }

    public String getNumero() {
        return _numero;
    }

    public void setNumero(String _numero) {
        this._numero = _numero;
    }

    public Date getfCaducidad() {
        return _fCaducidad;
    }

    public void setfCaducidad(Date _fCaducidad) {
        this._fCaducidad = _fCaducidad;
    }

    public String getCvv() {
        return _cvv;
    }

    public void setCvv(String _cvv) {
        this._cvv = _cvv;
    }
}

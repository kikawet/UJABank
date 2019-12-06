/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author axpos
 */
public class Tarjeta {

    private String _titular;
    private int _numero;
    private Date _fCaducidad;
    private String _cvv;

    public Tarjeta() {
    }

    public boolean ingresar(float cantidad) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        return true;// se supone que se modifica
    }

    public boolean retirar(float cantidad) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;// se supone que se modifica
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this._titular);
        hash = 83 * hash + Objects.hashCode(this._numero);
        hash = 83 * hash + Objects.hashCode(this._fCaducidad);
        hash = 83 * hash + Objects.hashCode(this._cvv);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tarjeta other = (Tarjeta) obj;
        if (!Objects.equals(this._titular, other._titular)) {
            return false;
        }
        if (!Objects.equals(this._numero, other._numero)) {
            return false;
        }
        if (!Objects.equals(this._cvv, other._cvv)) {
            return false;
        }
        return Objects.equals(this._fCaducidad, other._fCaducidad);
    }

    public String getTitular() {
        return _titular;
    }

    public void setTitular(String _titular) {
        this._titular = _titular;
    }

    public int getNumero() {
        return _numero;
    }

    public void setNumero(int _numero) {
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

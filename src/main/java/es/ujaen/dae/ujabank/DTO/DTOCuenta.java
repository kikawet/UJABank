/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

/**
 *
 * @author axpos
 */
public class DTOCuenta {

    private int _id;
    private double _saldo;

    public DTOCuenta() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public double getSaldo() {
        return _saldo;
    }

    public void setSaldo(double _saldo) {
        this._saldo = _saldo;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.interfaces.Transaccion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author axpos
 */
public class Cuenta {

    private static int NUMERO_CUENTAS = 0;
    private int _id;
    private float _saldo;
    private List<Transaccion> _historial;

    public Cuenta() {
        this(0);//llama a cuenta con saldo 0
    }

    public Cuenta(float saldo) {// si pongo el saldo en el constructor no necesitaré crear un setter
        this._id = NUMERO_CUENTAS;
        NUMERO_CUENTAS++;

        this._saldo = saldo;
        this._historial = new ArrayList<>();
    }
    
    public Cuenta(int id,float saldo){
        this._id = id;
        this._saldo = saldo;
        this._historial = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this._id;
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
        final Cuenta other = (Cuenta) obj;

        return this._id == other._id;
    }

    public boolean ingresar(Ingreso ingreso) {
        boolean insertado = this._historial.add(ingreso);

        if (insertado) {
            this._saldo += ingreso.getCantidad();//hacer valor absoluto si es necesario
        }
        return insertado;
    }

    public boolean transferir(Transferencia transferencia) {
        boolean insertado = this._historial.add(transferencia);

        if (insertado) {
            if (this.equals(transferencia.getOrigen())) {
                this._saldo -= transferencia.getCantidad();
            } else {
                this._saldo += transferencia.getCantidad();
            }
        }

        return insertado;
    }

    public boolean retirar(Retiro retiro) {
        boolean retirado = this._historial.add(retiro);

        if (retirado) {
            this._saldo -= retiro.getCantidad();
        }

        return retirado;
    }

    public List<Transaccion> consultar(Date inicio, Date fin) {
        ArrayList<Transaccion> consulta = new ArrayList<>();
        
        this._historial.forEach((transaccion) -> {
            if (transaccion.entreFechas(inicio, fin)) {
                consulta.add(transaccion);
            }
        });

        return consulta;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public float getSaldo() {
        return _saldo;
    }
}

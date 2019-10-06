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
    private final int _id;
    private int _saldo;

    public int getSaldo() {
        return _saldo;
    }

    private List<Transaccion> _historial;

    public Cuenta() {
        this._id = NUMERO_CUENTAS;
        NUMERO_CUENTAS++;

        this._saldo = 0;
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
            if (transaccion.getFecha().after(inicio) && transaccion.getFecha().before(fin)) {
                consulta.add(transaccion);
            }
        });

        return consulta;
    }

}

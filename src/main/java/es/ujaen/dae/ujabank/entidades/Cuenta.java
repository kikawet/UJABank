/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 *
 * @author axpos
 */
@Entity
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private float saldo;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE })
    private List<Transaccion> historial;

    public Cuenta() {
//        this(0);//llama a cuenta con saldo 0
    }

    public Cuenta(float saldo) {// si pongo el saldo en el constructor no necesitaré crear un setter
//        this.id = NUMERO_CUENTAS;
//        NUMERO_CUENTAS++;

        this.saldo = saldo;
//        this._historial = new ArrayList<>();
    }

    public Cuenta(int id, float saldo) {
        this.id = id;
        this.saldo = saldo;
//        this._historial = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
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

        return this.id == other.id;
    }

    public boolean ingresar(Ingreso ingreso) {
        boolean insertado = true;//this._historial.add(ingreso);

        if (insertado) {
            this.saldo += ingreso.getCantidad();//hacer valor absoluto si es necesario
        }
        return insertado;
    }

    public boolean transferir(Transferencia transferencia) {
        boolean insertado = true;//this._historial.add(transferencia);

        if (insertado) {
            if (this.getId() == transferencia.getIDOrigen()) {
                this.saldo -= transferencia.getCantidad();
            } else {
                this.saldo += transferencia.getCantidad();
            }
        }

        return insertado;
    }

    public boolean retirar(Retiro retiro) {
        boolean retirado = true;//this._historial.add(retiro);

        if (retirado) {
            this.saldo -= retiro.getCantidad();
        }

        return retirado;
    }

    public List<Transaccion> consultar(Date inicio, Date fin) {
        ArrayList<Transaccion> consulta = new ArrayList<>();

//        this._historial.forEach((transaccion) -> {
//            if (transaccion.entreFechas(inicio, fin)) {
//                consulta.add(transaccion);
//            }
//        });

        return consulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public float getSaldo() {
        return saldo;
    }
}

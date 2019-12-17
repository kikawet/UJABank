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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author axpos
 */
@Entity
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    private double saldo;

    @ManyToOne(fetch = FetchType.EAGER)//Muy pocas veces no se comprueba el usuario(solo en el destino de transferencia)
    @JoinColumn(name = "propietario")
    Usuario propietario;

    @JoinTable(name = "transacciones")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    /*No se puede indexar por fecha este historial pero se puede @OrderColumn
    que guarda en la DB el orden de la lista, por ahora solo hay index en transaccion*/
    private List<Transaccion> historial;

    public Cuenta() {
    }

    public Cuenta(float saldo, Usuario propietario) {
        this.saldo = saldo;
        this.propietario = propietario;
    }

    public Cuenta(int id, double saldo) {
        this.id = id;
        this.saldo = saldo;
        this.historial = new ArrayList<>();
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
        this.saldo += ingreso.getCantidad();//hacer valor absoluto si es necesario
        return this.historial.add(ingreso);
    }

    public boolean transferir(Transferencia transferencia) {

        if (this.getID() == transferencia.getIDOrigen()) {
            this.saldo -= transferencia.getCantidad();
        } else {
            this.saldo += transferencia.getCantidad();
        }

        return this.historial.add(transferencia);
    }

    public boolean retirar(Retiro retiro) {
        this.saldo -= retiro.getCantidad();
        return this.historial.add(retiro);
    }

    public List<Transaccion> consultar(Date inicio, Date fin) {
        ArrayList<Transaccion> consulta = new ArrayList<>();
        int i = 0;
        Transaccion transaccion = this.historial.get(i);

        while (i < this.historial.size() && transaccion.beforeFecha(fin)) {
            i++;
            transaccion = this.historial.get(i);

            if (transaccion.entreFechas(inicio, fin)) {
                consulta.add(transaccion);
            }
        }

        return consulta;
    }

    public int getID() {
        return id;
    }

    public void setID(int _id) {
        this.id = _id;
    }

    public double getSaldo() {
        return saldo;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

}

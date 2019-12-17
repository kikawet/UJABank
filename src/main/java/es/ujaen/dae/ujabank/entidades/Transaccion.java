/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author axpos
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = {
    @Index(columnList = "fecha")})
public abstract class Transaccion implements Serializable {

    protected double cantidad;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha")
    protected Date fecha;

    @Id
    @GeneratedValue
    @Column(name = "id")//el nombre es el mismo pero de este modo se confirma    
    private long id;

    @Override
    public abstract String toString();

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(float _cantidad) {
        this.cantidad = _cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date _fecha) {
        this.fecha = _fecha;
    }

    public abstract DTOTransaccion.TIPO getTipo();

    public boolean beforeFecha(Date fecha) {
        return this.getFecha().before(fecha);
    }

    public boolean entreFechas(Date inicio, Date fin) {
        return this.getFecha().after(inicio) && this.getFecha().before(fin);
    }

}

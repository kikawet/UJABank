/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author axpos
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Transaccion implements Serializable {

    protected float cantidad;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date fecha;
    @Id
    @GeneratedValue
    private long id;
    @Override
    public abstract String toString();

    public float getCantidad() {
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

    public boolean entreFechas(Date inicio, Date fin) {
        Calendar Cfecha = Calendar.getInstance(), Cinicio = Calendar.getInstance(), Cfin = Calendar.getInstance();

        Cfecha.setTime(this.getFecha());
        Cfecha.set(Calendar.HOUR_OF_DAY, 0);
        Cfecha.set(Calendar.MINUTE, 0);
        Cfecha.set(Calendar.SECOND, 0);
        Cfecha.set(Calendar.MILLISECOND, 0);

        Cinicio.setTime(this.getFecha());
        Cinicio.set(Calendar.HOUR_OF_DAY, 0);
        Cinicio.set(Calendar.MINUTE, 0);
        Cinicio.set(Calendar.SECOND, 0);
        Cinicio.set(Calendar.MILLISECOND, 0);

        Cfin.setTime(this.getFecha());
        Cfin.set(Calendar.HOUR_OF_DAY, 0);
        Cfin.set(Calendar.MINUTE, 0);
        Cfin.set(Calendar.SECOND, 0);
        Cfin.set(Calendar.MILLISECOND, 0);

        return (Cfecha.equals(Cinicio) || Cfecha.equals(Cfin) || Cfecha.after(inicio) && Cfecha.before(fin));
    }

}

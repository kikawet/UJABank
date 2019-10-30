/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.interfaces;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author axpos
 */
public abstract class Transaccion {

    protected float _cantidad;
    protected Date _fecha;

    @Override
    public abstract String toString();

    public float getCantidad() {
        return _cantidad;
    }

    public void setCantidad(float _cantidad) {
        this._cantidad = _cantidad;
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
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

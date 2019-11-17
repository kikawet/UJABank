/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Ingreso;
import es.ujaen.dae.ujabank.entidades.Retiro;
import es.ujaen.dae.ujabank.entidades.Transferencia;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.entidades.Transaccion;

/**
 *
 * @author lopez
 */
public class Mapper {

    public static Usuario usuarioMapper(DTOUsuario usuario) {
        if (usuario == null) {
            return null;
        }

        Usuario u = new Usuario();
        u.setDni(usuario.getDni());
        u.setDomicilio(usuario.getDomicilio());
        u.setEmail(usuario.getEmail());
        u.setNombre(usuario.getNombre());
        u.setTelefono(usuario.getTelefono());
        u.setfNacimiento(usuario.getfNacimiento());
        return u;
    }

    public static Cuenta cuentaMapper(DTOCuenta dtoCuenta) {
        if (dtoCuenta == null) {
            return null;
        }

        Cuenta cuenta = new Cuenta(dtoCuenta.getId(), dtoCuenta.getSaldo());
        return cuenta;
    }

    public static DTOCuenta dtoCuentaMapper(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }

        DTOCuenta dtoCuenta = new DTOCuenta();
        dtoCuenta.setId(cuenta.getId());
        dtoCuenta.setSaldo(cuenta.getSaldo());
        return dtoCuenta;
    }

    public static DTOTransaccion dtoTransaccionMapper(Transaccion transaccion) {

        if (transaccion == null) {
            return null;
        }

        DTOTransaccion dtoTransaccion = new DTOTransaccion();
        dtoTransaccion.setCantidad(transaccion.getCantidad());
        dtoTransaccion.setFecha(transaccion.getFecha());

        if (transaccion instanceof Ingreso) {
            Ingreso i = (Ingreso) transaccion;
            dtoTransaccion.setOrigen(i.getIDOrigen());
            dtoTransaccion.setDestino(i.getIDDestino());
            dtoTransaccion.setTipo(DTOTransaccion.TIPO.ingreso);

        } else if (transaccion instanceof Transferencia) {
            Transferencia t = (Transferencia) transaccion;
            dtoTransaccion.setOrigen(t.getIDOrigen());
            dtoTransaccion.setDestino(t.getIDDestino());
            dtoTransaccion.setConcepto(t.getConcepto());
            dtoTransaccion.setTipo(DTOTransaccion.TIPO.transferencia);

        } else {
            Retiro r = (Retiro) transaccion;
            dtoTransaccion.setOrigen(r.getIDOrigen());
            dtoTransaccion.setDestino(r.getIDDestino());
            dtoTransaccion.setTipo(DTOTransaccion.TIPO.retiro);

        }
        return dtoTransaccion;

    }

}

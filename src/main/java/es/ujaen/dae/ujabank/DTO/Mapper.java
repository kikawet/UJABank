/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Tarjeta;
import es.ujaen.dae.ujabank.entidades.Usuario;

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

        Cuenta cuenta = new Cuenta(dtoCuenta.getSaldo());
        cuenta.setId(dtoCuenta.getId());
        return cuenta;
    }

    public static DTOCuenta dtoCuentaMapper(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }

        DTOCuenta dtoCuenta = new DTOCuenta();
        dtoCuenta.setId(cuenta.getSaldo());
        dtoCuenta.setSaldo(cuenta.getSaldo());
        return dtoCuenta;
    }

    public static Tarjeta tarjetaMapper(DTOTarjeta dtoTarjeta) {
        if (dtoTarjeta == null) {
            return null;
        }

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setCvv(dtoTarjeta.getCvv());
        tarjeta.setNumero(dtoTarjeta.getNumero());
        tarjeta.setTitular(dtoTarjeta.getTitular());
        tarjeta.setfCaducidad(dtoTarjeta.getfCaducidad());
        return tarjeta;
    }

}

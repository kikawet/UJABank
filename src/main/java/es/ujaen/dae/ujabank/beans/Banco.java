/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import es.ujaen.dae.ujabank.DAO.DAOCuenta;
import es.ujaen.dae.ujabank.DAO.DAOUsuario;
import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.config.SeguridadUJABank;
import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Transaccion;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.excepciones.*;
import es.ujaen.dae.ujabank.excepciones.formato.CantidadNegativa;
import es.ujaen.dae.ujabank.excepciones.formato.ConceptoIncorrecto;
import es.ujaen.dae.ujabank.excepciones.formato.CuentaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.FechaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.TarjetaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.UsuarioIncorrecto;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author axpos
 */
@Component
public class Banco implements ServiciosTransacciones, ServiciosUsuario {

    @Autowired
    private DAOUsuario usuariosBanco;

    @Autowired
    private DAOCuenta cuentasBanco;

    @Autowired
    private ConversorUJACoin euro_UJACoin;
    
    @Autowired
    private SeguridadUJABank seguridad;

    public Banco() {
    }

    @Override
    public boolean ingresar(String id, Tarjeta origen, int idDestino, float cantidad) throws IllegalAccessError, InvalidParameterException {

        Usuario usuario = this.usuariosBanco.buscar(id);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        if (origen == null) {
            throw new TarjetaIncorrecta();
        }

        if (idDestino < 0) {
            throw new CuentaIncorrecta();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        Cuenta cuenta = this.cuentasBanco.buscar(idDestino);

        if (cuenta == null) {
            throw new CuentaIncorrecta();
        }

        if (!usuario.equals(cuenta.getPropietario())) {
            throw new CuentaNoPerteneceUsuario();
        }

        origen.retirar(cantidad);

        cantidad *= euro_UJACoin.euroToUC();

        return this.cuentasBanco.ingresar(origen, cuenta, cantidad) != null;
    }

    @Override
    public boolean transferir(String id, int idOrigen, int idDestino, float cantidad, String concepto) throws InvalidParameterException, IllegalAccessError {

        Usuario usuario = this.usuariosBanco.buscar(id);
        
        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        if (idOrigen < 0) {
            throw new CuentaIncorrecta();
        }

        if (idDestino == 0) {
            throw new CuentaIncorrecta();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        if (concepto == null) {
            throw new ConceptoIncorrecto();
        }

        Cuenta cOrigen = this.cuentasBanco.buscar(idOrigen);
        Cuenta cDestino = this.cuentasBanco.buscar(idDestino);

        if (cOrigen == null) {
            throw new CuentaIncorrecta();
        }

        if (!usuario.equals(cOrigen.getPropietario())) {
            throw new CuentaNoPerteneceUsuario();
        }

        if (cantidad > cOrigen.getSaldo()) {
            throw new CuentaSaldoInsuficiente();
        }

        if (cDestino == null) {
            throw new CuentaIncorrecta();
        }

        return this.cuentasBanco.transferir(cOrigen, cDestino, cantidad, concepto) != null;
    }

    @Override
    public boolean retirar(String id, int idOrigen, Tarjeta destino, float cantidad) throws InvalidParameterException, IllegalAccessError {

        Usuario usuario = this.usuariosBanco.buscar(id);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        if (idOrigen < 0) {
            throw new CuentaIncorrecta();
        }

        if (destino == null) {
            throw new TarjetaIncorrecta();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        Cuenta cuenta = this.cuentasBanco.buscar(idOrigen);

        if (cuenta == null) {
            throw new CuentaIncorrecta();
        }

        if (!usuario.equals(cuenta.getPropietario())) {
            throw new CuentaNoPerteneceUsuario();
        }

        if (cantidad > cuenta.getSaldo()) {
            throw new CuentaSaldoInsuficiente();
        }

        boolean retiro = this.cuentasBanco.retirar(cuenta, destino, cantidad) != null;

        cantidad *= euro_UJACoin.UCToEuro();
        destino.ingresar(cantidad);

        return retiro;
    }

    @Override
    @Async
    public CompletableFuture<List<DTOTransaccion>> consultar(String id, int idCuenta, Date inicio, Date fin) throws InvalidParameterException, IllegalAccessError {

        if(id == null){
            throw new ErrorAutorizacion();
        }

        if (idCuenta < 0) {
            throw new CuentaIncorrecta();
        }

        if (inicio == null) {
            throw new FechaIncorrecta();
        }

        if (fin == null) {
            throw new FechaIncorrecta();
        }

        if (fin.before(inicio)) {
            throw new FechaFinAnteriorAInicio();
        }

        Usuario usuario = this.usuariosBanco.buscar(id);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        Cuenta cuenta = this.cuentasBanco.buscar(idCuenta);

        if (cuenta == null) {
            throw new CuentaIncorrecta();
        }

        if (!usuario.equals(cuenta.getPropietario())) {
            throw new CuentaNoPerteneceUsuario();
        }

        CompletableFuture<List<Transaccion>> listaTransacciones = this.cuentasBanco.consultarTransacciones(cuenta, inicio, fin);

        List<DTOTransaccion> dtoTransacciones = new ArrayList<>();

        try {
            List<Transaccion> lt = listaTransacciones.get();

            lt.forEach((transaccion) -> {
                dtoTransacciones.add(Mapper.dtoTransaccionMapper(transaccion));
            });

        } catch (InterruptedException | ExecutionException ex) {
        }

        return CompletableFuture.completedFuture(dtoTransacciones);
    }

    @Override
    public boolean registrar(Usuario usuario) throws InvalidParameterException {
        if (usuario == null) {
            throw new UsuarioIncorrecto();
        }

        if (usuario.getContrasena() == null) {
            throw new ContrasenaIncorrecta();
        }

        if (usuario.getContrasena().isBlank()) {
            throw new ContrasenaIncorrecta();
        }

        boolean insertado;
        
        if (!this.usuariosBanco.contiene(usuario)) {
            usuario.setContrasena(seguridad.encode(usuario.getContrasena()));
            this.usuariosBanco.insertar(usuario);// al insertar si al añadir la cuenta da error lanza excepcion aqui
            Cuenta cuenta = cuentasBanco.crear(0, usuario);

            insertado = usuario.addCuenta(cuenta);
            this.usuariosBanco.actualizar(usuario);
        } else {
            throw new UsuarioIncorrecto();
        }

        return insertado;
    }

    @Override
    public boolean login(Usuario usuarioLogin) throws InvalidParameterException, IllegalAccessError {
        if (usuarioLogin == null) {
            throw new UsuarioIncorrecto();
        }

        if (usuarioLogin.getContrasena() == null) {
            throw new ContrasenaIncorrecta();
        }

        if (usuarioLogin.getContrasena().isBlank()) {
            throw new ContrasenaIncorrecta();
        }

        Usuario usuario = this.usuariosBanco.buscar(usuarioLogin.getDni());

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }
        
        if (!this.seguridad.matches(usuarioLogin.getContrasena(), usuario.getContrasena())) {
            throw new ContrasenaIncorrecta();
        }

        return true;
    }

    @Override
    public boolean crearCuenta(String id) throws IllegalAccessError {

        Usuario usuario = this.usuariosBanco.buscar(id);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        Cuenta cuenta = cuentasBanco.crear(0, usuario);

        return cuenta != null;
    }

    @Override
    public List<DTOCuenta> consultarCuentas(String id) throws IllegalAccessError {
        ArrayList<DTOCuenta> cuentasDTO = new ArrayList<>();

        List<Cuenta> cuentas = this.usuariosBanco.getCuentas(id);

        cuentas.forEach((Cuenta cuenta) -> {
            cuentasDTO.add(Mapper.dtoCuentaMapper(cuenta));
        });

        return cuentasDTO;
    }

    @Override
    public boolean borrarUsuario(String id) {
        usuariosBanco.borrar(id);
        return usuariosBanco.buscar(id) != null;

    }
}

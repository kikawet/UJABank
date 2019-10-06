/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTarjeta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Ingreso;
import es.ujaen.dae.ujabank.entidades.Retiro;
import es.ujaen.dae.ujabank.entidades.Tarjeta;
import es.ujaen.dae.ujabank.entidades.Transferencia;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import es.ujaen.dae.ujabank.interfaces.Transaccion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 *
 * @author axpos
 */
@Component
public class Banco implements ServiciosTransacciones, ServiciosUsuario {

    private final List<Usuario> _usuariosBanco;
    private final List<Cuenta> _cuentasBanco;
    private Map<UUID, Usuario> _tokensActivos;

    public Banco() {
        this._usuariosBanco = new ArrayList<>();
        this._cuentasBanco = new ArrayList<>();
    }

    @Override
    public boolean ingresar(UUID token, DTOTarjeta origen, DTOCuenta destino, int cantidad) throws NoSuchElementException, IllegalAccessError {

        if (!this._tokensActivos.containsKey(token)) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        Cuenta cuenta = Mapper.cuentaMapper(destino);
        Tarjeta tarjeta = Mapper.tarjetaMapper(origen);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new NoSuchElementException("No existe esa cuenta");
        }

        cuenta = this._cuentasBanco.get(posicionCuenta);

        tarjeta.retirar(cantidad);//deshacer transsacion si es necesario

//        cantidad = EuroUJACoinRate...
        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(new Date());
        ingreso.setCantidad(cantidad);
        ingreso.setOrigen(tarjeta);
        ingreso.setDestino(cuenta);

        boolean ingresado = cuenta.ingresar(ingreso);

        return ingresado;
    }

    @Override
    public boolean transferir(UUID token, DTOCuenta origen, DTOCuenta destino, int cantidad, String concepto) throws NoSuchElementException, IllegalAccessError {
        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        Cuenta cOrigen = Mapper.cuentaMapper(origen);
        Cuenta cDestino = Mapper.cuentaMapper(destino);

        int posicionCuentaOrigen = this._cuentasBanco.indexOf(cOrigen);
        int posicionCuentaDestino = this._cuentasBanco.indexOf(cDestino);

        if (posicionCuentaOrigen == -1) {
            throw new NoSuchElementException("No existe la cuenta de origen");
        }

        cOrigen = this._cuentasBanco.get(posicionCuentaOrigen);

        if (!usuario.containsCuenta(cOrigen)) {
            throw new IllegalAccessError("El usuario no tiene acceso a la cuenta de origen");
        }

        if (cantidad > cOrigen.getSaldo()) {
            throw new IllegalAccessError("La cuenta de origen no tiene dinero suficiente");
        }

        if (posicionCuentaDestino == -1) {
            throw new NoSuchElementException("La cuenta de destino no existe");
        }

        cDestino = this._cuentasBanco.get(posicionCuentaDestino);

//        cantidad = EuroUJACoinRate... // no es necesario entre cuentas
        Transferencia transferencia = new Transferencia();

        transferencia.setFecha(new Date());
        transferencia.setOrigen(cOrigen);
        transferencia.setDestino(cDestino);
        transferencia.setCantidad(cantidad);
        transferencia.setConcepto(concepto);

        boolean transferido = cOrigen.transferir(transferencia) && cDestino.transferir(transferencia); // hacer dos pasos si hace falta deshacer

        //deshacer si es necesario
        return transferido;
    }

    @Override
    public boolean retirar(UUID token, DTOCuenta origen, DTOTarjeta destino, int cantidad) throws NoSuchElementException, IllegalAccessError {
        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        Cuenta cuenta = Mapper.cuentaMapper(origen);
        Tarjeta tarjeta = Mapper.tarjetaMapper(destino);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new NoSuchElementException("No existe la cuenta de origen");
        }

        cuenta = this._cuentasBanco.get(posicionCuenta);

        if (!usuario.containsCuenta(cuenta)) {
            throw new IllegalAccessError("El usuario no tiene acceso a la cuenta de origen");
        }

        if (cantidad > cuenta.getSaldo()) {
            throw new IllegalAccessError("La cuenta de origen no tiene dinero suficiente");
        }

        Retiro retiro = new Retiro();

        retiro.setFecha(new Date());
        retiro.setOrigen(cuenta);
        retiro.setDestino(tarjeta);
        retiro.setCantidad(cantidad);

        boolean retirado = cuenta.retirar(retiro);

        //        cantidad = EuroUJACoinRate... 
        tarjeta.ingresar(cantidad);

        //si no se ha ingresado deshacer
        return retirado;
    }

    @Override
    public List<Transaccion> consultar(UUID token, DTOCuenta cuentaDTO, Date inicio, Date fin) throws NoSuchElementException, IllegalAccessError {
        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("El usuario necesita estar logeado");
        }

        Cuenta cuenta = Mapper.cuentaMapper(cuentaDTO);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new NoSuchElementException("No existe esa cuenta");
        }

        cuenta = this._cuentasBanco.get(posicionCuenta);

        if (!usuario.containsCuenta(cuenta)) {
            throw new IllegalAccessError("El usuario no tiene acceso a esa cuenta");
        }

        return cuenta.consultar(inicio, fin);
    }

    @Override
    public boolean registrar(DTOUsuario u, String contasena) {
        Usuario usuario = Mapper.usuarioMapper(u);
        usuario.setContrasena(contasena);
        boolean insertado = false;
        if (!this._usuariosBanco.contains(usuario)) {
            Cuenta cuenta = new Cuenta();
            usuario.addCuenta(cuenta);
            insertado = this._usuariosBanco.add(usuario) && this._cuentasBanco.add(cuenta);

            if (!insertado) {
                this._usuariosBanco.remove(usuario);
                this._cuentasBanco.remove(cuenta);
            }
        }

        return insertado;
    }

    @Override
    public UUID login(DTOUsuario usuario, String contrasena) throws NoSuchElementException, IllegalAccessError {
        int indiceUsuario = this._usuariosBanco.indexOf(Mapper.usuarioMapper(usuario));

        if (indiceUsuario == -1) {
            throw new NoSuchElementException("Ese usuario no está en el sistema");
        }

        if (this._usuariosBanco.get(indiceUsuario).getContrasena() == null ? contrasena == null : !this._usuariosBanco.get(indiceUsuario).getContrasena().equals(contrasena)) {
            throw new IllegalAccessError("Contraseña incorrecta");
        }

        UUID token = UUID.randomUUID();

        this._tokensActivos.put(token, this._usuariosBanco.get(indiceUsuario));

        return token;
    }

    @Override
    public DTOCuenta crearCuenta(UUID token) throws IllegalAccessError {
        if (!this._tokensActivos.containsKey(token)) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        Usuario usuario = this._tokensActivos.get(token);
        Cuenta cuenta = new Cuenta();

        boolean insertado = usuario.addCuenta(cuenta);

        if (insertado) {
            insertado = this._cuentasBanco.add(cuenta);

            if (!insertado) {
                usuario.removeCuenta(cuenta);
            }
        }

        return Mapper.dtoCuentaMapper(cuenta);
    }
}

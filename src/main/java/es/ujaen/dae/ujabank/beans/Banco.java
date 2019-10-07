/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import com.sun.tools.classfile.ConstantPool;
import es.dae.ujaen.euroujacoinrate.EuroUJACoinRate;
import es.ujaen.dae.ujabank.DTO.DTOCuenta;
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
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    private static final EuroUJACoinRate euro_UJACoin = new EuroUJACoinRate();

    public Banco() {
        this._usuariosBanco = new ArrayList<>();
        this._cuentasBanco = new ArrayList<>();
        this._tokensActivos = new TreeMap<>();
    }

    @Override
    public boolean ingresar(UUID token, Tarjeta origen, DTOCuenta destino, float cantidad) throws IllegalAccessError, InvalidParameterException {
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        if (origen == null) {
            throw new InvalidParameterException("La tarjeta no puede ser null");
        }

        if (destino == null) {
            throw new InvalidParameterException("La cuenta no puede ser null");
        }

        if (cantidad < 0) {
            throw new InvalidParameterException("La cantidad a ingresar no puede ser negativa");
        }

        Cuenta cuenta = Mapper.cuentaMapper(destino);
//        Tarjeta tarjeta = Mapper.tarjetaMapper(origen);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new InvalidParameterException("No existe esa cuenta");
        }

        cuenta = this._cuentasBanco.get(posicionCuenta);

        if (usuario.containsCuenta(cuenta)) {
            throw new InvalidParameterException("Esa cuenta no pertence a ese usuario");
        }

        origen.retirar(cantidad);//deshacer transsacion si es necesario

        cantidad *= euro_UJACoin.euroToUJACoinToday();

        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(new Date());
        ingreso.setCantidad(cantidad);
        ingreso.setOrigen(origen);
        ingreso.setDestino(cuenta);

        boolean ingresado = cuenta.ingresar(ingreso);

        return ingresado;
    }

    @Override
    public boolean transferir(UUID token, DTOCuenta origen, DTOCuenta destino, float cantidad, String concepto) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        if (origen == null) {
            throw new InvalidParameterException("La cuenta de origen no puede ser null");
        }

        if (destino == null) {
            throw new InvalidParameterException("La cuenta de destino no puede ser null");
        }

        if (cantidad < 0) {
            throw new InvalidParameterException("La cantidad debe ser positiva");
        }

        if (concepto == null) {
            throw new InvalidParameterException("El concepto no puede ser null");
        }

        Cuenta cOrigen = Mapper.cuentaMapper(origen);
        Cuenta cDestino = Mapper.cuentaMapper(destino);

        int posicionCuentaOrigen = this._cuentasBanco.indexOf(cOrigen);
        int posicionCuentaDestino = this._cuentasBanco.indexOf(cDestino);

        if (posicionCuentaOrigen == -1) {
            throw new InvalidParameterException("No existe la cuenta de origen");
        }

        cOrigen = this._cuentasBanco.get(posicionCuentaOrigen);

        if (!usuario.containsCuenta(cOrigen)) {
            throw new IllegalAccessError("El usuario no tiene acceso a la cuenta de origen");
        }

        if (cantidad > cOrigen.getSaldo()) {
            throw new IllegalAccessError("La cuenta de origen no tiene dinero suficiente");
        }

        if (posicionCuentaDestino == -1) {
            throw new InvalidParameterException("La cuenta de destino no existe");
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
    public boolean retirar(UUID token, DTOCuenta origen, Tarjeta destino, float cantidad) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        if (origen == null) {
            throw new InvalidParameterException("La cuenta no puede ser null");
        }

        if (destino == null) {
            throw new InvalidParameterException("La tarjeta no puede ser null");
        }

        if (cantidad < 0) {
            throw new InvalidParameterException("La cantidad debe ser positiva");
        }

        Cuenta cuenta = Mapper.cuentaMapper(origen);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new InvalidParameterException("No existe la cuenta de origen");
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
        retiro.setDestino(destino);
        retiro.setCantidad(cantidad);

        boolean retirado = cuenta.retirar(retiro);

        cantidad *= euro_UJACoin.ujaCoinToEuroToday();
        destino.ingresar(cantidad);

        //si no se ha ingresado deshacer
        return retirado;
    }

    @Override
    public List<Transaccion> consultar(UUID token, DTOCuenta cuentaDTO, Date inicio, Date fin) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        if (cuentaDTO == null) {
            throw new InvalidParameterException("La cuenta no puede ser null");
        }

        if (inicio == null) {
            throw new InvalidParameterException("La fecha de inicio no puede ser null");
        }

        if (fin == null) {
            throw new InvalidParameterException("La fecha de fin no puede ser null");
        }

        if (fin.before(inicio)) {
            throw new InvalidParameterException("La fecha de fin debe de ser posterior a la de inicio");
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("El usuario necesita estar logeado");
        }

        Cuenta cuenta = Mapper.cuentaMapper(cuentaDTO);

        int posicionCuenta = this._cuentasBanco.indexOf(cuenta);

        if (posicionCuenta == -1) {
            throw new InvalidParameterException("No existe esa cuenta");
        }

        cuenta = this._cuentasBanco.get(posicionCuenta);

        if (!usuario.containsCuenta(cuenta)) {
            throw new InvalidParameterException("El usuario no tiene acceso a esa cuenta");
        }

        return cuenta.consultar(inicio, fin);
    }

    @Override
    public boolean registrar(DTOUsuario u, String contasena) throws InvalidParameterException {
        if (u == null) {
            throw new InvalidParameterException("El usuario no puede ser null");
        }

        if (contasena == null) {
            throw new InvalidParameterException("La contraseña no puede ser null");
        }

        if (contasena.isBlank()) {
            throw new InvalidParameterException("La contraseña no puede estar vacia");
        }

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
        } else {
            throw new InvalidParameterException("Ese usuario ya está registrado");
        }

        return insertado;
    }

    @Override
    public UUID login(DTOUsuario usuario, String contrasena) throws InvalidParameterException, IllegalAccessError {
        if (usuario == null) {
            throw new InvalidParameterException("El usuario no puede ser null");
        }

        if (contrasena == null) {
            throw new InvalidParameterException("La contraseña no puede ser null");
        }

        if (contrasena.isBlank()) {
            throw new InvalidParameterException("La contraseña no puede estar vacia");
        }

        int indiceUsuario = this._usuariosBanco.indexOf(Mapper.usuarioMapper(usuario));

        if (indiceUsuario == -1) {
            throw new InvalidParameterException("Ese usuario no está en el sistema");
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
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        Cuenta cuenta = new Cuenta();

        boolean insertado = usuario.addCuenta(cuenta);

        if (insertado) {
            this._cuentasBanco.add(cuenta);

//            if (!insertado) { //No hace falta realmente
//                usuario.removeCuenta(cuenta);
//            }
        }

        return Mapper.dtoCuentaMapper(cuenta);
    }

    @Override
    public List<DTOCuenta> consultarCuentas(UUID token) throws IllegalAccessError {
        if (token == null) {
            throw new InvalidParameterException("La token no puede ser null");
        }

        if (!this._tokensActivos.containsKey(token)) {
            throw new IllegalAccessError("Usuario no logeado");
        }

        ArrayList<DTOCuenta> cuentas = new ArrayList<>();

        this._tokensActivos.get(token).getCuentas().forEach((Cuenta cuenta) -> {
            cuentas.add(Mapper.dtoCuentaMapper(cuenta));
        });

        return cuentas;
    }
}

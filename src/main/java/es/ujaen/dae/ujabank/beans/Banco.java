/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import es.ujaen.dae.ujabank.excepciones.formato.UsuarioIncorrecto;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import es.ujaen.dae.ujabank.excepciones.formato.TarjetaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.FechaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.CuentaIncorrecta;
import es.ujaen.dae.ujabank.excepciones.formato.ConceptoIncorrecto;
import es.ujaen.dae.ujabank.excepciones.formato.CantidadNegativa;
import es.dae.ujaen.euroujacoinrate.EuroUJACoinRate;
import es.ujaen.dae.ujabank.DAO.DAOCuenta;
import es.ujaen.dae.ujabank.DAO.DAOGenerico;
import es.ujaen.dae.ujabank.DAO.DAOUsuario;
import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Ingreso;
import es.ujaen.dae.ujabank.entidades.Retiro;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.entidades.Transferencia;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import es.ujaen.dae.ujabank.entidades.Transaccion;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.stereotype.Component;
import es.ujaen.dae.ujabank.excepciones.*;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author axpos
 */
@Component
public class Banco implements ServiciosTransacciones, ServiciosUsuario {

//    private final List<Usuario> _usuariosBanco;
    @Autowired
    private DAOUsuario usuariosBanco;
//    private final Map<Integer, Cuenta> _cuentasBanco;

    @Autowired
    private DAOCuenta cuentasBanco;
    private final Map<UUID, Usuario> _tokensActivos;

    private static final EuroUJACoinRate euro_UJACoin = new EuroUJACoinRate();

    public Banco() {
//        this._usuariosBanco = new ArrayList<>();
        this._tokensActivos = new TreeMap<>();
    }

    @Override
    public boolean ingresar(UUID token, Tarjeta origen, int idDestino, float cantidad) throws IllegalAccessError, InvalidParameterException {
        if (token == null) {
            throw new TokenIncorrecto();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioIncorrecto();
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

//        Tarjeta tarjeta = Mapper.tarjetaMapper(origen);
//        if (cuenta == null) {
//            throw new CuentaIncorrecta();
//        }
        if (!usuario.equals(cuenta.getPropietario())) {
            throw new CuentaNoPerteneceUsuario();
        }

        origen.retirar(cantidad);//deshacer transsacion si es necesario

        cantidad *= euro_UJACoin.euroToUJACoinToday();

        return this.cuentasBanco.ingresar(origen, cuenta, cantidad) != null;
    }

    @Override
    public boolean transferir(UUID token, int idOrigen, int idDestino, float cantidad, String concepto) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenIncorrecto();
        }

        Usuario usuario = this._tokensActivos.get(token);

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
            throw new ErrorAutorizacion();
        }

        if (cantidad > cOrigen.getSaldo()) {
            throw new CuentaSaldoInsuficiente();
        }

        if (cDestino == null) {
            throw new CuentaIncorrecta();
        }

//        cantidad = EuroUJACoinRate... // no es necesario entre cuentas
        //deshacer si es necesario
        return this.cuentasBanco.transferir(cOrigen, cDestino, cantidad, concepto) != null;
    }

    @Override
    public boolean retirar(UUID token, int idOrigen, Tarjeta destino, float cantidad) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenIncorrecto();
        }

        Usuario usuario = this._tokensActivos.get(token);

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
            throw new ErrorAutorizacion();
        }

        if (cantidad > cuenta.getSaldo()) {
            throw new CuentaSaldoInsuficiente();
        }

        boolean retiro = this.cuentasBanco.retirar(cuenta, destino, cantidad) != null;

        cantidad *= euro_UJACoin.ujaCoinToEuroToday();
        destino.ingresar(cantidad);

        //si no se ha ingresado deshacer
        return retiro;
    }

    @Override
    public List<DTOTransaccion> consultar(UUID token, int idCuenta, Date inicio, Date fin) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenIncorrecto();
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

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        Cuenta cuenta = this.cuentasBanco.buscar(idCuenta);

        if (cuenta == null) {
            throw new CuentaIncorrecta();
        }

        if (!usuario.equals(cuenta.getPropietario())) {
            throw new ErrorAutorizacion();
        }

        List<Transaccion> transacciones = this.cuentasBanco.consultarTransacciones(cuenta, inicio, fin);
        List<DTOTransaccion> dtoTransacciones = new ArrayList<>();
        transacciones.forEach((transaccion) -> {
            dtoTransacciones.add(Mapper.dtoTransaccionMapper(transaccion));
        });
        return dtoTransacciones;
    }

    @Override
    public void registrar(DTOUsuario u, String contasena) throws InvalidParameterException {
        if (u == null) {
            throw new UsuarioIncorrecto();
        }

        if (contasena == null) {
            throw new ContrasenaIncorrecta();
        }

        if (contasena.isBlank()) {
            throw new ContrasenaIncorrecta();
        }

        Usuario usuario = Mapper.usuarioMapper(u);
        usuario.setContrasena(contasena);

        if (!this.usuariosBanco.contiene(usuario)) {
            this.usuariosBanco.insertar(usuario);// al insertar si al añadir la cuenta da error lanza excepcion aqui
            Cuenta cuenta = cuentasBanco.crear(0, usuario);

            usuario.addCuenta(cuenta);
            this.usuariosBanco.actualizar(usuario);
        } else {
            throw new UsuarioIncorrecto();
        }

    }

    @Override
    public UUID login(DTOUsuario usuarioDTO, String contrasena) throws InvalidParameterException, IllegalAccessError {
        if (usuarioDTO == null) {
            throw new UsuarioIncorrecto();
        }

        if (contrasena == null) {
            throw new ContrasenaIncorrecta();
        }

        if (contrasena.isBlank()) {
            throw new ContrasenaIncorrecta();
        }

//        int indiceUsuario = this._usuariosBanco.indexOf(Mapper.usuarioMapper(usuario));
//
        Usuario usuario = this.usuariosBanco.buscar(usuarioDTO.getDni());

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }
        if (!usuario.getContrasena().equals(contrasena)) {
            throw new ContrasenaIncorrecta();
        }

        UUID token = UUID.randomUUID();

        this._tokensActivos.put(token, usuario);

        return token;
    }

    @Override
    public boolean crearCuenta(UUID token) throws IllegalAccessError {
        if (token == null) {
            throw new TokenIncorrecto();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new ErrorAutorizacion();
        }

        Cuenta cuenta = cuentasBanco.crear(0, usuario);

//        boolean insertado = usuario.addCuenta(cuenta);
//        usuariosBanco.actualizar(usuario);
        return cuenta != null;
    }

    @Override
    public List<DTOCuenta> consultarCuentas(UUID token) throws IllegalAccessError {
        if (token == null) {
            throw new TokenIncorrecto();
        }

        if (!this._tokensActivos.containsKey(token)) {
            throw new ErrorAutorizacion();
        }

        ArrayList<DTOCuenta> cuentasDTO = new ArrayList<>();

        List<Cuenta> cuentas = this.usuariosBanco.getCuentas(this._tokensActivos.get(token));//this._tokensActivos.get(token).getCuentas();

        cuentas.forEach((Cuenta cuenta) -> {
            cuentasDTO.add(Mapper.dtoCuentaMapper(cuenta));
        });

        return cuentasDTO;
    }
}

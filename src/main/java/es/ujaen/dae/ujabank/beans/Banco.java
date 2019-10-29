/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.beans;

import es.dae.ujaen.euroujacoinrate.EuroUJACoinRate;
import es.ujaen.dae.ujabank.DTO.DTOCuenta;
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
import es.ujaen.dae.ujabank.interfaces.Transaccion;
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
/**
 *
 * @author axpos
 */
@Component
public class Banco implements ServiciosTransacciones, ServiciosUsuario {

    private final List<Usuario> _usuariosBanco;
    private final Map<Integer, Cuenta> _cuentasBanco;
    private Map<UUID, Usuario> _tokensActivos;

    private static final EuroUJACoinRate euro_UJACoin = new EuroUJACoinRate();

    public Banco() {
        this._usuariosBanco = new ArrayList<>();
        this._cuentasBanco = new HashMap<>();
        this._tokensActivos = new TreeMap<>();
    }

    @Override
    public boolean ingresar(UUID token, Tarjeta origen, int idDestino, float cantidad) throws IllegalAccessError, InvalidParameterException {
        if (token == null) {
            throw new TokenNulo();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioNulo();
        }

        if (origen == null) {
            throw new TarjetaNula();
        }

        if (idDestino < 0) {
            throw new CuentaNula();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        Cuenta cuenta = this._cuentasBanco.get(idDestino);

//        Tarjeta tarjeta = Mapper.tarjetaMapper(origen);
        if (cuenta == null) {
            throw new NoExisteCuenta();
        }

        if (!usuario.containsCuenta(cuenta)) {
            throw new CuentaNoPerteneceUsuario();
        }

        origen.retirar(cantidad);//deshacer transsacion si es necesario

        cantidad *= euro_UJACoin.euroToUJACoinToday();

        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(new Date());
        ingreso.setCantidad(cantidad);
        ingreso.setOrigen(origen.getNumero());
        ingreso.setIDDestino(cuenta.getId());

        boolean ingresado = cuenta.ingresar(ingreso);

        return ingresado;
    }

    @Override
    public boolean transferir(UUID token, int idOrigen, int idDestino, float cantidad, String concepto) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenNulo();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioNoLogeado();
        }

        if (idOrigen < 0) {
            throw new CuentaNula();
        }

        if (idDestino == 0) {
            throw new CuentaDestinoNula();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        if (concepto == null) {
            throw new ConceptoNulo();
        }

        Cuenta cOrigen = this._cuentasBanco.get(idOrigen);
        Cuenta cDestino = this._cuentasBanco.get(idDestino);

        if (cOrigen == null) {
            throw new NoExisteCuentaOrigen();
        }

        if (!usuario.containsCuenta(cOrigen)) {
            throw new UsuarioSinAccesoCuentaOrigen();
        }

        if (cantidad > cOrigen.getSaldo()) {
            throw new CuentaSinDineroSuficiente();
        }

        if (cDestino == null) {
            throw new NoExisteCuentaDestino();
        }

//        cantidad = EuroUJACoinRate... // no es necesario entre cuentas
        Transferencia transferencia = new Transferencia();

        transferencia.setFecha(new Date());
        transferencia.setIDOrigen(cOrigen.getId());
        transferencia.setIDDestino(cDestino.getId());
        transferencia.setCantidad(cantidad);
        transferencia.setConcepto(concepto);

        boolean transferido = cOrigen.transferir(transferencia) && cDestino.transferir(transferencia); // hacer dos pasos si hace falta deshacer

        //deshacer si es necesario
        return transferido;
    }

    @Override
    public boolean retirar(UUID token, int idOrigen, Tarjeta destino, float cantidad) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenNulo();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioNoLogeado();
        }

        if (idOrigen < 0) {
            throw new CuentaNula();
        }

        if (destino == null) {
            throw new TarjetaNula();
        }

        if (cantidad < 0) {
            throw new CantidadNegativa();
        }

        Cuenta cuenta = this._cuentasBanco.get(idOrigen);

        if (cuenta == null) {
            throw new NoExisteCuentaOrigen();
        }

        if (!usuario.containsCuenta(cuenta)) {
            throw new UsuarioSinAccesoCuentaOrigen();
        }

        if (cantidad > cuenta.getSaldo()) {
            throw new CuentaSinDineroSuficiente();
        }

        Retiro retiro = new Retiro();

        retiro.setFecha(new Date());
        retiro.setIDOrigen(cuenta.getId());
        retiro.setDestino(destino.getNumero());
        retiro.setCantidad(cantidad);

        boolean retirado = cuenta.retirar(retiro);

        cantidad *= euro_UJACoin.ujaCoinToEuroToday();
        destino.ingresar(cantidad);

        //si no se ha ingresado deshacer
        return retirado;
    }

    @Override
    public List<Transaccion> consultar(UUID token, int idCuenta, Date inicio, Date fin) throws InvalidParameterException, IllegalAccessError {
        if (token == null) {
            throw new TokenNulo();
        }

        if (idCuenta < 0) {
            throw new CuentaNula();
        }

        if (inicio == null) {
            throw new FechaInicioNula();
        }

        if (fin == null) {
            throw new FechaFinNula();
        }

        if (fin.before(inicio)) {
            throw new FechaFinAnteriorAInicio();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioNoLogeado();
        }

        Cuenta cuenta = this._cuentasBanco.get(idCuenta);

        if (cuenta == null) {
            throw new NoExisteCuenta();
        }

        if (!usuario.containsCuenta(cuenta)) {
            throw new UsuaroSinAccesoCuenta();
        }

        return cuenta.consultar(inicio, fin);
    }

    @Override
    public boolean registrar(DTOUsuario u, String contasena) throws InvalidParameterException {
        if (u == null) {
            throw new UsuarioNulo();
        }

        if (contasena == null) {
            throw new ContraseñaNula();
        }

        if (contasena.isBlank()) {
            throw new ContraseñaVacía();
        }

        Usuario usuario = Mapper.usuarioMapper(u);
        usuario.setContrasena(contasena);
        boolean insertado = false;
        if (!this._usuariosBanco.contains(usuario)) {
            Cuenta cuenta = new Cuenta();
            usuario.addCuenta(cuenta);
//            insertado = this._usuariosBanco.add(usuario) && this._cuentasBanco.add(idCuenta);
//
//            if (!insertado) {
//                this._usuariosBanco.remove(usuario);
//                this._cuentasBanco.remove(cuenta);
//            }
        } else {
            throw new UsuarioYaRegistrado();
        }

        return insertado;
    }

    @Override
    public UUID login(DTOUsuario usuario, String contrasena) throws InvalidParameterException, IllegalAccessError {
        if (usuario == null) {
            throw new UsuarioNulo();
        }

        if (contrasena == null) {
            throw new ContraseñaNula();
        }

        if (contrasena.isBlank()) {
            throw new ContraseñaVacía();
        }

        int indiceUsuario = this._usuariosBanco.indexOf(Mapper.usuarioMapper(usuario));

        if (indiceUsuario == -1) {
            throw new UsuarioNoRegistrado();
        }

        if (this._usuariosBanco.get(indiceUsuario).getContrasena() == null ? contrasena == null : !this._usuariosBanco.get(indiceUsuario).getContrasena().equals(contrasena)) {
            throw new ContrasenaIncorrecta();
        }

        UUID token = UUID.randomUUID();

        this._tokensActivos.put(token, this._usuariosBanco.get(indiceUsuario));

        return token;
    }

    @Override
    public boolean crearCuenta(UUID token) throws IllegalAccessError {
        if (token == null) {
            throw new TokenNulo();
        }

        Usuario usuario = this._tokensActivos.get(token);

        if (usuario == null) {
            throw new UsuarioNoLogeado();
        }

        Cuenta cuenta = new Cuenta();

//        boolean insertado = 
        usuario.addCuenta(cuenta);

//        if (insertado) {
//            this._cuentasBanco.add(idCuenta);
//
//            if (!insertado) { //No hace falta realmente
//                usuario.removeCuenta(cuenta);
//            }
//        }
        return this._cuentasBanco.put(cuenta.getId(), cuenta) != null;
    }

    @Override
    public List<DTOCuenta> consultarCuentas(UUID token) throws IllegalAccessError {
        if (token == null) {
            throw new TokenNulo();
        }

        if (!this._tokensActivos.containsKey(token)) {
            throw new UsuarioNoLogeado();
        }

        ArrayList<DTOCuenta> cuentas = new ArrayList<>();

        this._tokensActivos.get(token).getCuentas().forEach((Cuenta cuenta) -> {
            cuentas.add(Mapper.dtoCuentaMapper(cuenta));
        });

        return cuentas;
    }
}

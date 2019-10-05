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
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import es.ujaen.dae.ujabank.interfaces.Transaccion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 *
 * @author axpos
 */
@Component
public class Banco implements ServiciosTransacciones, ServiciosUsuario {

    private List<Usuario> usuariosBanco;
    private List<Cuenta> cuentasBanco;
    private Map<UUID, Usuario> tokensActivos;

    public Banco() {
        this.usuariosBanco = new ArrayList<>();
        this.cuentasBanco = new ArrayList<>();
    }

    @Override
    public boolean ingresar(UUID token, DTOTarjeta origen, DTOCuenta destino, int cantidad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean transferir(UUID token, DTOCuenta origen, DTOCuenta destino, int cantidad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retirar(UUID token, DTOCuenta origen, DTOTarjeta destino, int cantidad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transaccion> consultar(UUID token, DTOCuenta cuenta, Date inicio, Date fin) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean registrar(DTOUsuario u, String contasena) {
        Usuario usuario = Mapper.usuarioMapper(u);
        usuario.setContrasena(contasena);
        boolean insertado = false;
        if (!this.usuariosBanco.contains(usuario)) {
            Cuenta cuenta = new Cuenta();
            usuario.addCuenta(cuenta);
            insertado = this.usuariosBanco.add(usuario) && this.cuentasBanco.add(cuenta);

            if (!insertado) {
                this.usuariosBanco.remove(usuario);
                this.cuentasBanco.remove(cuenta);
            }
        }

        return insertado;
    }

    @Override
    public UUID login(DTOUsuario usuario, String contrasena) throws NoSuchElementException, IllegalAccessError {
        int indiceUsuario = this.usuariosBanco.indexOf(Mapper.usuarioMapper(usuario));

        if (indiceUsuario == -1) {
            throw new NoSuchElementException("Ese usuario no está en el sistema");
        }

        if (this.usuariosBanco.get(indiceUsuario).getContrasena() == null ? contrasena == null : !this.usuariosBanco.get(indiceUsuario).getContrasena().equals(contrasena)) {
            throw new IllegalAccessError("Contraseña incorrecta");
        }

        UUID token = UUID.randomUUID();

        this.tokensActivos.put(token, this.usuariosBanco.get(indiceUsuario));

        return token;
    }

    @Override
    public boolean crearCuenta(UUID token) throws IllegalAccessError{
        if(!this.tokensActivos.containsKey(token))
            throw new IllegalAccessError("Token no existente");
        
        Usuario usuario = this.tokensActivos.get(token);
        Cuenta cuenta = new Cuenta();
        
        boolean insertado = usuario.addCuenta(cuenta);
        
        if (insertado){
            insertado = this.cuentasBanco.add(cuenta);
            
            if(!insertado)
                usuario.removeCuenta(cuenta);
        }
        
        return insertado;
    }
}

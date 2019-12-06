/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping("/usuario")
public class RESTUsuario {// implements ServiciosUsuario{

    @Autowired
    private Banco ujabank;

    private final Map<UUID, Usuario> _tokensActivos;

    public RESTUsuario() {
//        this._tokensActivos = _tokensActivos;
        this._tokensActivos = new TreeMap<>();
    }

    @GetMapping("/test")
    public ResponseEntity comprobar() {
        return ResponseEntity.ok("API funciona correctamente (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

//    @Override
    @PostMapping
    public ResponseEntity registrar(@RequestBody(required = true) DTOUsuario usuario) {
        ujabank.registrar(Mapper.usuarioMapper(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/cc")
    public ResponseEntity crearCuenta(@PathVariable("id") String dni, @RequestParam(defaultValue = "") String token) {
        try {
            ujabank.crearCuenta(dni);
        } catch (IllegalArgumentException e) {
            throw new TokenIncorrecto();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //@Async//Para que funcione hace falta que este método esté en un servicio pero lo dejo igual
    private void limpiarToken(Usuario usuario, UUID token) {

        this._tokensActivos.entrySet().removeIf((entry) -> {
            return usuario.equals(entry.getValue()) && !token.equals(entry.getKey());
        });
    }

//    @Overrider
    @GetMapping//(consumes = MediaType.APPLICATION_JSON_VALUE, poduces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity login(@RequestBody(required = true) DTOUsuario usuarioDTO) {
        Usuario usuario = Mapper.usuarioMapper(usuarioDTO);
        boolean logeable = ujabank.login(usuario);

        UUID token = null;

        if (logeable) {
            token = UUID.randomUUID();
            this._tokensActivos.put(token, usuario);
            limpiarToken(usuario, token);
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}/logout")//como alternativa poner un put 
    public ResponseEntity logout(@RequestParam(defaultValue = "") String token) {
        try {
            //ujabank.logout(UUID.fromString(token));
            this._tokensActivos.remove(UUID.fromString(token));
        } catch (IllegalArgumentException e) {
            throw new TokenIncorrecto();
        }

        return ResponseEntity.ok().build();
    }

//    @Override
    @GetMapping(value = "/{id}/cuentas")
    public ResponseEntity consultarCuentas(@PathVariable("id") String dni, @RequestParam(defaultValue = "") String token) {
        List<?> cuentas;
        try {
            cuentas = ujabank.consultarCuentas(dni);
        } catch (IllegalArgumentException e) {
            throw new TokenIncorrecto();
        }
        return ResponseEntity.ok(cuentas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity borrar(@PathVariable("id") String dni) {
        try {
            ujabank.borrarUsuario(dni);
        } catch (IllegalArgumentException e) {
            throw new TokenIncorrecto();
        }

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({TokenIncorrecto.class})
    public void handlerToken() {

    }

}

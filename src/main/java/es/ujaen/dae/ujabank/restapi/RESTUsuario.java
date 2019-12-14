/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.anotaciones.Login;
import es.ujaen.dae.ujabank.anotaciones.Logout;
import es.ujaen.dae.ujabank.anotaciones.ValidarToken;
import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping(RESTUsuario.URI_MAPPING)
@Validated
public class RESTUsuario {// implements ServiciosUsuario{

    @Autowired
    private Banco ujabank;

    public static final String URI_MAPPING = "/usuario";

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
    public ResponseEntity crearCuenta(@PathVariable("id") String dni) {
        ujabank.crearCuenta(dni);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*
    @GetMapping
    @Login
    public ResponseEntity login(@RequestBody(required = true) DTOUsuario usuarioDTO) {
        Usuario usuario = Mapper.usuarioMapper(usuarioDTO);
        boolean logeable = ujabank.login(usuario);

        UUID token = null;

        if (logeable) {
            token = UUID.randomUUID();
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}/logout")//como alternativa poner un put 
    @Logout
    public ResponseEntity logout(@ValidarToken @RequestParam String token) {
        //El aop ya lo elimina y solo se entra si el token existe

        return ResponseEntity.ok().build();
    }
    */

//    @Override
    @GetMapping(value = "/{id}/cuentas")
    public ResponseEntity consultarCuentas(@PathVariable("id") String dni) {
        List<?> cuentas = ujabank.consultarCuentas(dni);

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
}

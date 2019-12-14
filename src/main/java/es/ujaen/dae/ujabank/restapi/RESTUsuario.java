/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping(RESTUsuario.URI_MAPPING)
@Validated
public class RESTUsuario {// implements ServiciosUsuario{

    public static final String URI_MAPPING = "/usuario";
    
    @Autowired
    private Banco ujabank;

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

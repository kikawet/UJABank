/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping("/cuenta")
public class RESTCuenta {

    @Autowired
    private Banco ujabank;

    @GetMapping("/test")
    public ResponseEntity comprobar() {
        return ResponseEntity.ok("API funciona correctamente  (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

    @PutMapping(value = "/{origen}")
    public ResponseEntity ingresar(String token, int origen, int idDestino, float cantidad){
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{origen}")
    public ResponseEntity transferir(String token, int idOrigen, int idDestino, float cantidad, String concepto){
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{origen}")
    public ResponseEntity retirar(String token, int idOrigen, int destino, float cantidad){
        throw new UnsupportedOperationException();
    }

    public ResponseEntity consultar(String token, int idCuenta, Date inicio, Date fin){
     throw new UnsupportedOperationException();
    }

}

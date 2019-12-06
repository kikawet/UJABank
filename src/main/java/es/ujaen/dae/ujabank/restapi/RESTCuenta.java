/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.anotaciones.ValidarToken;
import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.excepciones.formato.FechaIncorrecta;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping(RESTUsuario.URI_MAPPING + "/{id}/cuenta")
@Validated
public class RESTCuenta {

    @Autowired
    private Banco ujabank;

    @GetMapping("/test")
    public ResponseEntity comprobar() {
        return ResponseEntity.ok("API funciona correctamente (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

    @PutMapping(value = "/{origen}/ingresar")
    public ResponseEntity ingresar(@ValidarToken @RequestParam String token,
            @PathVariable String id, @RequestBody Tarjeta origen,
            @PathVariable("origen") int destino,
            @RequestParam @Min(0) float cantidad) {

        ujabank.ingresar(id, origen, destino, cantidad);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{origen}/transferir")
    public ResponseEntity transferir(@ValidarToken @RequestParam String token,
            @PathVariable String id, @PathVariable @Min(0) int origen,
            @RequestParam @Min(0) int destino,
            @RequestParam @Min(0) float cantidad,
            @RequestParam(required = false, defaultValue = "") String concepto) {

        ujabank.transferir(id, origen, destino, cantidad, concepto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{origen}/retirar")
    public ResponseEntity retirar(@ValidarToken @RequestParam String token,
            @PathVariable String id, @PathVariable @Min(0) int origen,
            @RequestBody Tarjeta destino, @RequestParam float cantidad) {

        ujabank.retirar(id, origen, destino, cantidad);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idCuenta}")
    public ResponseEntity consultar(@ValidarToken @RequestParam String token,
            @PathVariable String id, @PathVariable @Min(0) int idCuenta,
            @RequestParam(value = "finicio", required = false) String sinicio,
            @RequestParam(value = "ffin", required = false) String sfin)
            throws InterruptedException, ExecutionException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");//No se si es por el locale pero lo lee mal

        Date inicio, fin;

        try {
            if (sinicio == null) {
                Calendar finicio = Calendar.getInstance();
                finicio.set(Calendar.DAY_OF_MONTH, 1);
                inicio = finicio.getTime();
            } else {
                inicio = sdf.parse(sinicio);
            }

            if (sfin == null) {
                Calendar ffin = Calendar.getInstance();
                ffin.add(Calendar.DAY_OF_MONTH, 1);//Para incluir hoy también
                fin = ffin.getTime();
            } else {
                fin = sdf.parse(sfin);
            }

        } catch (ParseException ex) {
            throw new FechaIncorrecta();
        }

        CompletableFuture<List<DTOTransaccion>> transacciones;
        transacciones = ujabank.consultar(id, idCuenta, inicio, fin);

        List<DTOTransaccion> finala = transacciones.get();
        return ResponseEntity.ok(finala);//Encontré una manera de hacerlo pero se usaba webflux
    }

}
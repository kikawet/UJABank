/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank;

import es.ujaen.dae.ujabank.excepciones.formato.FechaIncorrecta;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author lopez
 */
@SpringBootApplication(scanBasePackages = {
    "es.ujaen.dae.ujabank.beans",
    "es.ujaen.dae.ujabank.DAO",
    "es.ujaen.dae.ujabank.restapi", 
//    "es.ujaen.dae.ujabank.validators",//Para validar con token no se usa con spring security
    "es.ujaen.dae.ujabank.config",
    "es.ujaen.dae.ujabank.servicios"
})
@EntityScan(basePackages = "es.ujaen.dae.ujabank.entidades")
@EnableCaching
@EnableAsync(proxyTargetClass = true)//marcar esto a true es importante
@ControllerAdvice
public class UJABank {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication servidor = new SpringApplication(UJABank.class);
        ApplicationContext contexto = servidor.run(args);
        System.out.println("Servidor iniciado");
    }

    //Excepción que se lanza cuando el token no es válido
    @ExceptionHandler({ConstraintViolationException.class, FechaIncorrecta.class})
    public void handlerBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InterruptedException.class, ExecutionException.class})
    public void handlerConcurrencia() {
    }
}

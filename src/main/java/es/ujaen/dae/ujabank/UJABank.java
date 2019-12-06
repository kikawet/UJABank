/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author lopez
 */
@SpringBootApplication(scanBasePackages = {"es.ujaen.dae.ujabank.beans", "es.ujaen.dae.ujabank.DAO", "es.ujaen.dae.ujabank.restapi", "es.ujaen.dae.ujabank.validators"})
@EntityScan(basePackages = "es.ujaen.dae.ujabank.entidades")
@EnableCaching
//@EnableAsync
@ControllerAdvice
public class UJABank {

    public static void main(String[] args) throws Exception {
        SpringApplication servidor = new SpringApplication(UJABank.class);
        ApplicationContext contexto = servidor.run(args);
        System.out.println("Servidor iniciado");

//        String [] beans;
//        beans = contexto.getBeanDefinitionNames();
//        
//        for (String bean : beans) {
//            System.out.println(bean);
//        }
    }

    //Excepción que se lanza cuando el token no es válido
    @ExceptionHandler({ConstraintViolationException.class})
    public void handlerToken(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

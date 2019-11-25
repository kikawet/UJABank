/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author lopez
 */
@SpringBootApplication(scanBasePackages = {"es.ujaen.dae.ujabank.beans", "es.ujaen.dae.ujabank.DAO"})
@EntityScan(basePackages = "es.ujaen.dae.ujabank.entidades")
@EnableCaching
public class UJABank {

    public static void main(String[] args) throws Exception {
        SpringApplication servidor = new SpringApplication(UJABank.class);
        ApplicationContext contexto = servidor.run(args);

        System.out.println("Servidor iniciado");
    }
}

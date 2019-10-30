/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank;

import es.ujaen.dae.ujabank.cliente.Cliente;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author lopez
 */
@SpringBootApplication(scanBasePackages = "es.ujaen.dae.ujabank.beans")
public class UJABank {

    public static void main(String[] args) throws Exception {
        SpringApplication servidor = new SpringApplication(UJABank.class);
        ApplicationContext contexto = servidor.run(args);

        System.out.println("Servidor iniciado");

        Cliente c = new Cliente();
        c.run(contexto);

    }
}

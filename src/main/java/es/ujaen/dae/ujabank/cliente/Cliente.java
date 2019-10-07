/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.cliente;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTarjeta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author axpos
 */
public class Cliente {

    private void borrarConsola() {

        for (int i = 0; i < 20; ++i) {
            System.out.println("\n");
        }

//        final String os = System.getProperty("os.name");
//
//        if (os.contains("Windows"))
//        {
//            Runtime.getRuntime().exec("cls");
//        }
//        else
//        {
//            Runtime.getRuntime().exec("clear");
//        }
    }

    public void run(ApplicationContext contexto) {
        ServiciosUsuario sUsuarios = (ServiciosUsuario) contexto.getBean("banco");
        ServiciosTransacciones sTrans = (ServiciosTransacciones) contexto.getBean("banco");

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int opcion = 0, i;

        DTOUsuario usuario = new DTOUsuario();
        List<DTOCuenta> cuentas = new ArrayList<>();
        List<DTOTarjeta> tarjetas = new ArrayList<>();

        borrarConsola();
        while (opcion != -1) {
            try {
                //Menu
                borrarConsola();
                i = 0;
                System.out.println("Cual de estas opciones quieres realizar:");
                System.out.println(++i + ": Modificar datos de usuario");
                System.out.println(++i + ": Mostrar tus cuentas");
                System.out.println(++i + ": Registrar usuario");
                System.out.println(++i + ": Login de usuario");
                System.out.println(++i + ": Crear una nueva cuenta");
                System.out.println(++i + ": ingresar dinero");
                System.out.println(++i + ": transferir dinero");
                System.out.println(++i + ": retirar dinero");
                System.out.println(++i + ": consultar movimientos");
                System.out.println("otro: salir");

                opcion = Integer.parseInt(input.readLine());

                borrarConsola();
                switch (opcion) {
                    case 1: //Modificar datos
                        String dato;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        sdf.setLenient(false);

                        System.out.println("Modificando el usuario, tus datos actuales son:");
                        System.out.println("-Nombre: " + usuario.getNombre());
                        System.out.println("-DNI: " + usuario.getDni());
                        System.out.println("-Domicilio: " + usuario.getDomicilio());
                        System.out.println("-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
                        System.out.println("-Telefono: " + usuario.getTelefono());
                        System.out.println("-Email: " + usuario.getEmail());

                        System.out.println("\n--------------------------------------\n");
                        System.out.println("Introduce tus nuevos datos: ");
                        //input.nextLine();//limpiar buffer

                        System.out.print("-Nombre: ");
                        dato = input.readLine();
                        usuario.setNombre(dato);

                        System.out.print("-DNI: ");
                        dato = input.readLine();
                        usuario.setDni(dato);

                        System.out.print("-Domicilio: ");
                        dato = input.readLine();
                        usuario.setDomicilio(dato);

                        System.out.print("-Fecha de nacimiento (dd/mm/yyyy): ");
                        dato = input.readLine();
                        usuario.setfNacimiento(sdf.parse(dato));

                        System.out.print("-Telefono: ");
                        dato = input.readLine();
                        usuario.setTelefono(dato);

                        System.out.print("-Email: ");
                        dato = input.readLine();
                        usuario.setEmail(dato);

                        System.out.println("\n--------------------------------------\n");
                        System.out.println("Tus nuevos datos (pulsa enter para voler): ");

                        System.out.println("\t-Nombre: " + usuario.getNombre());
                        System.out.println("\t-DNI: " + usuario.getDni());
                        System.out.println("\t-Domicilio: " + usuario.getDomicilio());
                        System.out.println("\t-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
                        System.out.println("\t-Telefono: " + usuario.getTelefono());
                        System.out.println("\t-Email: " + usuario.getEmail());
//                        System.out.flush();
                        break;
                    case 2: // Mostrar tus cuentas
                        System.out.println("\n--------------------------------------\n");
                        if (cuentas.isEmpty()) {
                            System.out.print("Aun no tienes nungna cuenta en UJAbank");
                        } else {
                            cuentas.forEach((cuenta) -> {
                                System.out.print("-ID: " + cuenta.getId() + "\t");
                                System.out.println("- Saldo: " + cuenta.getSaldo());
                            });
                            System.out.print("Esas son todas tu cuentas y su saldo");
                        }
                        System.out.println(" (pulsa enter para volver)");
                        break;
                    case 3:// Registrar usuario
                        break;
                    case 4:// Login usuario
                        break;
                    case 5:// Crear cuenta
                        break;
                    case 6:// Ingresar dinero
                        break;
                    case 7:// Transferir dinero
                        break;
                    case 8:// Retirar dinero
                        break;
                    case 9:// Consultar movimientos
                        break;
                    default:
                        System.out.println("SALIR");
                        opcion = -1;
                }
                if (opcion != -1) {
                    input.readLine();//Pausa para enter
                }
            } catch (Exception ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Finalazada operación");

//        EuroUJACoinRate e;
    }

}

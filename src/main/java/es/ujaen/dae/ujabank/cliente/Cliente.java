/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.cliente;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.entidades.Ingreso;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import es.ujaen.dae.ujabank.interfaces.Transaccion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author axpos
 */
public class Cliente {

    private final DTOUsuario usuario;
    private List<DTOCuenta> cuentas;
    private List<Tarjeta> tarjetas;

    UUID tokenUsuario;

    public Cliente() {
        usuario = new DTOUsuario();

        usuario.setDni("56555555Z");
        usuario.setDomicilio("c/ Pepito");
        usuario.setEmail("flo00008@red.ujaen.es");
        usuario.setNombre("flo00008");
        usuario.setTelefono("654 365 421");
        usuario.setfNacimiento(new Calendar.Builder().setDate(1996, 1, 1).build().getTime());

        cuentas = new ArrayList<>();
        tarjetas = crearTarjetas();
    }

    private void borrarConsola() {

        for (int i = 0; i < 10; ++i) {
            System.out.println("\n");
        }
    }

    public void run(ApplicationContext contexto) {
        ServiciosUsuario sUsuarios = (ServiciosUsuario) contexto.getBean("banco");
        ServiciosTransacciones sTrans = (ServiciosTransacciones) contexto.getBean("banco");

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int opcion = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        borrarConsola();
        while (opcion != -1) {
            try {
                //Menu
                System.out.println("Cual de estas opciones quieres realizar:");
                System.out.println("1: Modificar datos de usuario");
                System.out.println("2: Mostrar tus cuentas (logeado)");
                System.out.println("3: Registrar usuario");
                System.out.println("4: Login de usuario (registrado)");
                System.out.println("5: Crear una nueva cuenta (logeado)");
                System.out.println("6: ingresar dinero (logeado)");
                System.out.println("7: transferir dinero (logeado)");
                System.out.println("8: retirar dinero (logeado)");
                System.out.println("9: consultar movimientos (logeado)");
                System.out.println("otro: salir");

                opcion = Integer.parseInt(input.readLine());

                borrarConsola();
                switch (opcion) {
                    case 1: //Modificar datos
                        String dato;

                        System.out.println("Modificando el usuario, tus datos actuales son:");
                        System.out.println("-Nombre: " + usuario.getNombre());
                        System.out.println("-DNI: " + usuario.getDni());
                        System.out.println("-Domicilio: " + usuario.getDomicilio());
                        System.out.println("-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
                        System.out.println("-Telefono: " + usuario.getTelefono());
                        System.out.println("-Email: " + usuario.getEmail());

                        System.out.println("\n--------------------------------------\n");
                        System.out.println("Introduce tus nuevos datos: ");

                        System.out.print("\t+Nombre: ");
                        dato = input.readLine();
                        usuario.setNombre(dato);

                        System.out.print("\t+DNI: ");
                        dato = input.readLine();
                        usuario.setDni(dato);

                        System.out.print("\t+Domicilio: ");
                        dato = input.readLine();
                        usuario.setDomicilio(dato);

                        System.out.print("\t+Fecha de nacimiento (dd/mm/yyyy): ");
                        dato = input.readLine();
                        usuario.setfNacimiento(sdf.parse(dato));

                        System.out.print("\t+Telefono: ");
                        dato = input.readLine();
                        usuario.setTelefono(dato);

                        System.out.print("\t+Email: ");
                        dato = input.readLine();
                        usuario.setEmail(dato);

                        System.out.println("\n--------------------------------------\n");
                        System.out.println("Tus nuevos datos: ");

                        System.out.println("-Nombre: " + usuario.getNombre());
                        System.out.println("-DNI: " + usuario.getDni());
                        System.out.println("-Domicilio: " + usuario.getDomicilio());
                        System.out.println("-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
                        System.out.println("-Telefono: " + usuario.getTelefono());
                        System.out.println("-Email: " + usuario.getEmail());

                        break;
                    case 2: // Mostrar tus cuentas
                        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
                        System.out.println("\n--------------------------------------\n");
                        if (cuentas.isEmpty()) {
                            System.out.print("Aun no tienes nungna cuenta en UJAbank");
                        } else {
                            cuentas.forEach((cuenta) -> {
                                System.out.print("-ID: " + cuenta.getId() + "\t");
                                System.out.println("- Saldo (UJAC): " + cuenta.getSaldo());
                            });
                            System.out.print("Esas son todas tu cuentas y su saldo");
                        }
                        break;
                    case 3:// Registrar usuario
                        String contrasena;
                        System.out.println("Registrando usuario con nombre : " + usuario.getNombre());
                        System.out.print("Introduce la contraseña: ");

                        contrasena = input.readLine();

                        boolean registro = sUsuarios.registrar(usuario, contrasena);

                        if (registro) {
                            System.out.print("El registro se realizó con exito");
                        } else {
                            System.out.print("Hubo un erro al crear el usuario");
                        }

                        break;
                    case 4:// Login usuario
//                        String contrasena;
                        System.out.println("Haciendo el login para el usuario: " + usuario.getNombre());
                        System.out.print("Introduce la contraseña: ");

                        contrasena = input.readLine();
                        tokenUsuario = sUsuarios.login(usuario, contrasena);

                        System.out.println("Usuario logeado con éxito");

                        System.out.println("Sincronizando tus cuentas ...");
                        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
                        System.out.print(" ... cuentras sincronizadas");

                        break;
                    case 5:// Crear cuenta
                        System.out.println("Creando una cuenta :");
                        if (tokenUsuario == null) {
                            System.out.println("Necesitas logearte para continuar.");
                            break;
                        }
                        System.out.println("Enviando solicitud ...");

                        boolean cuentaCreada = sUsuarios.crearCuenta(tokenUsuario);

                        if (!cuentaCreada) {
                            System.out.print("\t-Hubo algún error al crear la cuenta");
                        } else {
                            System.out.println("\t-Cuenta creada con éxito");
                        }
                        break;
                    case 6:// Ingresar dinero
                        if (tokenUsuario == null) {
                            System.out.print("Solamente puedes realizar un ingreso si estás logeado");
                            break;
                        }

                        if (tarjetas.isEmpty()) {
                            System.out.print("No se puede realizar un ingreso sin ninguna tarjeta");
                            break;
                        }

                        System.out.println("Vas a realizar un ingreso: ");
                        float cantidad;
                        int tarjetaIngreso,
                         cuentaIngreso;

                        System.out.print("Elige el índice de la tarjeta a usar (tienes " + tarjetas.size() + " tarjetas): ");
                        tarjetaIngreso = Integer.parseInt(input.readLine());

                        System.out.print("Elige el índice de la cuenta a la que ingresar (tienes " + cuentas.size() + " cuentas): ");
                        cuentaIngreso = Integer.parseInt(input.readLine());

                        System.out.print("Introduce la cantidad de euros que deseas ingrear: ");
                        cantidad = Float.parseFloat(input.readLine());

                        System.out.println("Realizando ingreso ...");

                        boolean ingreso = sTrans.ingresar(tokenUsuario, tarjetas.get(tarjetaIngreso), cuentas.get(cuentaIngreso).getId(), cantidad);

                        if (ingreso) {
                            System.out.print("El ingreso se realizó con éxito");
                        } else {
                            System.out.print("Hubo un error en la transacción");
                        }

                        break;
                    case 7:// Transferir dinero
                        if (tokenUsuario == null) {
                            System.out.println("Necesitras estar logeado para realizar una transferencia");
                            break;
                        }

                        System.out.println("Vas a realizar una transferencia");

                        int cuentaOrigen,
                         idCuentaDestino;
                        String concepto;
                        System.out.print("Elige el índice de la cuenta del origen de transferencia (tienes " + cuentas.size() + " cuentas): ");
                        cuentaOrigen = Integer.parseInt(input.readLine());

                        System.out.print("Indica el ID de la cuenta de destino de transferencia: ");
                        idCuentaDestino = Integer.parseInt(input.readLine());

                        System.out.print("Indica la cantidad de UJACoins que quieres transferir: ");
                        cantidad = Float.valueOf(input.readLine());

                        System.out.print("Indica el concepto de la transferencia: ");
                        concepto = input.readLine();

                        System.out.println("Realizando transferencia ...");

                        boolean transferido = sTrans.transferir(tokenUsuario, cuentas.get(cuentaOrigen).getId(), idCuentaDestino, cantidad, concepto);

                        if (transferido) {
                            System.out.print("La transferencia se realizó correctamente");
                        } else {
                            System.out.print("Hubo un fallo en la transferencia");
                        }

                        break;
                    case 8:// Retirar dinero
                        if (tokenUsuario == null) {
                            System.out.println("Necesitas estar logeado para realizar un retiro");
                            break;
                        }

                        System.out.println("Vas a realizar un retiro:");

                        int tarjetaRetiro,
                         cuentaRetiro;

                        System.out.print("Elige el índice de la cuenta desde la que retirar (tienes " + cuentas.size() + " cuentas): ");
                        cuentaRetiro = Integer.parseInt(input.readLine());

                        System.out.print("Elige el índice de la tarjeta a usar (tienes " + tarjetas.size() + " tarjetas): ");
                        tarjetaRetiro = Integer.parseInt(input.readLine());

                        System.out.print("Indica la cantidad de UJACoins que quieres retirar: ");
                        cantidad = Float.valueOf(input.readLine());

                        boolean retiro = sTrans.retirar(tokenUsuario, cuentas.get(cuentaRetiro).getId(), tarjetas.get(tarjetaRetiro), cantidad);

                        if (retiro) {
                            System.out.print("El retiro se realizó con éxito");
                        } else {
                            System.out.print("Hubo un error al retirar el dinero");
                        }

                        break;
                    case 9:// Consultar movimientos
                        if (tokenUsuario == null) {
                            System.out.println("Necesitas estar logeado");
                            break;
                        }

                        System.out.print("Introduce el indice de la cuenta que quieres consultar (tienes " + cuentas.size() + " cuentas): ");
                        int posCuenta = Integer.parseInt(input.readLine());
                        Date fInicio,
                         fFin;

                        System.out.print("Introduce la fecha de inicio (dd/mm/yyyy): ");
                        String fecha = input.readLine();
                        fInicio = sdf.parse(fecha);

                        System.out.print("Introduce la fecha de fin (dd/mm/yyyy): ");
                        fecha = input.readLine();
                        fFin = sdf.parse(fecha);

                        List<Transaccion> operaciones = sTrans.consultar(tokenUsuario, cuentas.get(posCuenta).getId(), fInicio, fFin);

                        if (operaciones.isEmpty()) {
                            System.out.println("No se ha hecho ninguna operación entre esas fechas");
                        } else {
                            System.out.println("------------------");
                            operaciones.forEach((transaccion) -> {
                                System.out.println(transaccion.toString());
                                System.out.println("------------------");
                            });
                        }

                        break;

                    default:
                        System.out.println("SALIR");
                        opcion = -1;
                }
                if (opcion != -1) {
                    System.out.println(" (pulsa enter para volver)");
                    input.readLine();//Pausa para enter
                    borrarConsola();
                }
            } catch (Exception | Error ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("!Hubo un error: " + ex.getClass().getName());
            }
        }

        System.out.println("Finalazada operación");

//        EuroUJACoinRate e;
    }

    private static List<Tarjeta> crearTarjetas() {
        ArrayList<Tarjeta> tarjetas = new ArrayList<>();
        Tarjeta tarjeta;

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("123");
        tarjeta.setNumero(645541);
        tarjeta.setTitular("flo00008");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2025, 3, 4).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("431");
        tarjeta.setNumero(458210);
        tarjeta.setTitular("aep00015");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2024, 2, 11).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("479");
        tarjeta.setNumero(936189);
        tarjeta.setTitular("Pepe");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2023, 5, 2).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("572");
        tarjeta.setNumero(184036);
        tarjeta.setTitular("Blas");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2026, 2, 2).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("749");
        tarjeta.setNumero(295380);
        tarjeta.setTitular("Genaro");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2027, 3, 8).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("782");
        tarjeta.setNumero(872052);
        tarjeta.setTitular("Federico");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2022, 1, 1).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("692");
        tarjeta.setNumero(579204);
        tarjeta.setTitular("Jessie");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2028, 2, 2).build().getTime());
        tarjetas.add(tarjeta);

        tarjeta = new Tarjeta();//saldo de la tarjeta
        tarjeta.setCvv("285");
        tarjeta.setNumero(403782);
        tarjeta.setTitular("Walter");
        tarjeta.setfCaducidad(new Calendar.Builder().setDate(2025, 5, 6).build().getTime());
        tarjetas.add(tarjeta);

        return tarjetas;
    }

}

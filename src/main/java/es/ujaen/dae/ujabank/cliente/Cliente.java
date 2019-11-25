/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.cliente;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.cliente.menu.ConsoleUtils;
import es.ujaen.dae.ujabank.cliente.menu.Menu;
import es.ujaen.dae.ujabank.cliente.menu.MenuItem;
import es.ujaen.dae.ujabank.interfaces.ServiciosTransacciones;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private final List<Tarjeta> tarjetas;

    UUID tokenUsuario;

    ServiciosUsuario sUsuarios;
    ServiciosTransacciones sTrans;

    BufferedReader input;

    SimpleDateFormat sdf;

    public Cliente() {
        input = new BufferedReader(new InputStreamReader(System.in));

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
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

    void modificarDatos() {
//        System.out.println("Modificando el usuario, tus datos actuales son:");
        ConsoleUtils.println("-Nombre: " + usuario.getNombre());
        ConsoleUtils.println("-DNI: " + usuario.getDni());
        ConsoleUtils.println("-Domicilio: " + usuario.getDomicilio());
        ConsoleUtils.println("-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
        ConsoleUtils.println("-Telefono: " + usuario.getTelefono());
        ConsoleUtils.println("-Email: " + usuario.getEmail());

        ConsoleUtils.println("\n--------------------------------------\n");
        ConsoleUtils.println("Introduce tus nuevos datos: ");

//        System.out.print("+Nombre: ");
//        dato = input.readLine();
        usuario.setNombre(ConsoleUtils.getStringField("Nombre"));

//        System.out.print("\t+DNI: ");
//        dato = input.readLine();
        usuario.setDni(ConsoleUtils.getStringField("DNI"));

//        System.out.print("\t+Domicilio: ");
//        dato = input.readLine();
        usuario.setDomicilio(ConsoleUtils.getStringField("Domicilio"));

//        System.out.print("\t+Fecha de nacimiento (dd/mm/yyyy): ");
//        dato = input.readLine();
        usuario.setfNacimiento(ConsoleUtils.getDateField("Fecha de nacimiento"));

//        System.out.print("\t+Telefono: ");
//        dato = input.readLine();
        usuario.setTelefono(ConsoleUtils.getStringField("Telefono"));

//        System.out.print("\t+Email: ");
//        dato = input.readLine();
        usuario.setEmail(ConsoleUtils.getStringField("Email"));

        ConsoleUtils.println("\n--------------------------------------\n");
        ConsoleUtils.println("Tus nuevos datos: ");

        ConsoleUtils.println("-Nombre: " + usuario.getNombre());
        ConsoleUtils.println("-DNI: " + usuario.getDni());
        ConsoleUtils.println("-Domicilio: " + usuario.getDomicilio());
        ConsoleUtils.println("-Fecha de nacimiento: " + sdf.format(usuario.getfNacimiento()));
        ConsoleUtils.println("-Telefono: " + usuario.getTelefono());
        ConsoleUtils.println("-Email: " + usuario.getEmail());

    }

    void mostrarCuentas() {
        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
//        System.out.println("\n--------------------------------------\n");
        if (cuentas.isEmpty()) {
            ConsoleUtils.println("Aun no tienes nungna cuenta en UJAbank");
        } else {
            cuentas.forEach((cuenta) -> {
                ConsoleUtils.print("-ID: " + cuenta.getId() + "\t");
                ConsoleUtils.println("- Saldo (UJAC): " + cuenta.getSaldo());
            });
            ConsoleUtils.print("Esas son todas tu cuentas y su saldo");
        }
    }

    void registrar() {
        ConsoleUtils.println("Registrando usuario con nombre : " + usuario.getNombre());
//                        ConsoleUtils.print("Introduce la contraseña: ");

        String contrasena = ConsoleUtils.getStringField("Introduce la contraseña");

        sUsuarios.registrar(usuario, contrasena);

//                        if (registro) {
        ConsoleUtils.print("El registro se realizó con exito");
    }

    void login() {
        ConsoleUtils.println("Haciendo el login para el usuario: " + usuario.getNombre());
//        ConsoleUtils.print("Introduce la contraseña: ");

        String contrasena = ConsoleUtils.getStringField("Introduce la contraseña");
        tokenUsuario = sUsuarios.login(usuario, contrasena);

        ConsoleUtils.println("Usuario logeado con éxito");

        ConsoleUtils.println("Sincronizando tus cuentas ...");
        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
        ConsoleUtils.print(" ... cuentras sincronizadas");
    }

    void crearCuenta() {
        this.borrarConsola();

        if (tokenUsuario == null) {
            ConsoleUtils.println("Error: Necesitas logearte para continuar.");
            return;
        }

        ConsoleUtils.println("Creando cuenta ...");

        boolean cuentaCreada = sUsuarios.crearCuenta(tokenUsuario);

        if (!cuentaCreada) {
            ConsoleUtils.println("\t-Hubo algún error al crear la cuenta");
        } else {
            ConsoleUtils.println("\t-Cuenta creada con éxito");
        }
    }

    void ingresar() {
        this.borrarConsola();

        if (tokenUsuario == null) {
            ConsoleUtils.print("Error: Solamente puedes realizar un ingreso si estás logeado");
            return;
        }

        if (tarjetas.isEmpty()) {
            ConsoleUtils.print("Error: No se puede realizar un ingreso sin ninguna tarjeta");
            return;
        }

        ConsoleUtils.println("*** Vas a realizar un ingreso *** ");
        float cantidad;
        int tarjetaIngreso,
                cuentaIngreso;

        ConsoleUtils.println("Sincronizando tus cuentas ...");
        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
        ConsoleUtils.print(" ... cuentras sincronizadas");

        tarjetaIngreso = ConsoleUtils.getIntFieldWithLimits("Elige el índice de la tarjeta a usar (tienes " + tarjetas.size() + " tarjetas) ",
                0,//from 
                tarjetas.size() - 1);//to
//         Integer.parseInt(input.readLine());

//        System.out.print();
        cuentaIngreso = ConsoleUtils.getIntFieldWithLimits("Elige el índice de la cuenta a la que ingresar (tienes " + cuentas.size() + " cuentas) ",
                0,//from 
                cuentas.size() - 1);//to

        cantidad = ConsoleUtils.getFloatField("Introduce la cantidad de euros que deseas ingrear ");

        ConsoleUtils.println("Realizando ingreso ...");

        boolean ingreso = sTrans.ingresar(tokenUsuario, tarjetas.get(tarjetaIngreso), cuentas.get(cuentaIngreso).getId(), cantidad);

        if (ingreso) {
            ConsoleUtils.println("El ingreso se realizó con éxito");
        } else {
            ConsoleUtils.println("Hubo un error en la transacción");
        }
    }

    void transferir() {
        this.borrarConsola();

        if (tokenUsuario == null) {
            ConsoleUtils.println("Error: Necesitras estar logeado para realizar una transferencia");
            return;
        }

        ConsoleUtils.println("*** Vas a realizar una transferencia ***");

        ConsoleUtils.println("Sincronizando tus cuentas ...");
        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
        ConsoleUtils.print(" ... cuentras sincronizadas");

        float cantidad;
        int cuentaOrigen,
                idCuentaDestino;
        String concepto;

        cuentaOrigen = ConsoleUtils.getIntFieldWithLimits("Elige el índice de la cuenta del origen de transferencia (tienes " + cuentas.size() + " cuentas) ",
                0,
                cuentas.size() - 1);

        idCuentaDestino = ConsoleUtils.getIntField("Indica el ID de la cuenta de destino de transferencia ");
        cantidad = ConsoleUtils.getFloatField("Indica la cantidad de UJACoins que quieres transferir ");
        concepto = ConsoleUtils.getStringField("Indica el concepto de la transferencia ");

        ConsoleUtils.println("Realizando transferencia ...");

        boolean transferido = sTrans.transferir(tokenUsuario, cuentas.get(cuentaOrigen).getId(), idCuentaDestino, cantidad, concepto);

        if (transferido) {
            ConsoleUtils.print("La transferencia se realizó correctamente");
        } else {
            ConsoleUtils.print("Hubo un fallo en la transferencia");
        }
    }

    void retirar() {
        this.borrarConsola();

        if (tokenUsuario == null) {
            ConsoleUtils.println("Error: Necesitas estar logeado para realizar un retiro");
            return;
        }

        ConsoleUtils.println("*** Vas a realizar un retiro ***");

        ConsoleUtils.println("Sincronizando tus cuentas ...");
        cuentas = sUsuarios.consultarCuentas(tokenUsuario);
        ConsoleUtils.print(" ... cuentras sincronizadas");

        float cantidad;
        int tarjetaRetiro,
                cuentaRetiro;

        cuentaRetiro = ConsoleUtils.getIntFieldWithLimits("Elige el índice de la cuenta desde la que retirar (tienes " + cuentas.size() + " cuentas) ",
                0,
                cuentas.size() - 1);

        tarjetaRetiro = ConsoleUtils.getIntFieldWithLimits("Elige el índice de la tarjeta a usar (tienes " + tarjetas.size() + " tarjetas) ",
                0,
                tarjetas.size() - 1);

        cantidad = ConsoleUtils.getFloatField("Indica la cantidad de UJACoins que quieres retirar ");

        boolean retiro = sTrans.retirar(tokenUsuario, cuentas.get(cuentaRetiro).getId(), tarjetas.get(tarjetaRetiro), cantidad);

        if (retiro) {
            ConsoleUtils.print("El retiro se realizó con éxito");
        } else {
            ConsoleUtils.print("Hubo un error al retirar el dinero");
        }
    }

    void consultarMovimientos() {
        this.borrarConsola();

        if (tokenUsuario == null) {
            ConsoleUtils.println("Error: Necesitas estar logeado para consultar moviemientos de una cuenta");
            return;
        }

        ConsoleUtils.println("*** Consultando movimientos ***");

        int posCuenta;
        Date fInicio,
                fFin;

        posCuenta = ConsoleUtils.getIntFieldWithLimits("Introduce el índice de la cuenta que quieres consultar (tienes " + cuentas.size() + " cuentas) ",
                0,
                cuentas.size() - 1);
        fInicio = ConsoleUtils.getDateField("Introduce la fecha de inicio (dd/mm/yyyy) ");
        fFin = ConsoleUtils.getDateField("Introduce la fecha de fin (dd/mm/yyyy) ");

        List<DTOTransaccion> operaciones = sTrans.consultar(tokenUsuario, cuentas.get(posCuenta).getId(), fInicio, fFin);

        if (operaciones.isEmpty()) {
            ConsoleUtils.println("No se ha hecho ninguna operación entre esas fechas ");
        } else {
            ConsoleUtils.println("------------------");
            operaciones.forEach((transaccion) -> {

                switch (transaccion.getTipo()) {
                    case ingreso:
                        ConsoleUtils.println("Ingreso:");
                        break;
                    case transferencia:
                        ConsoleUtils.println("Transferencia:");
                        break;
                    case retiro:
                        ConsoleUtils.println("Retiro:");
                        break;
                }

                ConsoleUtils.println("\tId Origen: " + transaccion.getOrigen());
                ConsoleUtils.println("\tId Destino: " + transaccion.getDestino());
                ConsoleUtils.println("\tFecha: " + transaccion.getFecha().toString());
                ConsoleUtils.println("\tCantidad: " + transaccion.getCantidad());

                if (transaccion.getTipo() == DTOTransaccion.TIPO.transferencia) {
                    ConsoleUtils.println("Concepto: " + transaccion.getConcepto());
                }

                ConsoleUtils.println("------------------");
            });
        }

    }

    public void run(ApplicationContext contexto) {
        this.sUsuarios = (ServiciosUsuario) contexto.getBean("banco");
        this.sTrans = (ServiciosTransacciones) contexto.getBean("banco");

        Menu menu = new Menu("Salir", "Volver");
        menu.setTitle("*** Cliente UJABANK ***");

        menu.addItem(new MenuItem("Modificar datos de usuario", this::modificarDatos));
        menu.addItem(new MenuItem("Registrar usuario", this::registrar));
        menu.addItem(new MenuItem("Iniciar sesión (registrado)", this::login));
        menu.addItem(new MenuItem("Mostrar tus cuentas (logeado)", this::mostrarCuentas));
        menu.addItem(new MenuItem("Crear cuenta nueva (logeado)", this::crearCuenta));
        menu.addItem(new MenuItem("Ingresar (logeado)", this::ingresar));
        menu.addItem(new MenuItem("Transferir (logeado)", this::transferir));
        menu.addItem(new MenuItem("Retirar (logeado)", this::retirar));
        menu.addItem(new MenuItem("Consultar trsnsferencias (logeado)", this::consultarMovimientos));

        try {

            menu.execute();

        } catch (Exception | Error ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("!Hubo un error: " + ex.getClass().getName());
        }

        System.out.println("Finalazada operación");

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

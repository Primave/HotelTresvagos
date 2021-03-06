package ar.com.ada.hoteltresvagos;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.exception.ConstraintViolationException;

import ar.com.ada.hoteltresvagos.entities.*;
import ar.com.ada.hoteltresvagos.excepciones.*;
import ar.com.ada.hoteltresvagos.managers.*;

public class ABM {

    public static Scanner Teclado = new Scanner(System.in);

    protected HuespedManager ABMHuesped = new HuespedManager();
    protected ReservaManager ABMReserva = new ReservaManager();

    public void iniciar() throws Exception {

        try {

            ABMHuesped.setup();
            ABMReserva.setup();

            printOpciones();

            int opcion = Teclado.nextInt();
            Teclado.nextLine();

            while (opcion > 0) {

                if (opcion == 1) {

                    printOpcionHuesped();
                    opcion = Teclado.nextInt();
                    Teclado.nextLine();

                    switch (opcion) {
                        case 1:

                            try {
                                alta();
                            } catch (HuespedDNIException exdni) {
                                System.out.println("Error en el DNI. Indique uno valido");
                            }
                            break;

                        case 2:
                            baja();
                            break;

                        case 3:
                            modifica();
                            break;

                        case 4:
                            listar();
                            break;

                        case 5:
                            listarPorNombre();
                            break;

                        default:
                            System.out.println("La opcion no es correcta.");
                            break;
                    }

                } else if (opcion == 2) {

                    printOpcionReserva();
                    opcion = Teclado.nextInt();
                    Teclado.nextLine();

                    switch (opcion) {
                        case 1:
                            altaReserva();
                            break;

                        case 2:
                            bajaReserva();
                            break;

                        case 3:
                            modificarReserva();
                            break;

                        case 4:
                            listarReservas();
                            break;

                        case 5:
                            listarPorNombreHuesped();
                            break;

                        default:
                            System.out.println("La opcion no es correcta.");
                            break;
                    }
                }

                printOpciones();

                opcion = Teclado.nextInt();
                Teclado.nextLine();
            }

            // Hago un safe exit del manager
            ABMHuesped.exit();

        } catch (Exception e) {
            System.out.println("Que lindo mi sistema,se rompio mi sistema");
            throw e;
        } finally {
            System.out.println("Saliendo del sistema, bye bye...");

        }

    }

    public void alta() throws Exception {
        Huesped huesped = new Huesped();
        System.out.println("Ingrese el nombre: ");
        huesped.setNombre(Teclado.nextLine());
        System.out.println("Ingrese el DNI:");
        huesped.setDni(Teclado.nextInt());
        Teclado.nextLine();
        System.out.println("Ingrese la domicilio:");
        huesped.setDomicilio(Teclado.nextLine());

        System.out.println("Ingrese el Domicilio alternativo(OPCIONAL):");

        String domAlternativo = Teclado.nextLine();

        if (domAlternativo != null)
            huesped.setDomicilioAlternativo(domAlternativo);

        // vamos a generar una reserva.
        Reserva reserva = new Reserva();

        BigDecimal importeReserva = new BigDecimal(1000);
        reserva.setImporteReserva(importeReserva); // Forma 1
        // importeReserva.add(new BigDecimal(20));
        reserva.setImporteTotal(new BigDecimal(3000)); // forma 2
        reserva.setImportePagado(new BigDecimal(0));
        reserva.setFechaReserva(new Date()); // fecha actual

        System.out.println("ingrese la fecha de ingreso(dd/mm/yy)");

        Date fechaIngreso = null;
        Date fechaEgreso = null;

        DateFormat dFormat = new SimpleDateFormat("dd/MM/yy ");

        // Alternativa de leer fecha con try catch
        try {
            fechaIngreso = dFormat.parse(Teclado.nextLine());

        } catch (Exception ex) {
            System.out.println("Ingrese una fecha invalida ");
            System.out.println("Vuelva a empezar ");
            return;
        }

        // Alternativa de leer a los golpes(puede tirar una exception)
        System.out.println("Ingrese la fecha de egreso(dd/mm/yy)");
        fechaEgreso = dFormat.parse(Teclado.nextLine());

        reserva.setFechaIngreso(fechaIngreso);
        reserva.setFechaEgreso(fechaEgreso); // por ahora 1 dia
        reserva.setTipoEstadoId(20); // en el caso. 20 significa pagado
        reserva.setHuesped(huesped); // esta es la relacion bidirecional

        // Actualizo todos los objeto
        ABMHuesped.create(huesped);

        /*
         * Si concateno el OBJETO directamente, me trae todo lo que este en el metodo
         * toString() mi recomendacion es NO usarlo para imprimir cosas en pantallas, si
         * no para loguear info Lo mejor es usar:
         * System.out.println("Huesped generada con exito.  " + huesped.getHuespedId);
         */

        System.out.println("Huesped generada con exito.  " + huesped);

    }

    public void baja() {
        System.out.println("Ingrese el nombre :");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el ID de Huesped:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado");

        } else {

            try {

                ABMHuesped.delete(huespedEncontrado);
                System.out
                        .println("El registro del huesped " + huespedEncontrado.getHuespedId() + " ha sido eliminado");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una huesped. Error: " + e.getCause());
            }

        }
    }

    public void bajaPorDNI() {
        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();
        System.out.println("Ingrese el DNI de Huesped:");
        int dni = Teclado.nextInt();
        Huesped huespedEncontrado = ABMHuesped.readByDNI(dni);

        if (huespedEncontrado == null) {
            System.out.println("Huesped no encontrado.");

        } else {
            ABMHuesped.delete(huespedEncontrado);
            System.out.println("El registro del DNI " + huespedEncontrado.getDni() + " ha sido eliminado.");
        }
    }

    public void modifica() throws Exception {
        // System.out.println("Ingrese el nombre de la huesped a modificar:");
        // String n = Teclado.nextLine();

        System.out.println("Ingrese el ID de la huesped a modificar:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huespedEncontrado = ABMHuesped.read(id);

        if (huespedEncontrado != null) {

            // RECOMENDACION NO USAR toString(), esto solo es a nivel educativo.
            System.out.println(huespedEncontrado.toString() + " seleccionado para modificacion.");

            System.out.println(
                    "Elija qué dato de la huesped desea modificar: \n1: nombre, \n2: DNI, \n3: domicilio, \n4: domicilio alternativo");
            int selecper = Teclado.nextInt();

            switch (selecper) {
                case 1:
                    System.out.println("Ingrese el nuevo nombre:");
                    Teclado.nextLine();
                    huespedEncontrado.setNombre(Teclado.nextLine());

                    break;
                case 2:
                    System.out.println("Ingrese el nuevo DNI:");
                    Teclado.nextLine();
                    huespedEncontrado.setDni(Teclado.nextInt());
                    Teclado.nextLine();

                    break;
                case 3:
                    System.out.println("Ingrese el nuevo domicilio:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilio(Teclado.nextLine());

                    break;
                case 4:
                    System.out.println("Ingrese el nuevo domicilio alternativo:");
                    Teclado.nextLine();
                    huespedEncontrado.setDomicilioAlternativo(Teclado.nextLine());

                    break;

                default:
                    break;
            }

            // Teclado.nextLine();

            ABMHuesped.update(huespedEncontrado);

            System.out.println("El registro de " + huespedEncontrado.getNombre() + " ha sido modificado.");

        } else {
            System.out.println("Huesped no encontrado.");
        }

    }

    public void listar() {

        List<Huesped> todos = ABMHuesped.buscarTodos();
        for (Huesped c : todos) {
            mostrarHuesped(c);
        }
    }

    public void listarPorNombre() {

        System.out.println("Ingrese el nombre:");
        String nombre = Teclado.nextLine();

        List<Huesped> huespedes = ABMHuesped.buscarPor(nombre);
        for (Huesped huesped : huespedes) {
            mostrarHuesped(huesped);
        }
    }

    public void mostrarHuesped(Huesped huesped) {

        System.out.print("Id: " + huesped.getHuespedId() + " Nombre: " + huesped.getNombre() + " DNI: "
                + huesped.getDni() + " Domicilio: " + huesped.getDomicilio());

        if (huesped.getDomicilioAlternativo() != null)
            System.out.println(" Alternativo: " + huesped.getDomicilioAlternativo());
        else
            System.out.println();
    }

    // metodos de RESERVA

    public void altaReserva() throws Exception {
        System.out.println("-- ALTA DE RESERVACIÓN --");

        System.out.println("Introducir el DNI del huésped: ");
        int dni = Teclado.nextInt();
        Teclado.nextLine();
        Huesped huesped = ABMHuesped.readByDNI(dni);

        if (huesped == null) {

            System.out.println("No existe");
            return;
        }

        Reserva reserva = new Reserva();
        reserva.setHuesped(huesped);
        reserva.setFechaReserva(new Date());

        System.out.println("ingrese la fecha de ingreso(dd/mm/yy)");

        Date fechaIngreso = null;
        Date fechaEgreso = null;

        DateFormat dFormat = new SimpleDateFormat("dd/MM/yy");

        try {
            fechaIngreso = dFormat.parse(Teclado.nextLine());
            reserva.setFechaReserva(fechaIngreso);
            // reserva.setFechaIngreso(dFormat.parse(Teclado.nextLine())); (Otra opción)

        } catch (Exception ex) {
            System.out.println("Ingrese una fecha invalida");
            System.out.println("Vuelva a empezar");
            return;
        }

        // Alternativa de leer a los golpes(puede tirar una exception)
        System.out.println("Ingrese la fecha de egreso(dd/mm/yy)");

        dFormat = new SimpleDateFormat("dd/MM/yy");
        try {
            fechaEgreso = dFormat.parse(Teclado.nextLine());
            reserva.setFechaReserva(fechaEgreso);
            // reserva.setFechaIngreso(dFormat.parse(Teclado.nextLine())); (Otra opción)

        } catch (Exception ex) {
            System.out.println("Ingrese una fecha invalida");
            System.out.println("Vuelva a empezar");
            return;
        }

        System.out.println("Ingrese número de habitación.");
        int habitacion = Teclado.nextInt();
        Teclado.nextLine();

        System.out.println("Introducir el importe de la reserva: ");
        BigDecimal importeReserva = Teclado.nextBigDecimal();
        Teclado.nextLine();

        System.out.println("Introducir el importe total: ");
        BigDecimal importeTotal = Teclado.nextBigDecimal();
        Teclado.nextLine();

        System.out.println("Introducir el importe pagado: ");
        BigDecimal importePagado = Teclado.nextBigDecimal();
        Teclado.nextLine();

        System.out.println("Ingrese el estado de pago: ");
        // otra forma reserva.setTipoEstadoId(Teclado.nextInt());
        int tipoEstadoId = Teclado.nextInt();
        Teclado.nextLine();

        reserva.setFechaIngreso(fechaIngreso);
        reserva.setFechaEgreso(fechaEgreso);
        reserva.setHabitacion(habitacion);
        reserva.setImporteReserva(importeReserva);
        reserva.setImporteTotal(importeTotal);
        reserva.setImportePagado(importePagado);
        reserva.setTipoEstadoId(tipoEstadoId);

        // Actualizo todos los objeto
        ABMReserva.create(reserva);
        System.out.println("Reserva generada con exito.  " + reserva);

    }

    public void bajaReserva() {

        System.out.println("Ingrese el ID de la reserva:");
        int id = Teclado.nextInt();
        Teclado.nextLine();
        Reserva reservaEncontrada = ABMReserva.read(id);

        if (reservaEncontrada == null) {
            System.out.println("Reserva no encontrada");

        } else {

            try {

                ABMReserva.delete(reservaEncontrada);
                System.out.println(
                        "El registro de la reserva " + reservaEncontrada.getReservaId() + " ha sido eliminado.");
            } catch (Exception e) {
                System.out.println("Ocurrio un error al eliminar una reserva. Error: " + e.getCause());
            }

        }
    }

    public void modificarReserva() throws Exception { // TODO
        // **
        // int id = ingresarID();
        // Reserva reservaEncontrada = ABMReserva.read(id);

        // if (reservaEncontrada == null) {
        // System.out.println("Reserva no encontrada");

        // } else {

        // System.out.println("Editar reserva: " + reservaEncontrada.toString());

        // System.out.println(
        // "Editar: \n1: fecha de ingreso \n2: fecha de egreso \n4: habitación \n5:
        // importe total \n6: importe de reserva \n7: importe pagado \n8: estado de
        // pago");
        // int seleccionarDato = Teclado.nextInt();

        // switch (seleccionarDato) {
        // case 1:
        // System.out.println("Ingrese la nueva Fecha de ingreso :");
        // Date fechaIngreso = Teclado.nextLine();
        // reservaEncontrada.setFechaIngreso(fechaIngreso);
        // break;

        // System.out.println("Ingrese el nuevo nombre:");
        // Teclado.nextLine();
        // huespedEncontrado.setNombre(Teclado.nextLine());
        // case 2:
        // Date fechaIngreso = this.ingresarFechaDeIngreso();
        // reservaEncontrada.setFechaIngreso(fechaIngreso);
        // break;
        // case 3:
        // Date fechaEgreso = this.ingresarFechaDeEgreso();
        // reservaEncontrada.setFechaEgreso(fechaEgreso);
        // break;
        // case 4:
        // int habitacion = this.ingresarHabitacion();
        // reservaEncontrada.setHabitacion(habitacion);
        // break;

        // case 5:
        // BigDecimal importeTotal = this.ingresarImporteTotal();
        // reservaEncontrada.setImporteTotal(importeTotal);
        // break;

        // case 6:
        // BigDecimal importeReserva = this.ingresarImporteReserva();
        // reservaEncontrada.setImporteReserva(importeReserva);
        // break;

        // case 7:
        // BigDecimal importePagado = this.ingresarImportePagado();
        // reservaEncontrada.setImportePagado(importePagado);
        // break;

        // case 8:
        // int estadoPago = this.ingresarEstadoPago();
        // reservaEncontrada.setTipoEstadoId(estadoPago);
        // break;

        // default:
        // break;
        // }

        // ABMReserva.update(reservaEncontrada);

        // System.out.println("La reserva ha sido modificada.");
        // }

    }

    public void listarReservas() {
        List<Reserva> todos = ABMReserva.buscarTodos();
        for (Reserva r : todos) {
            mostrarReserva(r);
        }
    }

    public void listarPorNombreHuesped() {
        System.out.println("FUNCIONA LISTA POR NOMBRE"); // TODO
    }

    public void mostrarReserva(Reserva reserva) {
        System.out.print("Id: " + reserva.getReservaId() + "\n Fecha de ingreso: " + reserva.getFechaIngreso()
                + "\n Fecha de egreso: " + reserva.getFechaEgreso() + "\n Importe total: " + reserva.getImporteTotal()
                + "\n Importe pagado: " + reserva.getImportePagado());
        // TODO agregar impresion de estado de reserva

        if (reserva.getHabitacion() != null) {
            System.out.println("\n Habitacion: " + reserva.getHabitacion());

        } else {
            System.out.println();
        }
    }

    // PRINT OPCIONES

    public static void printOpciones() {
        System.out.println("======================================");
        System.out.println("");
        System.out.println("Seleccione una de las siguientes opciones para continuar. \n");
        System.out.println("1. Menú de huespedes.");
        System.out.println("2. Menú de reservas.");
        System.out.println("0. Para terminar.");
        System.out.println("");
        System.out.println("========================================");
    }

    public static void printOpcionHuesped() {
        System.out.println("\n MENÚ HUESPEDES \n");
        System.out.println("");
        System.out.println("1. Para agregar un huesped.");
        System.out.println("2. Para eliminar un huesped.");
        System.out.println("3. Para modificar un huesped.");
        System.out.println("4. Para ver el listado de huespedes.");
        System.out.println("5. Buscar un huesped por nombre especifico(SQL Injection)).");
        System.out.println("");
        System.out.println("=======================================");
    }

    public static void printOpcionReserva() {
        System.out.println("\n MENÚ RESERVAS \n");
        System.out.println("");
        System.out.println("1. Para agregar una reserva.");
        System.out.println("2. Para eliminar una reserva.");
        System.out.println("3. Para modificar una reserva.");
        System.out.println("4. Para ver el listado de reservas.");
        System.out.println("5. Para ver el listado de reservas de un huesped por su nombre.");
        System.out.println("");
        System.out.println("=======================================");
    }
}
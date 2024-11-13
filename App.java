import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estadio estadio = new Estadio();
        boolean continuar = true;

        System.out.println("Bienvenido al sistema de reservación del Estadio");

        while (continuar) {
            // Solicitar datos del cliente
            System.out.print("\nIngrese su nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese su email: ");
            String email = scanner.nextLine();
            System.out.print("Ingrese su teléfono: ");
            String telefono = scanner.nextLine();
            Cliente cliente = new Cliente(nombre, email, telefono);

            // Mostrar disponibilidad de secciones y precios
            System.out.println("\nSecciones disponibles y costo por asiento:");
            estadio.verDisponibilidad();
            System.out.println("Precios: Field Level - $50, Main Level - $30, Grandstand Level - $20");

            // Preguntar la sección de interés
            System.out.print("\n¿En qué sección le gustaría reservar? ");
            String seccion = scanner.nextLine();

            // Validar la sección
            if (!esSeccionValida(seccion)) {
                System.out.println("La sección ingresada no es válida. Inténtelo de nuevo.");
                continue;
            }

            // Solicitar la cantidad de asientos juntos que necesita
            System.out.print("¿Cuántos asientos necesita juntos? ");
            int cantidadAsientos;
            try {
                cantidadAsientos = Integer.parseInt(scanner.nextLine());
                if (cantidadAsientos <= 0) {
                    System.out.println("La cantidad de asientos debe ser mayor que cero.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida para la cantidad de asientos.");
                continue;
            }

            // Intentar reservar los asientos juntos en la sección deseada
            boolean reservaExitosa = estadio.reservarAsientosJuntos(cliente, seccion, cantidadAsientos);

            if (reservaExitosa) {
                int precio = obtenerPrecioPorSeccion(seccion);
                System.out.println("Reservación exitosa. Costo total: $" + (precio * cantidadAsientos));
            } else {
                // Opción de lista de espera si no hay asientos disponibles
                System.out.print("No hay asientos juntos disponibles en esta sección. ¿Desea unirse a la lista de espera? (S/N): ");
                String respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("S")) {
                    estadio.agregarAListaEspera(cliente, seccion);
                } else {
                    System.out.println("Reservación cancelada.");
                }
            }

            // Preguntar si se desea cancelar una reservación
            System.out.print("\n¿Desea cancelar una reservación? (S/N): ");
            String cancelar = scanner.nextLine();
            if (cancelar.equalsIgnoreCase("S")) {
                System.out.print("Ingrese el nombre del cliente cuya reservación desea cancelar: ");
                String nombreCancelar = scanner.nextLine();
                Cliente clienteCancelar = buscarClientePorNombre(estadio, nombreCancelar);
                if (clienteCancelar != null) {
                    estadio.cancelarReservacion(clienteCancelar);
                    System.out.println("Su reservación ha sido cancelada. El asiento ha sido asignado a la primera persona en lista de espera, si aplica.");
                } else {
                    System.out.println("No se encontró una reservación para " + nombreCancelar);
                }
            }

            // Preguntar si el usuario desea salir del programa
            System.out.print("\n¿Desea salir del programa? (S/N): ");
            String salir = scanner.nextLine();
            if (salir.equalsIgnoreCase("S")) {
                continuar = false;
                System.out.println("Gracias por usar el sistema de reservaciones. ¡Hasta luego!");
            }
        }

        scanner.close();
    }

    // Método auxiliar para obtener el precio de acuerdo a la sección
    private static int obtenerPrecioPorSeccion(String seccion) {
        switch (seccion) {
            case "Field Level":
                return 50;
            case "Main Level":
                return 30;
            case "Grandstand Level":
                return 20;
            default:
                return 0;
        }
    }

    // Método auxiliar para validar si la sección ingresada es válida
    private static boolean esSeccionValida(String seccion) {
        return seccion.equals("Field Level") || seccion.equals("Main Level") || seccion.equals("Grandstand Level");
    }

    // Método auxiliar para buscar un cliente por su nombre en las reservaciones
    private static Cliente buscarClientePorNombre(Estadio estadio, String nombre) {
        for (Cliente cliente : estadio.getReservaciones().keySet()) {
            if (cliente.getNombre().equalsIgnoreCase(nombre)) {
                return cliente;
            }
        }
        return null;
    }
}

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estadio stadium = new Estadio();
        boolean valid = true;

        System.out.println("Welcome to the ticket system for the baseball game at the stadium.");

        while (valid) {
            // Ask user data
            System.out.print("\nEnter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter phone number: ");
            int phoneNumber = scanner.nextInt();
            Cliente client = new Cliente(name, email, phoneNumber);

            // Mostrar disponibilidad de secciones y precios
            System.out.println("\nAvailable sections and seats price:");
            stadium.verDisponibilidad();
            System.out.println("Prices: Field Level - $50, Main Level - $30, Grandstand Level - $20");

            // Preguntar la sección de interés
            System.out.print("\nIn what section would you like to reserve? ");
            String section = scanner.nextLine();

            // Validar la sección
            if (!esSeccionValida(section)) {
                System.out.println("The section entered is not valid. Please try again.");
                continue;
            }

            // Solicitar la cantidad de asientos juntos que necesita
            System.out.print("Enter the number of seats you would like to reserve: ");
            int seatsQuantity;
            try {
                seatsQuantity = Integer.parseInt(scanner.nextLine());
                if (seatsQuantity <= 0) {
                    System.out.println("The number of seats must be greater than 0.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("The number of seats entered is not valid. Please try again.");
                continue;
            }

            // Intentar reservar los asientos juntos en la sección deseada
            boolean revervationSuccesful = stadium.reservarAsientosJuntos(client, section, seatsQuantity);

            if (revervationSuccesful) {
                int price = obtenerPrecioPorSeccion(section);
                System.out.println("Reservation succesful, total cost: " + (price * seatsQuantity));
            } else {
                // Opción de lista de espera si no hay asientos disponibles
                System.out.print("There are no seats available in the section you selected. Would you like to be added to the waiting list? (S/N): ");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("S")) {
                    stadium.agregarAListaEspera(client, section);
                } else {
                    System.out.println("Thank you for using the reservation system. Have a nice day!");
                }
            }

            // Preguntar si se desea cancel una reservación
            System.out.print("\nWould you like to cancel a reservation? (S/N): ");
            String cancel = scanner.nextLine();
            if (cancel.equalsIgnoreCase("S")) {
                System.out.print("Enter the name of the person whose reservation you would like to cancel: ");
                String nameCancel = scanner.nextLine();
                Cliente clientCancel = buscarClientePorname(stadium, nameCancel);
                if (clientCancel != null) {
                    stadium.cancelReservacion(clientCancel);
                    System.out.println("Reservation canceled successfully.");
                } else {
                    System.out.println("No reservation was found for" + nameCancel);
                }
            }

            // Preguntar si el usuario desea exit del programa
            System.out.print("\nWould you like to exit the program? (S/N): ");
            String exit = scanner.nextLine();
            if (exit.equalsIgnoreCase("S")) {
             valid = false;
                System.out.println("Thank you for using the reservation system. Have a nice day!");
            }
        }

        scanner.close();
    }

    // Método auxiliar para obtener el precio de acuerdo a la sección
    private static int obtenerPrecioPorSeccion(String section) {
        switch (section) {
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

    // Método auxiliar para buscar un cliente por su name en las reservaciones
    private static Cliente buscarClientePorname(Estadio stadium, String name) {
        for (Cliente client : stadium.getReservaciones().keySet()) {
            if (client.getName().equalsIgnoreCase(name)) {
                return client;
            }
        }
        return null;
    }
}

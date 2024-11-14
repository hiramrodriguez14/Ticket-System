import java.util.Scanner;
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estadio stadium = new Estadio();
        boolean valid = true;

        System.out.println("Welcome to the ticket system for the baseball game at the stadium.");

     
        while (valid) {

            boolean validEmail = false;
            boolean validPhone = false;
            String email = "";
            String phoneNumber = "";
            // Ask user data
            System.out.print("\nEnter name: ");
            String name = scanner.nextLine();

            while(!validEmail){
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.endsWith("@upr.edu") || email.endsWith("@gmail.com") || email.endsWith("@hotmail.com")) {
                validEmail = true; 
            } else {
                System.out.println("Invalid email domain. Please enter a valid email ending with @upr.edu, @gmail.com, or @hotmail.com.");
            }
        }   

            while(!validPhone){
            System.out.print("Enter phone number: ");
             phoneNumber = scanner.nextLine();
             phoneNumber = phoneNumber.replace("-","");
             if(phoneNumber.length() == 10){
                    validPhone = true;
             }else{
                    System.out.println("Invalid phone number. Please enter a valid phone number with 10 digits.");
             }
            }
           
            Cliente client = new Cliente(name, email, phoneNumber);

            validEmail = false;
            validPhone = false;
            name = "";
            email = "";
            phoneNumber = "";

            // Mostrar disponibilidad de secciones y precios
            System.out.println("\nAvailable sections and seats price:");
            stadium.lookAvailability();
            System.out.println("Prices: Field Level - $50, Main Level - $30, Grandstand Level - $20");

            // Preguntar la sección de interés
            System.out.println("\nIn what section would you like to reserve? ");
            String section = scanner.nextLine();
            section = section.toLowerCase();
            section = section.replace(" ", "");
            // Validar la sección
            if (!isValidSection(section)) {
                System.out.println("The section entered is not valid. Please try again.");
                continue;
            }

            // Solicitar la cantidad de asientos juntos que necesita
            System.out.println("Enter the number of seats you would like to reserve: ");
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
            boolean revervationSuccesful = stadium.reserveJointSeats(client, section, seatsQuantity);

            if (revervationSuccesful) {
                int price = obtainPriceBySection(section);
                System.out.println("Reservation succesful, total cost: " + (price * seatsQuantity));
            } else {
                // Opción de lista de espera si no hay asientos disponibles
                System.out.println("There are no seats available in the section you selected. Would you like to be added to the waiting list? (S/N): ");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("S")) {
                    stadium.addAwaitList(client, section);
                } else {
                    System.out.println("Thank you for using the reservation system. Have a nice day!");
                }
            }

            // Preguntar si se desea cancel una reservación
            System.out.println("\nWould you like to cancel a reservation? (S/N): ");
            String cancel = scanner.nextLine();
            if (cancel.equalsIgnoreCase("S")) {
                System.out.println("Enter the name of the person whose reservation you would like to cancel: ");
                System.out.print("\nEnter name: ");
    
                while(!validEmail){
                System.out.print("Enter email: ");
                email = scanner.nextLine();
                if (email.endsWith("@upr.edu") || email.endsWith("@gmail.com") || email.endsWith("@hotmail.com")) {
                    validEmail = true; 
                } else {
                    System.out.println("Invalid email domain. Please enter a valid email ending with @upr.edu, @gmail.com, or @hotmail.com.");
                }
            }   
    
                while(!validPhone){
                System.out.print("Enter phone number: ");
                 phoneNumber = scanner.nextLine();
                 phoneNumber = phoneNumber.replace("-","");
                 if(phoneNumber.length() == 10){
                        validPhone = true;
                 }else{
                        System.out.println("Invalid phone number. Please enter a valid phone number with 10 digits.");
                 }
                }
               
                Cliente clientCancel = lookClientByName(stadium, name, email, phoneNumber);
                if (clientCancel != null) {
                    stadium.cancelReservation(clientCancel);
                    System.out.println("Reservation canceled successfully.");
                } else {
                    System.out.println("No reservation was found for" + name);
                }
            }

            // Preguntar si el usuario desea exit del programa
            System.out.print("\nWould you like to exit the program? (S/N): ");
            boolean validExit = false;
            String exit = scanner.nextLine();

            while(!validExit){
            if (exit.equalsIgnoreCase("S")) {
             valid = false;
             validExit = true;
            System.out.println("Thank you for using the reservation system. Have a nice day!");
            }else if(exit.equalsIgnoreCase("N")){
                validExit = true;
            }else{
                System.out.println("Invalid input. Please enter S or N.");
            }
         }
    }
        scanner.close();
    }

    // Método auxiliar para obtener el precio de acuerdo a la sección
    private static int obtainPriceBySection(String section) {
        switch (section) {
            case "fieldlevel":
                return 50;
            case "mainlevel":
                return 30;
            case "grandstandlevel":
                return 20;
            default:
                return 0;
        }
    }

    // Método auxiliar para validar si la sección ingresada es válida
    private static boolean isValidSection(String section) {
        return section.equals("fieldlevel") || section.equals("mainlevel") || section.equals("grandstandlevel");
    }

    // Método auxiliar para buscar un cliente por su name en las reservaciones
    private static Cliente lookClientByName(Estadio stadium, String name, String email, String phoneNum) {
        for (Cliente client : stadium.getReservations().keySet()) {
            if ((client.getName().equalsIgnoreCase(name))&&(client.getEmail().equalsIgnoreCase(email))&&(client.getPhoneNum().equalsIgnoreCase(phoneNum))) {
                return client;
            }
        }
        return null;
    }

    
}


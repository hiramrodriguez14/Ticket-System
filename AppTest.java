import java.util.Scanner;

/**
 * The AppTest class implements a terminal-based user interface for managing 
 * ticket reservations in a baseball stadium. It allows users to create clients, 
 * make or cancel reservations, and view seat availability by interacting with 
 * the Estadio class.
 */
public class AppTest {

    /**
     * Prompts the user to enter details to create a new Cliente object.
     * Ensures that the entered email and phone number are valid.
     *
     * @return a new Cliente object created from the entered details
     */
    private static Cliente newClient() {

        Scanner scanner = new Scanner(System.in);
        boolean valid = true;

        boolean validEmail = false;
        boolean validPhone = false;
        String email = "";
        String phoneNumber = "";

        System.out.println("Enter name: ");
        String name = scanner.nextLine();

        // Validate email
        while(!validEmail){
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (email.endsWith("@upr.edu") || email.endsWith("@gmail.com") || email.endsWith("@hotmail.com")) {
                validEmail = true; 
            } else {
                System.out.println("Invalid email domain. Please enter a valid email ending with @upr.edu, @gmail.com, or @hotmail.com.");
            }
        }

        // Validate phone number
        while(!validPhone){
            System.out.print("Enter phone number: ");
            phoneNumber = scanner.nextLine();
            phoneNumber = phoneNumber.replace("-","");
            if(phoneNumber.length() == 10){ //supposed to be 10
                validPhone = true;
            }else{
               System.out.println("Invalid phone number. Please enter a valid phone number with 10 digits.");
            }
        }

        Cliente client = null;
        if(validEmail && validPhone) {
            client = new Cliente(name, email, phoneNumber);
        }

        return client;
    }

    /**
     * Returns the price of seats based on the specified section.
     *
     * @param section the section of the stadium
     * @return the price per seat in the specified section
     */
    private static int obtainPriceBySection(String section) {
        switch (section) {
            case "fieldlevel":
                return 300;
            case "mainlevel":
                return 120;
            case "grandstandlevel":
                return 45;
            default:
                return 0;
        }
    }

    /**
     * Validates if the given section is one of the valid sections in the stadium.
     *
     * @param section the section name to validate
     * @return true if the section is valid, false otherwise
     */
    private static boolean isValidSection(String section) {
        return section.equals("fieldlevel") || section.equals("mainlevel") || section.equals("grandstandlevel");
    }


    /**
     * The main method that handles user interaction with the ticket reservation system.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estadio stadium = new Estadio();
        boolean valid = true;

        boolean running = true; 
    while (running) {
        System.out.println("Welcome to the ticket system for the baseball game at the stadium.");
        System.out.println("Would you like to make or cancel a reservation? (make/cancel)");
        String cancelOrReserve = scanner.nextLine();

        if (cancelOrReserve.equalsIgnoreCase("cancel")) {
            
            stadium.cancelReservation(newClient());
            continue; 
        } else if (cancelOrReserve.equalsIgnoreCase("make")) {
            Cliente client = newClient();

            boolean validSection = false;
            while (!validSection) {
                System.out.println("\nAvailable sections and seats price:");
                stadium.lookAvailability();
                System.out.println("Prices: Field Level - $300, Main Level - $120, Grandstand Level - $45");
                System.out.println("\nIn what section would you like to reserve? ");

                String section = scanner.nextLine();
                section = section.toLowerCase();
                section = section.replace(" ", "");

                if (!isValidSection(section)) {
                    System.out.println("The section entered is not valid. Please try again.");
                    continue;
                }

                System.out.println("Enter the number of seats you would like to reserve: ");
                int seatsQuantity;
                try {
                    seatsQuantity = Integer.parseInt(scanner.nextLine());
                    if (seatsQuantity <= 0) {
                        System.out.println("The number of seats must be greater than 0.");
                        continue;
                    }
                    if (seatsQuantity > 50) {
                        System.out.println("You cannot reserve more than 50 seats at a time. Please enter a valid number.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("The number of seats entered is not valid. Please try again.");
                    continue;
                }

                boolean reservationSuccessful = stadium.reserveSeats(client, section, seatsQuantity);

                if (reservationSuccessful) {
                    int price = obtainPriceBySection(section);
                    System.out.println("Reservation successful!");
                    System.out.println("Section: " + section + ", Seats: " + seatsQuantity + ", Total Cost: $" + (price * seatsQuantity));

                    System.out.println("Are you sure about the reservation? (yes/no): ");
                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("no")) {
                        // cancel the reservation
                        stadium.cancelReservation(client);
                        System.out.println("Reservation cancelled. Returning to section selection...");
                        continue; 
                    } else if (confirmation.equalsIgnoreCase("yes")) {
                        System.out.println("Thank you! Your reservation is confirmed.");
                        validSection = true; 
                    } else {
                        System.out.println("Invalid response. Assuming you want to keep the reservation.");
                        validSection = true; 
                    }

                    System.out.println("Returning to the main menu...");
                    break; 
                } else {
                    System.out.println("There are no seats available in the section you selected. Would you like to be added to the waiting list? (yes/no): ");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("yes")) {
                        stadium.addAwaitList(client, section);
                        System.out.println("You have been added to the waiting list.");
                    } else {
                        System.out.println("Thank you for using the reservation system. Returning to the main menu...");
                    }
                    break;
                }
            }
        } else {
            System.out.println("Invalid option. Please type 'make' or 'cancel'.");
        }
    }
    scanner.close();
}

}

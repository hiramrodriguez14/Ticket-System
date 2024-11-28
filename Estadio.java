import java.util.*;

// Estadio represents the stadium with sections, it allows the user to reserve seats
// it includes reservations, waitlists, cancelations and seat availability

public class Estadio {
    private Set<Asiento> availableSeats = new HashSet<>();
    private Map<Cliente, List<Asiento>> reservations = new HashMap<>();
    private LinkedList<Cliente> reservationHistory = new LinkedList<>();
    public Stack<Cliente> actionsUndo = new Stack<>();
    private Map<String, Queue<Cliente>> waitList = new HashMap<>();

    // constructor of the class Estadio
    public Estadio() {
        // initialize seats from estadio on each section
        initializeSeats("fieldlevel", 10, 50);
        initializeSeats("mainlevel", 20, 50);
        initializeSeats("grandstandlevel", 40, 50);

        // initialize wait lists for each section
        waitList.put("Field Level", new LinkedList<>());
        waitList.put("Main Level", new LinkedList<>());
        waitList.put("Grandstand Level", new LinkedList<>());
    }

    // method for initializing seats of a section
    private void initializeSeats(String section, int rows, int seatsPerRow) {
        for (int r = 1; r <= rows; r++) {
            for (int a = 1; a <= seatsPerRow; a++) {
                availableSeats.add(new Asiento(section, r, a));
            }
        }
    }

    // reserves a certain amount of seats for a client in a specific section
    // organizes the seats available per rows
    public boolean reserveSeats(Cliente client, String section, int quantity) {
        // organize available seats per row
        Map<Integer, List<Asiento>> seatsPerRow = new HashMap<>();
        for (Asiento seat : availableSeats) {
            if (seat.getSection().equals(section)) {
                seatsPerRow
                    .computeIfAbsent(seat.getRow(), k -> new ArrayList<>())
                    .add(seat);
            }
        }

        // los for rows with enough seats together
        for (Map.Entry<Integer, List<Asiento>> entry : seatsPerRow.entrySet()) {
            List<Asiento> rowSeats = entry.getValue();
            rowSeats.sort(Comparator.comparingInt(Asiento::getSeatNumber));

            List<Asiento> jointSeats = new ArrayList<>();
            for (int i = 0; i < rowSeats.size(); i++) {
                Asiento currentSeat = rowSeats.get(i);
                if (jointSeats.isEmpty() || currentSeat.getSeatNumber() == jointSeats.get(jointSeats.size() - 1).getSeatNumber() + 1) {
                    jointSeats.add(currentSeat);
                    if (jointSeats.size() == quantity) {
                        // reserve seats
                        availableSeats.removeAll(jointSeats);
                        reservations.put(client, new ArrayList<>(jointSeats));
                        reservationHistory.add(client);
                        for(Cliente clien : reservationHistory) {
                            System.out.println(clien.getName());
                        }
                        actionsUndo.push(client);
                        System.out.println("Reservation succesful: " + client.getName() + " - ");
                        for(Asiento asiento : jointSeats) {
                            System.out.println(asiento);
                        }
                        return true;
                    }
                } else {
                    jointSeats.clear();
                    jointSeats.add(currentSeat);
                }
            }
        }
        return false; // didnt find enough seats
    }

    
    
    // add a client looking for seats in a specific section to a wait list
    public void addAwaitList(Cliente client, String section) {
        // Check if the section exists in the waitlist map
        Queue<Cliente> wait = waitList.get(section);
        
        if (wait == null) {
            // If the section doesn't have a waitlist, initialize it
            wait = new LinkedList<>();
            waitList.put(section, wait);
            System.out.println("The specified section didn't have a waitlist. A new one has been created.");
            
        }
        
        // Add the client to the waitlist for the given section
        wait.add(client);
        reservationHistory.add(client); // optional depending on if you want to keep track of this client
        System.out.println(client.getName() + " has been added to the waitlist for the section " + section);

        System.out.println(wait);
    }
    
    // cancels reservation of a client and makes those seats available 
    public void cancelReservation(Cliente client) {
        // loop through reservation history to find the client
        for (int i = 0; i < reservationHistory.size(); i++) {
            if (reservationHistory.get(i).equals(client)) {
                // remove client from reservation history
                reservationHistory.remove(i);
                // get the reserved seats for this client
                List<Asiento> seats = reservations.remove(client);
                System.out.println("Reservation successfully canceled. Your money has been refunded.\n");
    
                // check if there are seats to release
                if (seats != null) {
                    // Add the canceled seats back to available seats
                    availableSeats.addAll(seats);
    
                    // Identify the section of the seats that were reserved
                    String section = seats.get(0).getSection();
                    // Get the waiting list for the section
                    Queue<Cliente> wait = waitList.get(section);
    
                    // If there's anyone in the waiting list, attempt to reserve seats for them
                    if (wait != null && !wait.isEmpty()) {
                        Cliente next = wait.poll(); // get the first client in the waiting list
                        // try to reserve the same number of seats for the client
                        boolean reserveSucceed = reserveSeats(next, section, seats.size());
    
                        if (reserveSucceed) {
                            System.out.println("It has been automatically reserved for " + next.getName() + " from the waitlist.");
                        } else {
                            // If reservation failed, add the client back to the waiting list
                            addAwaitList(next, section);
                            System.out.println("Could not reserve the same seats. " + next.getName() + " is back on the waiting list.");
                        }
                    } else {
                        System.out.println("No one is currently on the waiting list for this section.");
                    }
                } else {
                    System.out.println("No reservation found for " + client.getName());
                }
    
                return;
            }
        }
    
        // if the client is not found in reservation history
        System.out.println("Client not found in reservation history.");
    }
    
    // shows available seats of a section
    public void lookAvailability() {
        Map<String, Integer> availability = new HashMap<>();
        for (Asiento seat : availableSeats) {
            availability.put(seat.getSection(), availability.getOrDefault(seat.getSection(), 0) + 1);
        }
        availability.forEach((section, quantity) -> {
        StringBuilder modifiedSection = new StringBuilder(section);
        modifiedSection.setCharAt(0, Character.toUpperCase(modifiedSection.charAt(0)));

   
        int fifthFromEndIndex = modifiedSection.length() - 5;
        modifiedSection.setCharAt(fifthFromEndIndex, Character.toUpperCase(modifiedSection.charAt(fifthFromEndIndex)));


        modifiedSection.insert(fifthFromEndIndex, ' ');
        System.out.println(modifiedSection + ": " + quantity + " seats");
    });
    
    }

    // shows reservation history
    public void showReservationHistory() {
        System.out.println("Reservation history:");
        reservationHistory.forEach(System.out::println);
    }

    // getter to retrieve reservations
    public Map<Cliente, List<Asiento>> getReservations() {
        return reservations;}
}
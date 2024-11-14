import java.util.*;

public class Estadio {
    private Set<Asiento> availableSeats = new HashSet<>();
    private Map<Cliente, List<Asiento>> reservations = new HashMap<>();
    private LinkedList<String> reservationHistory = new LinkedList<>();
    private Stack<String> actionsUndo = new Stack<>();
    private Map<String, Queue<Cliente>> waitList = new HashMap<>();

    public Estadio() {
        // initialize seats from estadio on each section
        initializeSeats("fieldlevel", 10, 50);
        initializeSeats("mainlevel", 20, 50);
        initializeSeats("grandstandlevel", 40, 50);

        // initialize listas de espera por seccion
        waitList.put("Field Level", new LinkedList<>());
        waitList.put("Main Level", new LinkedList<>());
        waitList.put("Grandstand Level", new LinkedList<>());
    }

    private void initializeSeats(String section, int rows, int seatsPerRow) {
        for (int r = 1; r <= rows; r++) {
            for (int a = 1; a <= seatsPerRow; a++) {
                availableSeats.add(new Asiento(section, r, a));
            }
        }
    }

    @SuppressWarnings("unused")
    public boolean reserveJointSeats(Cliente client, String section, int quantity) {
        // organizar available seats per row
        Map<Integer, List<Asiento>> seatsPerRow = new HashMap<>();
        for (Asiento seat : availableSeats) {
            if (seat.getSection().equals(section)) {
                seatsPerRow
                    .computeIfAbsent(seat.getRow(), k -> new ArrayList<>())
                    .add(seat);
            }
        }

        // buscar fila con enough seats together
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
                        reservationHistory.add(client.getName() + " reserved " + jointSeats);
                        actionsUndo.push("reserve");
                        System.out.println("Reservation succesful: " + client.getName() + " - " + jointSeats);
                        return true;
                    }
                } else {
                    jointSeats.clear();
                    jointSeats.add(currentSeat);
                }
            }
        }
        return false; // no se encontraron enough joint seats
    }

    public void addAwaitList(Cliente client, String section) {
        Queue<Cliente> wait = waitList.get(section);
        if (wait != null) {
            wait.add(client);
            reservationHistory.add(client.getName() + " added to the await list of " + section);
            actionsUndo.push("waitList");
            System.out.println("It has been added to the waitlist for the section " + section);
        } else {
            System.out.println("The specified section does not exist.");
        }
    }

    public void cancelReservation(Cliente client) {
        List<Asiento> seats = reservations.remove(client);
        if (seats != null) {
            availableSeats.addAll(seats);
            reservationHistory.add(client.getName() + " canceled " + seats);
            actionsUndo.push("cancelation");

            // verificar si hay alguien en la lista de wait para cada asiento liberado
            String section = seats.get(0).getSection();
            Queue<Cliente> wait = waitList.get(section);
            if (wait != null && !wait.isEmpty()) {
                Cliente next = wait.poll();
                // Intentar reservar los mismos seats para el next cliente
                boolean reserveSucceed = reserveJointSeats(next, section, seats.size());
                if (reserveSucceed) {
                    System.out.println("It has been automatically reserved for " + next.getName() + " from the waitlist.");
                } else {
                    // Si no se pueden reservar los mismos seats, volver a añadir a la lista de wait
                    addAwaitList(next, section);
                }
            }
        } else {
            System.out.println("No reservation was found for " + client.getName());
        }
    }

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

    public void showReservationHistory() {
        System.out.println("Reservation history:");
        reservationHistory.forEach(System.out::println);
    }

    public void undoLastAction() {
        if (actionsUndo.isEmpty()) {
            System.out.println("There are no actions to undo.");
            return;
        }
        String lastAction = actionsUndo.pop();
        if (lastAction.equals("reserve") && !reservationHistory.isEmpty()) {
            String lastReserved = reservationHistory.pollLast();
            // Aquí se debe parsear el historial para obtener el cliente
            // Esto requiere una implementación más detallada
            System.out.println("Undo last reservation: " + lastReserved);
            // Implementar la lógica de deshacer según las necesidades
        } else if (lastAction.equals("cancelation")) {
            // Lógica para deshacer una cancelación
            System.out.println("Undo last cancellation.");
            // Implementar la lógica de deshacer según las necesidades
        } else if (lastAction.equals("waitList")) {
            // Lógica para deshacer la adición a la lista de wait
            System.out.println("Undo last addition to the waitlist.");
            // Implementar la lógica de deshacer según las necesidades
        }
    }

    public Map<Cliente, List<Asiento>> getReservations() {
        return reservations;
    }
   
}

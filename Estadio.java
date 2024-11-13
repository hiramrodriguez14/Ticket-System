import java.util.*;

public class Estadio {
    private Set<Asiento> availableSeats = new HashSet<>();
    private Map<Cliente, List<Asiento>> reservations = new HashMap<>();
    private LinkedList<String> reservationHistory = new LinkedList<>();
    private Stack<String> actionsUndo = new Stack<>();
    private Map<String, Queue<Cliente>> waitList = new HashMap<>();

    public Estadio() {
        // initialize seats from estadio on each section
        initializeSeats("Field Level", 10, 50);
        initializeSeats("Main Level", 20, 50);
        initializeSeats("Grandstand Level", 40, 50);

        // initialize listas de espera por seccion
        waitList.put("Field Level", new LinkedList<>());
        waitList.put("Main Level", new LinkedList<>());
        waitList.put("Grandstand Level", new LinkedList<>());
    }

    private void initializeSeats(String section, int rows, int seatsPerRow) {
        for (int f = 1; f <= rows; f++) {
            for (int a = 1; a <= seatsPerRow; a++) {
                availableSeats.add(new Asiento(section, f, a));
            }
        }
    }

    public boolean reserveJointSeats(Cliente client, String section, int quantity) {
        // organizar available seats per row
        Map<Integer, List<Asiento>> seatsPerRow = new HashMap<>();
        for (Asiento seat : availableSeats) {
            if (seat.getsection().equals(section)) {
                seatsPerRow
                    .computeIfAbsent(seat.getFila(), k -> new ArrayList<>())
                    .add(seat);
            }
        }

        // buscar fila con enough seats together
        for (Map.Entry<Integer, List<Asiento>> entry : seatsPerRow.entrySet()) {
            List<Asiento> rowSeats = entry.getValue();
            rowSeats.sort(Comparator.comparingInt(Asiento::getNumero));

            List<Asiento> jointSeats = new ArrayList<>();
            for (int i = 0; i < rowSeats.size(); i++) {
                Asiento currentSeat = rowSeats.get(i);
                if (jointSeats.isEmpty() || currentSeat.getNumero() == jointSeats.get(jointSeats.size() - 1).getNumero() + 1) {
                    jointSeats.add(currentSeat);
                    if (jointSeats.size() == quantity) {
                        // reserve seats
                        availableSeats.removeAll(jointSeats);
                        reservations.put(client, new ArrayList<>(jointSeats));
                        reservationHistory.add(client.getName() + " reservó " + jointSeats);
                        actionsUndo.push("reserva");
                        System.out.println("Reservación exitosa: " + client.getName() + " - " + jointSeats);
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

    public void agregarAwaitList(Cliente client, String section) {
        Queue<Cliente> wait = waitList.get(section);
        if (wait != null) {
            wait.add(client);
            reservationHistory.add(client.getName() + " añadido a la lista de wait de " + section);
            actionsUndo.push("waitList");
            System.out.println("Se ha añadido a la lista de wait para la sección " + section);
        } else {
            System.out.println("La sección especificada no existe.");
        }
    }

    public void cancelReservation(Cliente client) {
        List<Asiento> seats = reservations.remove(client);
        if (seats != null) {
            availableSeats.addAll(seats);
            reservationHistory.add(client.getName() + " canceló " + seats);
            actionsUndo.push("cancelación");

            // verificar si hay alguien en la lista de wait para cada asiento liberado
            String section = seats.get(0).getsection();
            Queue<Cliente> wait = waitList.get(section);
            if (wait != null && !wait.isEmpty()) {
                Cliente next = wait.poll();
                // Intentar reservar los mismos seats para el next cliente
                boolean reserveSucceed = reserveJointSeats(next, section, seats.size());
                if (reserveSucceed) {
                    System.out.println("Se ha reservado automáticamente para " + next.getName() + " desde la lista de wait.");
                } else {
                    // Si no se pueden reservar los mismos seats, volver a añadir a la lista de wait
                    agregarAwaitList(next, section);
                }
            }
        } else {
            System.out.println("No se encontró una reservación para " + client.getName());
        }
    }

    public void veravailability() {
        System.out.println("seats disponibles por sección:");
        Map<String, Integer> availability = new HashMap<>();
        for (Asiento seat : availableSeats) {
            availability.put(seat.getsection(), availability.getOrDefault(seat.getsection(), 0) + 1);
        }
        availability.forEach((section, quantity) -> System.out.println(section + ": " + quantity + " seats"));
    }

    public void showReservationHistory() {
        System.out.println("Historial de reservations:");
        reservationHistory.forEach(System.out::println);
    }

    public void undoLastAction() {
        if (actionsUndo.isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }
        String lastAction = actionsUndo.pop();
        if (lastAction.equals("reserva") && !reservationHistory.isEmpty()) {
            String lastReserved = reservationHistory.pollLast();
            // Aquí se debe parsear el historial para obtener el cliente
            // Esto requiere una implementación más detallada
            System.out.println("Deshacer última reserva: " + lastReserved);
            // Implementar la lógica de deshacer según las necesidades
        } else if (lastAction.equals("cancelación")) {
            // Lógica para deshacer una cancelación
            System.out.println("Deshacer última cancelación.");
            // Implementar la lógica de deshacer según las necesidades
        } else if (lastAction.equals("waitList")) {
            // Lógica para deshacer la adición a la lista de wait
            System.out.println("Deshacer última adición a lista de wait.");
            // Implementar la lógica de deshacer según las necesidades
        }
    }

    public Map<Cliente, List<Asiento>> getreservations() {
        return reservations;
    }
}

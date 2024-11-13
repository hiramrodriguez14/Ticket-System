import java.util.*;

public class Estadio {
    private Set<Asiento> asientosDisponibles = new HashSet<>();
    private Map<Cliente, List<Asiento>> reservaciones = new HashMap<>();
    private LinkedList<String> historialReservas = new LinkedList<>();
    private Stack<String> accionesDeshacer = new Stack<>();
    private Map<String, Queue<Cliente>> listaEspera = new HashMap<>();

    public Estadio() {
        // Inicializar los asientos del estadio en cada sección
        inicializarAsientos("Field Level", 10, 50);
        inicializarAsientos("Main Level", 20, 50);
        inicializarAsientos("Grandstand Level", 40, 50);

        // Inicializar listas de espera por sección
        listaEspera.put("Field Level", new LinkedList<>());
        listaEspera.put("Main Level", new LinkedList<>());
        listaEspera.put("Grandstand Level", new LinkedList<>());
    }

    private void inicializarAsientos(String seccion, int filas, int asientosPorFila) {
        for (int f = 1; f <= filas; f++) {
            for (int a = 1; a <= asientosPorFila; a++) {
                asientosDisponibles.add(new Asiento(seccion, f, a));
            }
        }
    }

    public boolean reservarAsientosJuntos(Cliente cliente, String seccion, int cantidad) {
        // Organizar los asientos disponibles por fila
        Map<Integer, List<Asiento>> asientosPorFila = new HashMap<>();
        for (Asiento asiento : asientosDisponibles) {
            if (asiento.getSeccion().equals(seccion)) {
                asientosPorFila
                    .computeIfAbsent(asiento.getFila(), k -> new ArrayList<>())
                    .add(asiento);
            }
        }

        // Buscar una fila con suficientes asientos juntos
        for (Map.Entry<Integer, List<Asiento>> entry : asientosPorFila.entrySet()) {
            List<Asiento> filaAsientos = entry.getValue();
            filaAsientos.sort(Comparator.comparingInt(Asiento::getNumero));

            List<Asiento> asientosJuntos = new ArrayList<>();
            for (int i = 0; i < filaAsientos.size(); i++) {
                Asiento asientoActual = filaAsientos.get(i);
                if (asientosJuntos.isEmpty() || asientoActual.getNumero() == asientosJuntos.get(asientosJuntos.size() - 1).getNumero() + 1) {
                    asientosJuntos.add(asientoActual);
                    if (asientosJuntos.size() == cantidad) {
                        // Reservar los asientos
                        asientosDisponibles.removeAll(asientosJuntos);
                        reservaciones.put(cliente, new ArrayList<>(asientosJuntos));
                        historialReservas.add(cliente.getName() + " reservó " + asientosJuntos);
                        accionesDeshacer.push("reserva");
                        System.out.println("Reservación exitosa: " + cliente.getName() + " - " + asientosJuntos);
                        return true;
                    }
                } else {
                    asientosJuntos.clear();
                    asientosJuntos.add(asientoActual);
                }
            }
        }
        return false; // No se encontraron suficientes asientos juntos
    }

    public void agregarAListaEspera(Cliente cliente, String seccion) {
        Queue<Cliente> espera = listaEspera.get(seccion);
        if (espera != null) {
            espera.add(cliente);
            historialReservas.add(cliente.getName() + " añadido a la lista de espera de " + seccion);
            accionesDeshacer.push("listaEspera");
            System.out.println("Se ha añadido a la lista de espera para la sección " + seccion);
        } else {
            System.out.println("La sección especificada no existe.");
        }
    }

    public void cancelarReservacion(Cliente cliente) {
        List<Asiento> asientos = reservaciones.remove(cliente);
        if (asientos != null) {
            asientosDisponibles.addAll(asientos);
            historialReservas.add(cliente.getName() + " canceló " + asientos);
            accionesDeshacer.push("cancelación");

            // Verificar si hay alguien en la lista de espera para cada asiento liberado
            String seccion = asientos.get(0).getSeccion();
            Queue<Cliente> espera = listaEspera.get(seccion);
            if (espera != null && !espera.isEmpty()) {
                Cliente siguiente = espera.poll();
                // Intentar reservar los mismos asientos para el siguiente cliente
                boolean reservaExitosa = reservarAsientosJuntos(siguiente, seccion, asientos.size());
                if (reservaExitosa) {
                    System.out.println("Se ha reservado automáticamente para " + siguiente.getName() + " desde la lista de espera.");
                } else {
                    // Si no se pueden reservar los mismos asientos, volver a añadir a la lista de espera
                    agregarAListaEspera(siguiente, seccion);
                }
            }
        } else {
            System.out.println("No se encontró una reservación para " + cliente.getName());
        }
    }

    public void verDisponibilidad() {
        System.out.println("Asientos disponibles por sección:");
        Map<String, Integer> disponibilidad = new HashMap<>();
        for (Asiento asiento : asientosDisponibles) {
            disponibilidad.put(asiento.getSeccion(), disponibilidad.getOrDefault(asiento.getSeccion(), 0) + 1);
        }
        disponibilidad.forEach((seccion, cantidad) -> System.out.println(seccion + ": " + cantidad + " asientos"));
    }

    public void mostrarHistorialReservas() {
        System.out.println("Historial de reservaciones:");
        historialReservas.forEach(System.out::println);
    }

    public void deshacerUltimaAccion() {
        if (accionesDeshacer.isEmpty()) {
            System.out.println("No hay acciones para deshacer.");
            return;
        }
        String ultimaAccion = accionesDeshacer.pop();
        if (ultimaAccion.equals("reserva") && !historialReservas.isEmpty()) {
            String ultimaReserva = historialReservas.pollLast();
            // Aquí se debe parsear el historial para obtener el cliente
            // Esto requiere una implementación más detallada
            System.out.println("Deshacer última reserva: " + ultimaReserva);
            // Implementar la lógica de deshacer según las necesidades
        } else if (ultimaAccion.equals("cancelación")) {
            // Lógica para deshacer una cancelación
            System.out.println("Deshacer última cancelación.");
            // Implementar la lógica de deshacer según las necesidades
        } else if (ultimaAccion.equals("listaEspera")) {
            // Lógica para deshacer la adición a la lista de espera
            System.out.println("Deshacer última adición a lista de espera.");
            // Implementar la lógica de deshacer según las necesidades
        }
    }

    public Map<Cliente, List<Asiento>> getReservaciones() {
        return reservaciones;
    }
}

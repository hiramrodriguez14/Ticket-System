import static org.junit.Assert.assertEquals;

import org.junit.*;
public class EstadioTest {
    Estadio stadium = new Estadio();
    Cliente client1 = new Cliente("John Doe", "johndoe@gmail.com", "787-123-4567");
    Cliente client2 = new Cliente("Naruto Uzumaki", "uzumaki@upr.edu", "787-133-4567");
    Cliente client3 = new Cliente("Sasuke Uchiha", "uchiha@upr.edu", "787-143-4567");
  @Test
    public void testReserveJointSeats() {
       // noticed that if the user request a SeatNumber > 50 reserveJoinSeat is going to return false because it wont find a seats together because the user requestes a number greater that seatsPerRow
       //a solution would be to put a limit of seats to the user
        // Pruebas de reservación en diferentes niveles
        assertEquals(true, stadium.reserveJointSeats(client1, "fieldlevel", 49));
        assertEquals(true, stadium.reserveJointSeats(client2, "fieldlevel", 2));
        assertEquals(false, stadium.reserveJointSeats(client3, "fieldlevel", 400));

        assertEquals(true, stadium.reserveJointSeats(client1, "mainlevel", 100));
        assertEquals(true, stadium.reserveJointSeats(client2, "mainlevel", 500));
        assertEquals(false, stadium.reserveJointSeats(client3, "mainlevel", 500));

        assertEquals(true, stadium.reserveJointSeats(client1, "grandstandlevel", 1000));
        assertEquals(true, stadium.reserveJointSeats(client2, "grandstandlevel", 500));
        assertEquals(false, stadium.reserveJointSeats(client3, "grandstandlevel", 501));
    }
}

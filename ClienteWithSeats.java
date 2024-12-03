/**
 * The ClienteWithSeats class extends the Cliente class to include
 * the number of seats that the customer wants to reserve.
 */
public class ClienteWithSeats extends Cliente {

    // Private field to store the number of seats the customer wants to reserve
    private int requestedSeats;

    /**
     * Constructs a new ClienteWithSeats object with the specified name, email, phone number, and requested seats.
     *
     * @param name          the name of the customer
     * @param email         the email address of the customer
     * @param phoneNum      the phone number of the customer
     * @param requestedSeats the number of seats the customer wants to reserve
     */
    public ClienteWithSeats(String name, String email, String phoneNum, int requestedSeats) {
        super(name, email, phoneNum); // Call the constructor of the parent class
        this.requestedSeats = requestedSeats; // Initialize the requested seats
    }

    /**
     * Returns the number of seats the customer wants to reserve.
     *
     * @return the number of requested seats
     */
    public int getRequestedSeats() {
        return requestedSeats;
    }

    /**
     * Sets the number of seats the customer wants to reserve.
     *
     * @param requestedSeats the number of seats to set
     */
    public void setRequestedSeats(int requestedSeats) {
        this.requestedSeats = requestedSeats;
    }

    /**
     * Compares this ClienteWithSeats object to another for equality.
     * Two ClienteWithSeats objects are considered equal if they have the same name, email, phone number, and requested seats.
     *
     * @param obj the object to compare to
     * @return true if the ClienteWithSeats objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false; // Check parent class equality first
        }
        if (obj instanceof ClienteWithSeats) {
            ClienteWithSeats other = (ClienteWithSeats) obj; // Cast to ClienteWithSeats
            return this.requestedSeats == other.requestedSeats; // Compare requested seats
        }
        return false; // Not the same type
    }

    /**
     * Returns the hash code for the ClienteWithSeats object.
     * The hash code is based on the parent's hash code and the requested seats.
     *
     * @return the hash code of the object
     */
    @Override
    public int hashCode() {
        return super.hashCode() + Integer.hashCode(requestedSeats); // Combine parent's hash code with requested seats
    }
}

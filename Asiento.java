/**
 * The Asiento class represents a seat in a stadium. 
 * Each seat has a section, row, and seat number to uniquely identify it.
 */
public class Asiento {

    // Private fields to store the section, row and seat number of seats.  
    private String section;
    private int row;
    private int seatNumber;


    /**
     * Constructs a new Asiento object with the specified section, row, and seat number.
     *
     * @param section    the section where the seat is located
     * @param row        the row number of the seat
     * @param seatNumber the seat number within the row
     */
    public Asiento(String section, int row, int seatNumber) {
        this.section = section;
        this.row = row;
        this.seatNumber = seatNumber;
    }

    /** 
     * Returns the section where the seat is located. 
     * @return the section as a     String 
     */
    public String getSection() {
        return section;
    }

    /**
     * Returns the row number of seats. 
     * @return the row number as an integer.  
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the seat number withing the row. 
     * @return the seat number as an integer.  
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the section for the seat.
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section = section; 
    }

    /**
     * Sets the row number for the seat.
     * @param row the row number to set
     */
    public void setRow(int row) {
        this.row = row; 
    }

    /**
     * Sets the seat number within the row.
     * @param seatNumber the seat number to set
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber; 
    }

    /**
     * Returns a string representation of the seat.
     * The format includes the section, row, and seat number.
     * @return a string representing the seat with all its parts. 
     */
    @Override
    public String toString() {
        return "Section: " + section + ", Row: " + row + ", SeatNumber: " + seatNumber;
    }

    /**
     * Compares this seat to another seat for equality.
     * Two seats are considered equal if they have the same section, row, and seat number.
     * @param asiento the seat to compare to
     * @return true if the seats are equal, false otherwise
     */
    public boolean equals(Asiento asiento) {
        return (this.getSection().equals(asiento.getSection())) && (this.getRow() == asiento.getRow()) && (this.getSeatNumber() == asiento.getSeatNumber());
    }

    /**
     * Returns a hash code value for the seat.
     * The hash code is based on the section, row, and seat number.
     * @return the hash code of the seat
     */
    @Override
    public int hashCode() {
        int result = 0; 
        
        if (section != null) {
            result = section.hashCode();
        }
        result = 31 * result + row;
        result = 31 * result + seatNumber;
        return result;
    }
}

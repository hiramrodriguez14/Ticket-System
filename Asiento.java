public class Asiento {
    private String section;
    private int row;
    private int seatNumber;

    public Asiento(String section, int row, int seatNumber) {
        this.section = section;
        this.row = row;
        this.seatNumber = seatNumber;
    }

    public String getSection() {
        return section;
    }

    public int getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSection(String section) {
        this.section = section; 
    }

    public void setRow(int row) {
        this.row = row; 
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber; 
    }

    @Override
    public String toString() {
        return "Section: " + section + ", Row: " + row + ", SeatNumber: " + seatNumber;
    }

    public boolean equals(Asiento asiento) {
        if((this.getSection() == asiento.getSection()) && (this.getRow() == asiento.getRow()) && (this.getSeatNumber() == asiento.getSeatNumber())) {
            return true;
        }
        return false;
    }

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

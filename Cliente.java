
/**
 * The Cliente class represents a customer with personal details such as name, email, and phone number.
 * It provides methods to access and modify these details, as well as to compare Cliente objects for equality purposes.
 */
public class Cliente {

    // Private fields to store customer details
    private String name;
    private String email;
    private String phoneNum;

    /**
     * Constructs a new Cliente object with the specified name, email, and phone number.
     *
     * @param name     the name of the customer
     * @param email    the email address of the customer
     * @param phoneNum the phone number of the customer
     */
    public Cliente(String name, String email, String phoneNum) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    /**
     * Returns the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {return name;}

    /**
     * Sets the name of the customer.
     *
     * @param name the name to set
     */
    public void setName(String name) {this.name = name;}

    /**
     * Returns the email address of the customer.
     *
     * @return the email address of the customer
     */
    public String getEmail() {return email;}

    /**
     * Sets the email address of the customer.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Returns the phone number of the customer.
     *
     * @return the phone number of the customer
     */
    public String getPhoneNum() {return phoneNum;}

    /**
     * Sets the phone number of the customer.
     *
     * @param phoneNum the phone number to set
     */
    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

 
    
    /**
     * Compares this Cliente object to another for equality.
     * Two Cliente objects are considered equal if they have the same name, email, and phone number.
     *
     * @param cliente the Cliente object to compare to
     * @return true if the Cliente objects are equal, false otherwise
     */
   
     @Override
     public boolean equals(Object obj) {
         if (this == obj) {
             return true; // Same reference, they are equal
         }
         if (obj == null || getClass() != obj.getClass()) {
             return false; // Null or different class, not equal
         }
         Cliente cliente = (Cliente) obj; // Cast to Cliente
         // Compare relevant fields
         return this.email.equals(cliente.email) &&
                this.name.equals(cliente.name) &&
                this.phoneNum.equals(cliente.phoneNum);
     }
     
    /**
     * Returns the hash code for the Cliente object.
     * The hash code is based on the email address of the customer.
     *
     * @return the hash code of the email, or 0 if the email is null
     */
    @Override
    public int hashCode() {
        if(email!=null){
            return email.hashCode(); //Returns the hashCode of the email string
        }else{
            return 0;
        }
    }
}

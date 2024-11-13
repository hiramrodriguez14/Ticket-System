import java.util.ArrayList;

public class Cliente {

    // private stuff to use for Cliente
    private String name;
    private String email;
    private int phoneNum;

    // constructor
    public  Cliente(String name, String email, int phoneNum) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // getters and setters
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public int getPhoneNum() {return phoneNum;}

    public void setPhoneNum(int phoneNum) {this.phoneNum = phoneNum;}

    // main method
    public static void main(String[] args) {

        
        ArrayList<Cliente> f = new ArrayList<>();

        Cliente fernando = new Cliente("Fernando", "fer.cas@upr.edu", 787);
        f.add(fernando);
        
        for(Cliente c : f) {
            System.out.println(c.getName());
            System.out.println(c.getEmail());
            System.out.println(c.getPhoneNum());
        }
    }
}

import java.util.ArrayList;


public class Cliente {

    // private stuff to use for Cliente
    private String name;
    private String email;
    private String phoneNum;

    // constructor
    public  Cliente(String name, String email, String phoneNum) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // getters and setters
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhoneNum() {return phoneNum;}

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

 
    public boolean equals(Cliente cliente) {
       if((this.getEmail()==cliente.getEmail())&&(this.getName()==cliente.getName())&&(this.getPhoneNum()==cliente.getPhoneNum())){
           return true;
    }
    return false;
    }

    @Override
    public int hashCode() {
        if(email!=null){
            return email.hashCode(); //Returns the hashCode of the email string
        }else{
            return 0;
        }
    }
    
    // main method
    public static void main(String[] args) {

        
        ArrayList<Cliente> f = new ArrayList<>();

        Cliente fernando = new Cliente("Fernando", "fer.cas@upr.edu", "787");
        f.add(fernando);
        
        for(Cliente c : f) {
            System.out.println(c.getName());
            System.out.println(c.getEmail());
            System.out.println(c.getPhoneNum());
        }
    }
}

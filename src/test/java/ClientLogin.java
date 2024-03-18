public class ClientLogin {
    private String email;
    private String password;
    public ClientLogin(String email, String password){
        this.email = email;
        this.password = password;
    }
    public ClientLogin(){
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}

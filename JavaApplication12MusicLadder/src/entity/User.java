package entity;

public class User {

    private String username;
    private String password;
    private int userLevel;

    public User( String username, String password ) {
        this.username = username;
        this.password = password;
    }

    public User( String username, int userLevel ) {
        this.username = username;
        this.userLevel = userLevel;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + '}';
    }

}

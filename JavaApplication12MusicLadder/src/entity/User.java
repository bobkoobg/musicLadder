package entity;

public class User {

    private int userId;
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

    public User() {
    }

    public void setUserId( int userId ) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
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

    public void setUserLevel( int userLevel ) {
        this.userLevel = userLevel;
    }

    public int getUserLevel() {
        return userLevel;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", password="
                + password + ", userLevel=" + userLevel + '}';
    }

}

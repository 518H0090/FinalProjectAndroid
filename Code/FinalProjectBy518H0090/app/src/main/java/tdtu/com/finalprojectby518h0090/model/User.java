package tdtu.com.finalprojectby518h0090.model;

public class User {
    private String userKey;
    private String userEmail;
    private String userPassword;
    private String userPermission;

    public User() {
    }

    public User(String userKey, String userEmail, String userPassword, String userPermission) {
        this.userKey = userKey;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPermission = userPermission;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }
}

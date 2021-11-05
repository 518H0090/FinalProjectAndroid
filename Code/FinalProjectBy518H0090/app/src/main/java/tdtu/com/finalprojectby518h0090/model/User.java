package tdtu.com.finalprojectby518h0090.model;

import java.io.Serializable;

public class User implements Serializable {
    private String userKey;
    private String userFullname;
    private String userBirth;
    private long userPhone;
    private String userAddress;
    private String userEmail;

    public User() {
    }

    public User(String userKey, String userFullname, String userBirth, long userPhone, String userAddress, String userEmail) {
        this.userKey = userKey;
        this.userFullname = userFullname;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userEmail = userEmail;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

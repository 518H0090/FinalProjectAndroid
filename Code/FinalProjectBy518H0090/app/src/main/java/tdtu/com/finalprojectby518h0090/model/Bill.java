package tdtu.com.finalprojectby518h0090.model;

public class Bill {

    private String billKey;
    private String tableName;
    private String userEmail;
    private int totalPrice;
    private String dateTime;

    public Bill() {
    }

    public Bill(String billKey, String tableName, String userEmail, int totalPrice, String dateTime) {
        this.billKey = billKey;
        this.tableName = tableName;
        this.userEmail = userEmail;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
    }

    public String getBillKey() {
        return billKey;
    }

    public void setBillKey(String billKey) {
        this.billKey = billKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

package tdtu.com.finalprojectby518h0090.model;

public class Customer {
    private String customerKey;
    private String customerName;
    private String customerAddress;
    private int customerPhone;

    public Customer() {
    }

    public Customer(String customerKey, String customerName, String customerAddress, int customerPhone) {
        this.customerKey = customerKey;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(int customerPhone) {
        this.customerPhone = customerPhone;
    }
}

package tdtu.com.testproductcart.model;

public class CartDrink {
    String drinkKey , drinkName;
    int drinkPrice;
    private int quantity;
    private int totalPrice;

    public CartDrink() {
    }

    public CartDrink(String drinkKey, String drinkName, int drinkPrice, int quantity, int totalPrice) {
        this.drinkKey = drinkKey;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getDrinkKey() {
        return drinkKey;
    }

    public void setDrinkKey(String drinkKey) {
        this.drinkKey = drinkKey;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public int getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(int drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}

package tdtu.com.testproductcart.model;

public class MenuDrink {
    String drinkKey , drinkName;
    int drinkPrice;

    public MenuDrink() {
    }

    public MenuDrink(String drinkKey, String drinkName, int drinkPrice) {
        this.drinkKey = drinkKey;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
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
}

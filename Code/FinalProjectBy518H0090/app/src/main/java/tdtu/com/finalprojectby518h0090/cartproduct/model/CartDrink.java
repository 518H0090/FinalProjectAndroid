package tdtu.com.finalprojectby518h0090.cartproduct.model;

public class CartDrink {
    private String menuDrinkKey;
    private String tagDrink;
    private String nameDrink;
    private String imageDrink;
    private int priceDrink;

    private int quantity;
    private int totalPrice;

    public CartDrink() {
    }

    public CartDrink(String menuDrinkKey, String tagDrink, String nameDrink, String imageDrink, int priceDrink, int quantity, int totalPrice) {
        this.menuDrinkKey = menuDrinkKey;
        this.tagDrink = tagDrink;
        this.nameDrink = nameDrink;
        this.imageDrink = imageDrink;
        this.priceDrink = priceDrink;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getMenuDrinkKey() {
        return menuDrinkKey;
    }

    public void setMenuDrinkKey(String menuDrinkKey) {
        this.menuDrinkKey = menuDrinkKey;
    }

    public String getTagDrink() {
        return tagDrink;
    }

    public void setTagDrink(String tagDrink) {
        this.tagDrink = tagDrink;
    }

    public String getNameDrink() {
        return nameDrink;
    }

    public void setNameDrink(String nameDrink) {
        this.nameDrink = nameDrink;
    }

    public String getImageDrink() {
        return imageDrink;
    }

    public void setImageDrink(String imageDrink) {
        this.imageDrink = imageDrink;
    }

    public int getPriceDrink() {
        return priceDrink;
    }

    public void setPriceDrink(int priceDrink) {
        this.priceDrink = priceDrink;
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

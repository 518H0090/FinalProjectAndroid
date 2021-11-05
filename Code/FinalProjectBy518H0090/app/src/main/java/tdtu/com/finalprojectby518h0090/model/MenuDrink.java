package tdtu.com.finalprojectby518h0090.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MenuDrink {

    private String menuDrinkKey;
    private String tagDrink;
    private String nameDrink;
    private String imageDrink;
    private int priceDrink;

    public MenuDrink() {
    }

    public MenuDrink(String menuDrinkKey, String tagDrink, String nameDrink, String imageDrink, int priceDrink) {
        this.menuDrinkKey = menuDrinkKey;
        this.tagDrink = tagDrink;
        this.nameDrink = nameDrink;
        this.imageDrink = imageDrink;
        this.priceDrink = priceDrink;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("menuDrinkKey", menuDrinkKey);
        result.put("tagDrink", tagDrink);
        result.put("nameDrink", nameDrink);
        result.put("imageDrink", imageDrink);
        result.put("priceDrink", priceDrink);

        return result;
    }
}

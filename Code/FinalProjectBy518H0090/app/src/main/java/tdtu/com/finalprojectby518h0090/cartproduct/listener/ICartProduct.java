package tdtu.com.finalprojectby518h0090.cartproduct.listener;

import java.util.List;

import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public interface ICartProduct {
    void addToCartMenuDrink(MenuDrink menuDrink);
    void countQuantityListInCart(List<CartDrink> cartDrinks);
}

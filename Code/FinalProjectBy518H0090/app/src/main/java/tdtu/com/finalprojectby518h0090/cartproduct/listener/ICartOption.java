package tdtu.com.finalprojectby518h0090.cartproduct.listener;

import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;

public interface ICartOption {
    void onClickPlusItem(CartDrink cartDrink);
    void onClickSubtractItem(CartDrink cartDrink);
    void onClickDeleteItem(CartDrink cartDrink);
}

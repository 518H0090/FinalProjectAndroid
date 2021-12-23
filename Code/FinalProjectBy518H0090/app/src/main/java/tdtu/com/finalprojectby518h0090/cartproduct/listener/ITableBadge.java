package tdtu.com.finalprojectby518h0090.cartproduct.listener;

import java.util.List;

import tdtu.com.finalprojectby518h0090.cartproduct.adapter.ShowTableAdapter;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;

public interface ITableBadge {
    void getCountList(List<CartDrink> cartDrinks, ShowTableAdapter.ShowTableViewHolder holder);
}

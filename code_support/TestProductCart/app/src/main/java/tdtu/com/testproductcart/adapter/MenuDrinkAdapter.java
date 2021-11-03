package tdtu.com.testproductcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.com.testproductcart.R;
import tdtu.com.testproductcart.listener.AddToCartLis;
import tdtu.com.testproductcart.model.MenuDrink;

public class MenuDrinkAdapter extends RecyclerView.Adapter<MenuDrinkAdapter.MenuDrinkViewHolder> {

    private Context context;
    private List<MenuDrink> list;
    private AddToCartLis cartLis;

    public MenuDrinkAdapter(Context context, List<MenuDrink> list) {
        this.context = context;
        this.list = list;
    }

    public void setCartLis(AddToCartLis cartLis) {
        this.cartLis = cartLis;
    }

    @NonNull
    @Override
    public MenuDrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu_drink, parent, false);
        return new MenuDrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDrinkViewHolder holder, int position) {
        MenuDrink menuDrink = list.get(position);
        if (menuDrink == null) {
            return;
        }

        holder.textNameDrink.setText(menuDrink.getDrinkName());
        holder.textPriceDrink.setText(String.valueOf(menuDrink.getDrinkPrice()));
        holder.textAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartLis.addTocart(menuDrink);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class MenuDrinkViewHolder extends RecyclerView.ViewHolder {

        TextView textNameDrink, textPriceDrink;
        TextView textAddToCart;

        public MenuDrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            textNameDrink = itemView.findViewById(R.id.textNameDrink);
            textPriceDrink = itemView.findViewById(R.id.textPriceDrink);
            textAddToCart = itemView.findViewById(R.id.textAddToCart);

        }
    }
}

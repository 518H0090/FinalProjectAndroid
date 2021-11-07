package tdtu.com.finalprojectby518h0090.cartproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartOption;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;

public class ShowCartAdapter extends RecyclerView.Adapter<ShowCartAdapter.ShowCartViewHolder> {

    private Context context;
    private List<CartDrink> cartDrinks;
    private ICartOption iCartOption;

    public ShowCartAdapter(Context context, List<CartDrink> cartDrinks) {
        this.context = context;
        this.cartDrinks = cartDrinks;
    }

    public void setiCartOption(ICartOption iCartOption) {
        this.iCartOption = iCartOption;
    }

    @NonNull
    @Override
    public ShowCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cart_view, parent, false);
        return new ShowCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowCartViewHolder holder, int position) {
        CartDrink cartDrink = cartDrinks.get(position);
        if (cartDrink == null) {
            return;
        }

        Glide.with(context).load(cartDrink.getImageDrink()).error(R.drawable.imagedefault).into(holder.image_cart_drink);
        holder.name_cart_drink.setText(cartDrink.getNameDrink());
        holder.price_cart_dink.setText(String.valueOf(cartDrink.getPriceDrink()));
        holder.quantity_cart_drink.setText(String.valueOf(cartDrink.getQuantity()));
        holder.quantity_cart_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCartOption.onClickPlusItem(cartDrink);
            }
        });

        holder.quantity_cart_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCartOption.onClickSubtractItem(cartDrink);
            }
        });

        holder.cart_delete_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCartOption.onClickDeleteItem(cartDrink);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartDrinks.size();
    }

    public class ShowCartViewHolder extends RecyclerView.ViewHolder {

        ImageView image_cart_drink, cart_delete_drink;
        TextView name_cart_drink, price_cart_dink, quantity_cart_plus , quantity_cart_drink, quantity_cart_subtract;

        public ShowCartViewHolder(@NonNull View itemView) {
            super(itemView);
            image_cart_drink = itemView.findViewById(R.id.image_cart_drink);
            cart_delete_drink = itemView.findViewById(R.id.cart_delete_drink);
            name_cart_drink = itemView.findViewById(R.id.name_cart_drink);
            price_cart_dink = itemView.findViewById(R.id.price_cart_dink);
            quantity_cart_plus = itemView.findViewById(R.id.quantity_cart_plus);
            quantity_cart_drink = itemView.findViewById(R.id.quantity_cart_drink);
            quantity_cart_subtract = itemView.findViewById(R.id.quantity_cart_subtract);
        }
    }
}

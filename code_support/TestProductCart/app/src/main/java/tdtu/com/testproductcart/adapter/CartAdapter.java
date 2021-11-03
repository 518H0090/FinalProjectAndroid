package tdtu.com.testproductcart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import tdtu.com.testproductcart.R;
import tdtu.com.testproductcart.model.CartDrink;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartDrink> list;

    public CartAdapter(Context context, List<CartDrink> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_product_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartDrink cartDrink = list.get(holder.getAdapterPosition());
        if (cartDrink == null) {
            return;
        }

        holder.textName.setText(cartDrink.getDrinkName());
        holder.textPrice.setText(String.valueOf(cartDrink.getDrinkPrice()));
        holder.textQuantity.setText(String.valueOf(cartDrink.getQuantity()));
        holder.textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncreaseQuantity(holder, cartDrink);
                notifyDataSetChanged();
            }
        });

        holder.textSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecreaseQuantity(holder, cartDrink);
                notifyDataSetChanged();
            }
        });

        holder.textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(cartDrink.getDrinkKey()).removeValue();
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

    }

    private void DecreaseQuantity(CartViewHolder holder, CartDrink cartDrink) {
        if (cartDrink.getQuantity() > 1) {
            cartDrink.setQuantity(cartDrink.getQuantity()-1);
            cartDrink.setTotalPrice(cartDrink.getDrinkPrice()*cartDrink.getQuantity());
        }

        holder.textQuantity.setText(cartDrink.getQuantity()+"");
        updateFirebase(cartDrink);
    }

    private void IncreaseQuantity(CartViewHolder holder, CartDrink cartDrink) {
        cartDrink.setQuantity(cartDrink.getQuantity()+1);
        cartDrink.setTotalPrice(cartDrink.getDrinkPrice()*cartDrink.getQuantity());

        holder.textQuantity.setText(cartDrink.getQuantity()+"");
        updateFirebase(cartDrink);
    }

    private void updateFirebase(CartDrink cartDrink) {
        FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(cartDrink.getDrinkKey()).setValue(cartDrink);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textPrice, textQuantity, textAdd, textSubtract, textDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textAdd = itemView.findViewById(R.id.textAdd);
            textSubtract = itemView.findViewById(R.id.textSubtract);
            textDelete = itemView.findViewById(R.id.textDelete);
        }
    }
}

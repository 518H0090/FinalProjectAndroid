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
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ICartProduct;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public class ShowMenuAdapter extends RecyclerView.Adapter<ShowMenuAdapter.ShowMenuViewHolder>{

    private Context context;
    private List<MenuDrink> list;
    private ICartProduct iCartProduct;

    public ShowMenuAdapter(Context context, List<MenuDrink> list) {
        this.context = context;
        this.list = list;
    }

    public void setiCartProduct(ICartProduct iCartProduct) {
        this.iCartProduct = iCartProduct;
    }

    @NonNull
    @Override
    public ShowMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu_cart, parent, false);
        return new ShowMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMenuViewHolder holder, int position) {
        MenuDrink menuDrink = list.get(position);
        if (menuDrink == null) {
            return;
        }

        Glide.with(context).load(menuDrink.getImageDrink()).error(R.drawable.imagedefault).into(holder.menuCartImage);
        holder.textMenuName.setText(menuDrink.getNameDrink());
        holder.textMenuPrice.setText(String.valueOf(menuDrink.getPriceDrink()));
        holder.textAddtoProductCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCartProduct.addToCartMenuDrink(menuDrink);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShowMenuViewHolder extends RecyclerView.ViewHolder {

        ImageView menuCartImage;
        TextView textMenuName, textMenuPrice, textAddtoProductCart;

        public ShowMenuViewHolder(@NonNull View itemView) {
            super(itemView);

            menuCartImage = itemView.findViewById(R.id.menuCartImage);
            textMenuName = itemView.findViewById(R.id.textMenuName);
            textMenuPrice = itemView.findViewById(R.id.textMenuPrice);
            textAddtoProductCart = itemView.findViewById(R.id.textAddtoProductCart);
        }
    }
}

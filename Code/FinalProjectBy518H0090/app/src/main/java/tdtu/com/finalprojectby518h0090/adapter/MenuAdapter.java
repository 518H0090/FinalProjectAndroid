package tdtu.com.finalprojectby518h0090.adapter;

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
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuDrink> list;

    public MenuAdapter() {
    }

    public MenuAdapter(Context context, List<MenuDrink> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<MenuDrink> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuDrink menuDrink = list.get(position);
        if (menuDrink != null) {
            return;
        }

        holder.textNameDrink.setText(menuDrink.getNameDrink());
        holder.textPriceDrink.setText(String.valueOf(menuDrink.getPriceDrink()));
        Glide.with(context).load(menuDrink.getImageDrink()).error(R.drawable.imagedefault).into(holder.imageViewMenu);
    }

    @Override
    public int getItemCount() {
        if (list != null ){
            return list.size();
        }
        return 0;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMenu;
        TextView textNameDrink, textPriceDrink;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);
            textNameDrink = itemView.findViewById(R.id.textNameDrink);
            textPriceDrink = itemView.findViewById(R.id.textPriceDrink);

        }
    }

}

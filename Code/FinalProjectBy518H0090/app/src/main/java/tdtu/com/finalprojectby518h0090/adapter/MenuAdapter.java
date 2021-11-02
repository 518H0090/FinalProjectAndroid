package tdtu.com.finalprojectby518h0090.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.MenuSelectionOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> implements Filterable {

    private Context context;
    private List<MenuDrink> list;
    private List<MenuDrink> oldList;
    private MenuSelectionOption menuSelectionOption;

    public MenuAdapter(Context context, List<MenuDrink> list) {
        this.context = context;
        this.list = list;
        this.oldList = list;
    }

    public void setMenuSelectionOption(MenuSelectionOption menuSelectionOption) {
        this.menuSelectionOption = menuSelectionOption;
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
        if (menuDrink == null) {
            return;
        }

        holder.textNameDrink.setText(menuDrink.getNameDrink());
        holder.textPriceDrink.setText(menuDrink.getPriceDrink() + "");
        Glide.with(context).load(menuDrink.getImageDrink()).error(R.drawable.imagedefault).into(holder.imageViewMenu);
        holder.btnActionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_menu_change:
                                menuSelectionOption.onClickEdit(menuDrink);
                                break;

                            case R.id.menu_menu_delete:
                                menuSelectionOption.onClickDelete(menuDrink);
                                break;
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMenu;
        TextView textNameDrink, textPriceDrink;
        Button btnActionMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewMenu = itemView.findViewById(R.id.imageViewMenu);
            textNameDrink = itemView.findViewById(R.id.textNameDrink);
            textPriceDrink = itemView.findViewById(R.id.textPriceDrink);
            btnActionMenu = itemView.findViewById(R.id.btnActionMenu);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String stringSearch = constraint.toString();
                if (stringSearch.isEmpty()) {
                    list = oldList;
                } else {
                    List<MenuDrink> newList = new ArrayList<>();
                    for (MenuDrink menuDrink : oldList) {
                        if (menuDrink.getNameDrink().toLowerCase().contains(stringSearch.toLowerCase())){
                            newList.add(menuDrink);
                        }
                    }
                    list = newList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<MenuDrink>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

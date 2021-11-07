package tdtu.com.finalprojectby518h0090.cartproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.IMenuChange;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.ITableBadge;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;
import tdtu.com.finalprojectby518h0090.model.Table;

public class ShowTableAdapter extends RecyclerView.Adapter<ShowTableAdapter.ShowTableViewHolder> implements ITableBadge, Filterable {

    private Context context;
    private List<Table> list;
    private List<Table> oldList;
    private IMenuChange menuChange;
    private ITableBadge iTableBadge;

    public ShowTableAdapter(Context context, List<Table> list) {
        this.context = context;
        this.list = list;
        this.oldList = list;
    }

    public void setMenuChange(IMenuChange menuChange) {
        this.menuChange = menuChange;
    }

    @NonNull
    @Override
    public ShowTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_table_view_show , parent , false);
        return new ShowTableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowTableViewHolder holder, int position) {
        Table table = list.get(position);
        if (table == null) {
            return;
        }

        holder.tTableName.setText(table.getTableName());
        holder.linearLayoutTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuChange.getTable(table);
            }
        });

        setCountBadge(table, holder);
        iTableBadge = this;
    }

    private void setCountBadge(Table table, ShowTableViewHolder holder) {
        FirebaseDatabase.getInstance().getReference("cart").child(table.getTableName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CartDrink> cartDrinkList = new ArrayList<>();
                for (DataSnapshot cartSnap : snapshot.getChildren()) {
                    CartDrink cartDrink = cartSnap.getValue(CartDrink.class);
                    cartDrinkList.add(cartDrink);
                }
                iTableBadge.getCountList(cartDrinkList,holder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getCountList(List<CartDrink> cartDrinks, ShowTableViewHolder holder) {
        int count = 0;
        for (CartDrink cartDrink : cartDrinks) {
            count += cartDrink.getQuantity();
        }
        holder.badge.setNumber(count);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShowTableViewHolder extends RecyclerView.ViewHolder {

        FrameLayout productCart;
        TextView tTableName;
        NotificationBadge badge;
        RelativeLayout linearLayoutTable;

        public ShowTableViewHolder(@NonNull View itemView) {
            super(itemView);

            productCart = itemView.findViewById(R.id.productCart);
            tTableName = itemView.findViewById(R.id.tTableName);
            badge = itemView.findViewById(R.id.badge);
            linearLayoutTable = itemView.findViewById(R.id.linearLayoutTable);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString();
                if (searchString.isEmpty()) {
                    list = oldList;
                } else {
                    List<Table> newList = new ArrayList<>();
                    for (Table table : oldList) {
                        if (table.getTableName().toLowerCase().contains(searchString.toLowerCase())){
                            newList.add(table);
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
                list = (List<Table>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

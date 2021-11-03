package tdtu.com.finalprojectby518h0090.cartproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.IMenuChange;
import tdtu.com.finalprojectby518h0090.model.Table;

public class ShowTableAdapter extends RecyclerView.Adapter<ShowTableAdapter.ShowTableViewHolder> {

    private Context context;
    private List<Table> list;
    private IMenuChange menuChange;

    public ShowTableAdapter(Context context, List<Table> list) {
        this.context = context;
        this.list = list;
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

        holder.tCustomer.setText(table.getCustomerName());
        holder.tTableName.setText(table.getTableName());
        holder.btnShowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuChange.getTable(table);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShowTableViewHolder extends RecyclerView.ViewHolder {

        TextView tCustomer;
        FrameLayout productCart;
        ImageView btnShowMenu;
        TextView tTableName;
        NotificationBadge badge;

        public ShowTableViewHolder(@NonNull View itemView) {
            super(itemView);

            tCustomer = itemView.findViewById(R.id.tCustomer);
            productCart = itemView.findViewById(R.id.productCart);
            btnShowMenu = itemView.findViewById(R.id.btnShowMenu);
            tTableName = itemView.findViewById(R.id.tTableName);
            badge = itemView.findViewById(R.id.badge);

        }
    }

}

package tdtu.com.finalprojectby518h0090.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.TableSelectOption;
import tdtu.com.finalprojectby518h0090.model.Table;

public class TableShowAdapter extends RecyclerView.Adapter<TableShowAdapter.TableShowViewHolder> {

    private List<Table> list;
    private TableSelectOption tableSelectOption;

    public TableShowAdapter() {

    }

    @NonNull
    @Override
    public TableShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_table_view, parent, false);
        return new TableShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableShowViewHolder holder, int position) {
        Table table = list.get(position);
        if (table == null) {
            return;
        }

        holder.tTableName.setText(table.getTableName());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


    public void setList(List<Table> list) {
        this.list = list;
    }

    public void setTableSelectOption(TableSelectOption tableSelectOption) {
        this.tableSelectOption = tableSelectOption;
    }

    public class TableShowViewHolder extends RecyclerView.ViewHolder  {

        TextView tCustomer, tTableName , tStatus;
        LinearLayout linearLayoutTable;
        ImageView tableIcon;

        public TableShowViewHolder(@NonNull View itemView) {
            super(itemView);

            tTableName = itemView.findViewById(R.id.tTableName);
            linearLayoutTable = itemView.findViewById(R.id.linearLayoutTable);
            tableIcon = itemView.findViewById(R.id.tableIcon);
        }


    }

}
package tdtu.com.finalprojectby518h0090.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.TableSelectOption;
import tdtu.com.finalprojectby518h0090.model.Table;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> implements Filterable {

    private List<Table> list;
    private List<Table> oldlist;
    private TableSelectOption tableSelectOption;

    public TableAdapter() {

    }

    public void setList(List<Table> list) {
        this.list = list;
        this.oldlist = list;
    }

    public void setTableSelectOption(TableSelectOption tableSelectOption) {
        this.tableSelectOption = tableSelectOption;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_table_view, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = list.get(position);
        if (table == null) {
            return;
        }

        holder.tTableName.setText(table.getTableName());
        holder.linearLayoutTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option_table , popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_table_change:
                                tableSelectOption.onClickEdit(table);
                                break;

                            case R.id.menu_table_delete:
                                tableSelectOption.onClickDelete(table);
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
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {

        TextView tTableName;
        LinearLayout linearLayoutTable;
        ImageView tableIcon;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            tTableName = itemView.findViewById(R.id.tTableName);
            linearLayoutTable = itemView.findViewById(R.id.linearLayoutTable);
            tableIcon = itemView.findViewById(R.id.tableIcon);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchString = constraint.toString();
                if (searchString.isEmpty()) {
                    list = oldlist;
                } else {
                    List<Table> newList = new ArrayList<>();
                    for (Table table : oldlist) {
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

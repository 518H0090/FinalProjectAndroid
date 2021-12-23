package tdtu.com.finalprojectby518h0090.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.IBillOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Bill;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> implements Filterable {

    private Context context;
    private List<Bill> list;
    private List<Bill> oldList;
    private IBillOption iBillOption;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
        this.oldList = list;
    }

    public void setiBillOption(IBillOption iBillOption) {
        this.iBillOption = iBillOption;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = list.get(position);
        if (bill == null) {
            return;
        }
        holder.BillTable.setText(bill.getTableName());
        holder.BillEmail.setText(bill.getUserEmail());
        holder.BillMoney.setText(String.valueOf(bill.getTotalPrice()));
        holder.BillDate.setText(bill.getDateTime());
        holder.btnLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option_bill, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_bill_change:
                                iBillOption.onClickEdit(bill);
                                break;
                            case R.id.menu_bill_delete:
                                iBillOption.onClickDelete(bill);
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

    public class BillViewHolder extends RecyclerView.ViewHolder {

        TextView BillTable, BillEmail, BillMoney, BillDate;
        LinearLayout btnLinearLayout;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            BillTable = itemView.findViewById(R.id.BillTable);
            BillEmail = itemView.findViewById(R.id.BillEmail);
            BillMoney = itemView.findViewById(R.id.BillMoney);
            BillDate = itemView.findViewById(R.id.BillDate);
            btnLinearLayout = itemView.findViewById(R.id.btnLinearLayout);

        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchValue = constraint.toString();
                if (searchValue.isEmpty()) {
                    list = oldList;
                } else {
                    List<Bill> newList = new ArrayList<>();
                    for (Bill bill : oldList) {
                        if (bill.getTableName().toLowerCase().contains(searchValue.toLowerCase()) || bill.getUserEmail().toLowerCase().contains(searchValue.toLowerCase())) {
                            newList.add(bill);
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
                list = (List<Bill>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

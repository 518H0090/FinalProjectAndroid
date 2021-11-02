package tdtu.com.finalprojectby518h0090.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.CustomerSelectOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Customer;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    private Context context;
    private List<Customer> list;
    private List<Customer> oldList;
    private CustomerSelectOption customerSelectOption;

    public CustomerAdapter(Context context, List<Customer> list) {
        this.context = context;
        this.list = list;
        this.oldList = list;
    }

    public void setCustomerSelectOption(CustomerSelectOption customerSelectOption) {
        this.customerSelectOption = customerSelectOption;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = list.get(position);
        if (customer == null) {
            return;
        }

        holder.textCustomerName.setText(customer.getCustomerName());
        holder.textCustomerAddress.setText(customer.getCustomerAddress());
        holder.textCustomerPhone.setText(customer.getCustomerPhone()+"");
        holder.btnActionCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext() , v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option_customer, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_customer_change:
                                customerSelectOption.onClickEdit(customer);
                                break;

                            case R.id.menu_customer_delete:
                                customerSelectOption.onClickDelete(customer);
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

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearCustomer;
        TextView textCustomerName, textCustomerAddress, textCustomerPhone;
        Button btnActionCustomer;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            linearCustomer = itemView.findViewById(R.id.linearCustomer);
            textCustomerName = itemView.findViewById(R.id.textCustomerName);
            textCustomerAddress = itemView.findViewById(R.id.textCustomerAddress);
            textCustomerPhone = itemView.findViewById(R.id.textCustomerPhone);
            btnActionCustomer = itemView.findViewById(R.id.btnActionCustomer);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String newSearch = constraint.toString();
                if (newSearch.isEmpty()) {
                    list = oldList;
                } else {
                    List<Customer> newList = new ArrayList<>();
                    for (Customer customer : oldList) {
                        if (customer.getCustomerName().toLowerCase().contains(newSearch.toLowerCase())) {
                            newList.add(customer);
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
                list = (List<Customer>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

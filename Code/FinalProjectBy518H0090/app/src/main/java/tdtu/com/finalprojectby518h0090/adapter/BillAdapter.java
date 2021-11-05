package tdtu.com.finalprojectby518h0090.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Bill;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{

    private Context context;
    private List<Bill> list;

    public BillAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {

        TextView BillTable, BillEmail, BillMoney, BillDate;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            BillTable = itemView.findViewById(R.id.BillTable);
            BillEmail = itemView.findViewById(R.id.BillEmail);
            BillMoney = itemView.findViewById(R.id.BillMoney);
            BillDate = itemView.findViewById(R.id.BillDate);

        }

    }
}

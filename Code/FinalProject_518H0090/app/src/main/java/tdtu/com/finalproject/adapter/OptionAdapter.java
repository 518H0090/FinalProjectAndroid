package tdtu.com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.com.finalproject.R;
import tdtu.com.finalproject.model.OptionDo;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.StringViewHolder> {

    private Context context;
    private List<OptionDo> listOption;


    public OptionAdapter(Context context, List<OptionDo> listOption) {
        this.context = context;
        this.listOption = listOption;
    }

    @NonNull
    @Override
    public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_setting_menu, parent , false);
        return new StringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
        OptionDo ValueOption = listOption.get(position);
        if (ValueOption == null) {
            return;
        }

        holder.textOption.setText(ValueOption.getNameOptions());
        holder.textOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ValueOption.getKeyOptions() + "  " + ValueOption.getNameOptions()  , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listOption != null) {
            return listOption.size();
        }
        return 0;
    }

    public class StringViewHolder extends RecyclerView.ViewHolder {

        TextView textOption;

        public StringViewHolder(@NonNull View itemView) {
            super(itemView);

            textOption = itemView.findViewById(R.id.textOption);
        }
    }
}

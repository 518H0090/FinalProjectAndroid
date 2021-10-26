package tdtu.com.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tdtu.com.finalproject.R;
import tdtu.com.finalproject.model.OptionDo;

public class OptionAdapterSpinner extends BaseAdapter {

    private Context context;
    private int layout;
    private List<OptionDo> doList;

    public OptionAdapterSpinner(Context context, int layout, List<OptionDo> doList) {
        this.context = context;
        this.layout = layout;
        this.doList = doList;
    }

    @Override
    public int getCount() {
        return doList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView textOption;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);

            viewHolder.textOption = convertView.findViewById(R.id.textOption);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OptionDo optionDo = doList.get(position);
        viewHolder.textOption.setText(optionDo.getNameOptions());
        return convertView;
    }
}

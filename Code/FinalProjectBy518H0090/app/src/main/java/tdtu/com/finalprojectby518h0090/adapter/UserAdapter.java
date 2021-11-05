package tdtu.com.finalprojectby518h0090.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.UserSelectOption;
import tdtu.com.finalprojectby518h0090.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private Context context;
    private List<User> list;
    private List<User> oldList;
    private UserSelectOption userSelectOption;

    public UserAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        this.oldList = list;
    }

    public void setUserSelectOption(UserSelectOption userSelectOption) {
        this.userSelectOption = userSelectOption;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = list.get(position);
        if (user == null) {
            return;
        }

        holder.userEmailShow.setText(user.getUserEmail());
        holder.userPasswordShow.setText(user.getUserPassword());
        holder.userPermissionShow.setText(user.getUserPermission());
        holder.btnUserAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option_user, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_user_change:
                                userSelectOption.onClickEditEmail(holder.getAdapterPosition());
                                break;

                            case R.id.menu_user_delete:
                                userSelectOption.onClickDelete(user);
                                break;

                            case R.id.menu_password_change:
                                userSelectOption.onClickEditPassword(user);
                                break;

                            case R.id.menu_permission_change:
                                userSelectOption.onChangePermission(user);
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

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userEmailShow, userPasswordShow, userPermissionShow;
        Button btnUserAction;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userEmailShow = itemView.findViewById(R.id.userEmailShow);
            userPasswordShow = itemView.findViewById(R.id.userPasswordShow);
            userPermissionShow = itemView.findViewById(R.id.userPermissionShow);
            btnUserAction = itemView.findViewById(R.id.btnUserAction);

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
                    List<User> newList = new ArrayList<>();
                    for (User user : oldList) {
                        if (user.getUserEmail().toLowerCase().contains(newSearch.toLowerCase())) {
                            newList.add(user);
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
                list = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

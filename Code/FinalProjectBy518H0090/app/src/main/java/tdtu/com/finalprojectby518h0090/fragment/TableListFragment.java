package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.statusDirty;
import static tdtu.com.finalprojectby518h0090.DefaultTag.statusOrder;
import static tdtu.com.finalprojectby518h0090.DefaultTag.statusReady;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.TableSelectOption;
import tdtu.com.finalprojectby518h0090.adapter.TableAdapter;
import tdtu.com.finalprojectby518h0090.model.Table;

public class TableListFragment extends Fragment implements TableSelectOption {

    private RecyclerView recyclerView;
    private TableAdapter adapter;
    private List<Table> list;
    private DatabaseReference mDatabase;
// ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);

        recyclerView = view.findViewById(R.id.recyler_table_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();
        adapter = new TableAdapter();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        
        adapter.setList(list);

        addShowChangeListner();

        adapter.setTableSelectOption(this);

        return view;
    }

    private void addShowChangeListner() {

        mDatabase.child("table").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Table table = snapshot.getValue(Table.class);
                if (table != null) {
                    list.add(table);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Table table = snapshot.getValue(Table.class);
                if (table == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0 ; i < list.size() ; i ++) {
                    if (table.getTKey() == list.get(i).getTKey()) {
                        list.set(i, table);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.getValue(Table.class);
                if (table == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size() ; i++) {
                    if (table.getTKey() == list.get(i).getTKey()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onClickEdit(Table table) {
        if (table == null ){
            return;
        }

        String tableKey = table.getTKey();
        String[] optionSelect= {statusReady , statusOrder ,statusDirty};

        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_edit_table);

        EditText customerNameTable = dialog.findViewById(R.id.customerNameTable);
        EditText tableName = dialog.findViewById(R.id.tableName);
        Spinner selectActionSpinner = dialog.findViewById(R.id.selectActionSpinner);
        Button btnUpdateNewTable = dialog.findViewById(R.id.btnUpdateNewTable);
        Button btnCancelNewTable = dialog.findViewById(R.id.btnCancelNewTable);

        customerNameTable.setText(table.getCustomerName());
        tableName.setText(table.getTableName());

        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                optionSelect
        );
        selectActionSpinner.setAdapter(adapter);

        btnUpdateNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customername = customerNameTable.getText().toString().trim();
                String tablename = tableName.getText().toString().trim();

                Table table1 = new Table(tableKey, customername, tablename , selectActionSpinner.getSelectedItem().toString());

                mDatabase.child("table").child(tableKey).updateChildren(table1.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getActivity(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        btnCancelNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClickDelete(Table table) {
        if (table == null ){
            return;
        }


        String tableKey = table.getTKey();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Xác nhận Xóa");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.child("table").child(tableKey).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getActivity(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Tắt", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
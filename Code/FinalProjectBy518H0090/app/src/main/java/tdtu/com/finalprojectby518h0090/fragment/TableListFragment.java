package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.statusDirty;
import static tdtu.com.finalprojectby518h0090.DefaultTag.statusOrder;
import static tdtu.com.finalprojectby518h0090.DefaultTag.statusReady;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.TableSelectOption;
import tdtu.com.finalprojectby518h0090.adapter.TableAdapter;
import tdtu.com.finalprojectby518h0090.cartproduct.model.CartDrink;
import tdtu.com.finalprojectby518h0090.model.Table;

public class TableListFragment extends Fragment implements TableSelectOption {

    private RecyclerView recyclerView;
    private TableAdapter adapter;
    private List<Table> list;
    private DatabaseReference mDatabase;
    Button btnAddNewTablePage;
    SearchView searchView;

// ...


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_list, container, false);

        recyclerView = view.findViewById(R.id.recyler_table_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnAddNewTablePage = view.findViewById(R.id.btnAddNewTablePage);

        btnAddNewTablePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_for_replace_table, new AddTableFragment());
                fragmentTransaction.addToBackStack(AddTableFragment.class.getName());
                fragmentTransaction.commit();
            }
        });


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

        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_edit_table);

        EditText tableName = dialog.findViewById(R.id.tableName);
        Button btnUpdateNewTable = dialog.findViewById(R.id.btnUpdateNewTable);
        Button btnCancelNewTable = dialog.findViewById(R.id.btnCancelNewTable);

        tableName.setText(table.getTableName());

        btnUpdateNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tablename = tableName.getText().toString().trim();

                Table table1 = new Table(tableKey, tablename);

                mDatabase.child("table").child(tableKey).updateChildren(table1.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getActivity(), "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();

                        mDatabase.child("cart").child(table.getTableName()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot cartSnapShot : snapshot.getChildren()) {
                                    CartDrink cartDrink = cartSnapShot.getValue(CartDrink.class);
                                    String keyAdd = mDatabase.push().getKey();
                                    mDatabase.child("cart").child(tablename).child(keyAdd).setValue(cartDrink);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        mDatabase.child("cart").child(table.getTableName()).removeValue();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
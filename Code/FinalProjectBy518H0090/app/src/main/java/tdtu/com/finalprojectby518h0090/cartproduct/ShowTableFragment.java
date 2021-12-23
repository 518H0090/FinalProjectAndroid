package tdtu.com.finalprojectby518h0090.cartproduct;

import android.app.SearchManager;
import android.content.Context;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.cartproduct.adapter.ShowTableAdapter;
import tdtu.com.finalprojectby518h0090.cartproduct.listener.IMenuChange;
import tdtu.com.finalprojectby518h0090.model.Table;

public class ShowTableFragment extends Fragment implements IMenuChange {

    RecyclerView listTableStatus;
    List<Table> tableList;
    ShowTableAdapter adapter;
    IMenuChange iMenuChange;
    SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_table, container, false);
        initUI(view);
        getDatabaseTable();
        return view;
    }

    private void initUI(View view) {
        listTableStatus = view.findViewById(R.id.listTableStatus);
        tableList = new ArrayList<>();
        adapter = new ShowTableAdapter(getActivity(), tableList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        listTableStatus.setLayoutManager(gridLayoutManager);
        listTableStatus.setAdapter(adapter);

        iMenuChange = this;
        adapter.setMenuChange(iMenuChange);
    }

    private void getDatabaseTable() {
        FirebaseDatabase
                .getInstance()
                .getReference("table")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Table table = snapshot.getValue(Table.class);
                        if (table != null) {
                            tableList.add(table);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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
    public void getTable(Table table) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("location_name", table.getTableName());
        ShowMenuFragment menuFragment = new ShowMenuFragment();
        menuFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.replace_show_everything, menuFragment);
        fragmentTransaction.addToBackStack(ShowMenuFragment.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
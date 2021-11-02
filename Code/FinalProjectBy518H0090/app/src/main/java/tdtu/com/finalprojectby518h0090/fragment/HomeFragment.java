package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.TableAdapter;
import tdtu.com.finalprojectby518h0090.adapter.TableShowAdapter;
import tdtu.com.finalprojectby518h0090.model.Table;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    TableShowAdapter tableAdapter;
    List<Table> list;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.listTableStatus);
        tableAdapter = new TableShowAdapter();

        list = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableAdapter.setList(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity() , 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(tableAdapter);

        ValueListenerFirebase();
        
        return view;


    }

    private void ValueListenerFirebase() {

        mDatabase.child("table").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Table table = snapshot.getValue(Table.class);
                if (table == null) {
                    return;
                }
                list.add(table);
                tableAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
                tableAdapter.notifyDataSetChanged();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
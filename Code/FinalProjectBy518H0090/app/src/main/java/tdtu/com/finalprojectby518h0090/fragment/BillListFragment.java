package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.BillAdapter;
import tdtu.com.finalprojectby518h0090.model.Bill;

public class BillListFragment extends Fragment {

    RecyclerView recycler_view_bill;
    List<Bill> list;
    BillAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);

        unitUI(view);
        getBillFromFirebase();

        return view;
    }

    private void unitUI(View view) {
        recycler_view_bill = view.findViewById(R.id.recycler_view_bill);
        list = new ArrayList<>();
        adapter = new BillAdapter(getActivity(), list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL , false);
        recycler_view_bill.setLayoutManager(layoutManager);
        recycler_view_bill.setAdapter(adapter);
    }

    private void getBillFromFirebase() {
        FirebaseDatabase.getInstance().getReference("bill").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if (bill != null) {
                    list.add(bill);
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

}
package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tdtu.com.finalprojectby518h0090.DefaultTag;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Table;

public class AddTableFragment extends Fragment {

    EditText editTableName;
    Button btnAddTable, btnCancelAddTable;
    DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_table, container, false);

        editTableName = view.findViewById(R.id.editTableName);
        btnAddTable = view.findViewById(R.id.btnAddTable);
        btnCancelAddTable = view.findViewById(R.id.btnCancelAddTable);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewTableValue();


            }
        });

        btnCancelAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    private void addNewTableValue() {
        String tableName = editTableName.getText().toString().trim();

        if (tableName.isEmpty() || tableName == null ) {
            Toast.makeText(getActivity(), "Vui Lòng Nhập Tên Vị Trí/ Bàn", Toast.LENGTH_SHORT).show();
        } else {
            String keyValue = mDatabase.push().getKey();

            Table table = new Table(keyValue, "", tableName, DefaultTag.statusReady);

            mDatabase.child("table").child(keyValue).setValue(table);
            mDatabase.child("table").child("keyValue").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}
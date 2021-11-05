package tdtu.com.finalprojectby518h0090.fragment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Bill;

public class AddBillFragment extends Fragment {

    EditText editTableBill, editMoneyBill, editDateBill;
    Button btnAddBill, btnDestroyBill;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_bill, container, false);
        unitUI(view);
        DesTroyAddBill();
        AddNewBill();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void unitUI(View view) {
        editTableBill = view.findViewById(R.id.editTableBill);
        editMoneyBill = view.findViewById(R.id.editMoneyBill);
        editDateBill = view.findViewById(R.id.editDateBill);
        btnAddBill = view.findViewById(R.id.btnAddBill);
        btnDestroyBill = view.findViewById(R.id.btnDestroyBill);
    }

    private void DesTroyAddBill() {
        btnDestroyBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void AddNewBill() {

        editDateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeForBill();
            }
        });

        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewValueBill();
            }
        });
    }

    private void setTimeForBill() {
        Calendar calendar = Calendar.getInstance();
        int yearGet = calendar.get(Calendar.YEAR);
        int monthGet = calendar.get(Calendar.MONTH);
        int dayGet = calendar.get(Calendar.DATE);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year , month , dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                editDateBill.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yearGet , monthGet, dayGet);

        dialog.show();
    }

    private void addNewValueBill() {
        String TableBill = editTableBill.getText().toString();
        int MoneyBill = Integer.parseInt(editMoneyBill.getText().toString());
        String dateTime = editDateBill.getText().toString();

        if (TableBill.isEmpty() || MoneyBill == 0 || dateTime.isEmpty()) {
            Toast.makeText(getActivity(), "Thiếu dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            String keyBill = FirebaseDatabase.getInstance().getReference().push().getKey();

            Bill bill = new Bill(keyBill, TableBill , FirebaseAuth.getInstance().getCurrentUser().getEmail(), MoneyBill, dateTime);

            FirebaseDatabase.getInstance().getReference("bill").child(keyBill).setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                    if (getParentFragmentManager() != null ) {
                        getParentFragmentManager().popBackStack();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
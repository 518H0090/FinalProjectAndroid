package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.Customer;

public class AddCustomerFragment extends Fragment {

    EditText editCustomerName,editCustomerAddress , editCustomerPhone;
    Button btnAddNewCustomer, btnCancelAddNewCustomer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_customer, container, false);

        editCustomerName = view.findViewById(R.id.editCustomerName);
        editCustomerAddress = view.findViewById(R.id.editCustomerAddress);
        editCustomerPhone = view.findViewById(R.id.editCustomerPhone);
        btnAddNewCustomer = view.findViewById(R.id.btnAddNewCustomer);
        btnCancelAddNewCustomer = view.findViewById(R.id.btnCancelAddNewCustomer);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        btnCancelAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        btnAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContract();
            }
        });

        return view;
    }

    private void addNewContract() {
        String customerName = editCustomerName.getText().toString().trim();
        String customerAddress = editCustomerAddress.getText().toString().trim();
        int customerPhone = Integer.parseInt(editCustomerPhone.getText().toString().trim());

        if (customerName.isEmpty() || customerAddress.isEmpty()) {
            Toast.makeText(getActivity(), "Thiếu Thông Tin", Toast.LENGTH_SHORT).show();
        } else {
            String customerKey = databaseReference.push().getKey();
            Customer customer = new Customer(customerKey, customerName, customerAddress , customerPhone);
            databaseReference.child("customer").child(customerKey).setValue(customer);
            Toast.makeText(getActivity(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
        }

    }
}
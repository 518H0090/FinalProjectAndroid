package tdtu.com.finalprojectby518h0090.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tdtu.com.finalprojectby518h0090.CustomerSelectOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.CustomerAdapter;
import tdtu.com.finalprojectby518h0090.model.Customer;

public class CustomerListFragment extends Fragment implements CustomerSelectOption {

    Button btnAddNewConTract;
    RecyclerView recyclerView;
    List<Customer> customerList;
    CustomerAdapter customerAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        btnAddNewConTract = view.findViewById(R.id.btnAddNewConTract);
        recyclerView = view.findViewById(R.id.recylerViewListContract);
        customerList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        customerAdapter = new CustomerAdapter(
                getActivity(),
                customerList
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customerAdapter);


        btnAddNewConTract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.replace_for_contract, new AddCustomerFragment());
                fragmentTransaction.addToBackStack(AddCustomerFragment.class.getName());
                fragmentTransaction.commit();
            }
        });

        addCustomerToArrayList();

        customerAdapter.setCustomerSelectOption(this);

        return view;
    }

    private void addCustomerToArrayList() {
        databaseReference.child("customer").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Customer customer = snapshot.getValue(Customer.class);
                if (customer != null) {
                    customerList.add(customer);
                    customerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Customer customer = snapshot.getValue(Customer.class);
                if (customer == null || customerList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < customerList.size() ; i++) {
                    if ( customer.getCustomerKey() == customerList.get(i).getCustomerKey()) {
                        customerList.set(i , customer);
                        break;
                    }
                }

                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Customer customer = snapshot.getValue(Customer.class);
                if (customer == null || customerList.isEmpty()) {
                    return;
                }

                for (int i = 0 ; i < customerList.size() ; i++) {
                    if (customer.getCustomerKey() == customerList.get(i).getCustomerKey()){
                        customerList.remove(customerList.get(i));
                        break;
                    }
                }
                customerAdapter.notifyDataSetChanged();
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
    public void onClickEdit(Customer customer) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_customer);
        dialog.setCanceledOnTouchOutside(false);

        EditText customerName = dialog.findViewById(R.id.customerName);
        EditText customerAddress = dialog.findViewById(R.id.customerAddress);
        EditText customerPhone = dialog.findViewById(R.id.customerPhone);
        Button btnUpdateNewCustomer = dialog.findViewById(R.id.btnUpdateNewCustomer);
        Button btnCancelAddNewCustomer = dialog.findViewById(R.id.btnCancelAddNewCustomer);

        customerName.setText(customer.getCustomerName());
        customerAddress.setText(customer.getCustomerAddress());
        customerPhone.setText(customer.getCustomerPhone() + "");

        btnUpdateNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = customerName.getText().toString();
                String address = customerAddress.getText().toString();
                int phone = Integer.parseInt(customerPhone.getText().toString());

                if (name == null || address == null) {
                    Toast.makeText(getActivity(), "Thiếu Thông Tin", Toast.LENGTH_SHORT).show();
                } else {
                    String key = customer.getCustomerKey();

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("customerName", name);
                    result.put("customerAddress", address);
                    result.put("customerPhone", phone);

                    databaseReference.child("customer").child(key).updateChildren(result, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getActivity(), "Thành Công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });


        btnCancelAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public void onClickDelete(Customer customer) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Xóa Khách Hàng");
        dialog.setMessage("Xác Nhận Xóa ?");

        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String customerKey = customer.getCustomerKey();
                databaseReference.child("customer").child(customerKey).removeValue();
                Toast.makeText(getActivity(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                customerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
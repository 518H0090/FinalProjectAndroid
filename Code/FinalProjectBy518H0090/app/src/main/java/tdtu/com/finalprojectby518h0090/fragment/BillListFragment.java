package tdtu.com.finalprojectby518h0090.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tdtu.com.finalprojectby518h0090.IBillOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.BillAdapter;
import tdtu.com.finalprojectby518h0090.model.Bill;

public class BillListFragment extends Fragment implements IBillOption{

    RecyclerView recycler_view_bill;
    List<Bill> list;
    BillAdapter adapter;
    IBillOption iBillOption = this;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_list, container, false);

        unitUI(view);
        getBillFromFirebase();
        adapter.setiBillOption(iBillOption);

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
                Bill bill = snapshot.getValue(Bill.class);
                if (bill == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size() ; i++) {
                    if (bill.getBillKey() == list.get(i).getBillKey()) {
                        list.set(i , bill);
                        break;
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bill bill = snapshot.getValue(Bill.class);
                if (bill == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size() ; i++) {
                    if (bill.getBillKey() == list.get(i).getBillKey()) {
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_option_add_bill, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_bill:
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.replace_for_bill, new AddBillFragment());
                fragmentTransaction.addToBackStack(AddBillFragment.class.getName());
                fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickEdit(Bill bill) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_bill);
        dialog.setCanceledOnTouchOutside(false);

        EditText editNewBillName = dialog.findViewById(R.id.editNewBillName);
        EditText editNewBillMoney = dialog.findViewById(R.id.editNewBillMoney);
        EditText editNewBillDate = dialog.findViewById(R.id.editNewBillDate);
        Button btnUpdateNewBill = dialog.findViewById(R.id.btnUpdateNewBill);
        Button btnCancelAddNewBill = dialog.findViewById(R.id.btnCancelAddNewBill);

        editNewBillName.setText(bill.getTableName());
        editNewBillMoney.setText(String.valueOf(bill.getTotalPrice()));
        editNewBillDate.setText(bill.getDateTime());

        editNewBillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int yearGet = calendar.get(Calendar.YEAR);
                int monthGet = calendar.get(Calendar.MONTH);
                int dateGet = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year , month , dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        editNewBillDate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },yearGet, monthGet, dateGet);

                datePickerDialog.show();
            }
        });

        btnCancelAddNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdateNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameBill = editNewBillName.getText().toString();
                int moneyBill = Integer.parseInt(editNewBillMoney.getText().toString());
                String dateBill = editNewBillDate.getText().toString();

                if (nameBill.isEmpty() || moneyBill == 0 || dateBill.isEmpty()){
                    Toast.makeText(getActivity(), "Thiếu Thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    bill.setUserEmail(user.getEmail());
                    bill.setTableName(nameBill);
                    bill.setTotalPrice(moneyBill);
                    bill.setDateTime(dateBill);

                    FirebaseDatabase.getInstance().getReference("bill").child(bill.getBillKey()).setValue(bill).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClickDelete(Bill bill) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Xóa Bill");
        dialog.setMessage("Xác Nhận Xóa ? ");
        dialog.setPositiveButton("Có ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("bill").child(bill.getBillKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Xóa Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });
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
}
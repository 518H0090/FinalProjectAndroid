package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionAdmin;
import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionStaff;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tdtu.com.finalprojectby518h0090.MainActivity;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.User;

public class AddUserFragment extends Fragment {

    Button btnDestroy, btnAddUser;
    EditText editEmail, editPassword, editFullName, editDateBirth , editUserPhone, editUserAddress;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView btnTurnBackShowTable;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        btnDestroy = view.findViewById(R.id.btnDestroy);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editFullName = view.findViewById(R.id.editFullName);
        editDateBirth = view.findViewById(R.id.editDateBirth);
        editUserPhone = view.findViewById(R.id.editUserPhone);
        editUserAddress = view.findViewById(R.id.editUserAddress);
        btnTurnBackShowTable = view.findViewById(R.id.btnTurnBackShowTable);
        mainActivity = (MainActivity) getActivity();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressDialog = new ProgressDialog(getActivity());

        auth = FirebaseAuth.getInstance();

        btnDestroy.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnTurnBackShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        editDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int yearGet = calendar.get(Calendar.YEAR);
                int monthGet = calendar.get(Calendar.MONTH);
                int dateGet = calendar.get(Calendar.DATE);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        editDateBirth.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },yearGet , monthGet, dateGet);
                dialog.show();
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUserToSystem();
            }
        });

        return view;
    }

    private void addNewUserToSystem() {

        String fullName = editFullName.getText().toString();
        String NgaySinh = editDateBirth.getText().toString();
        long phone = Long.parseLong(editUserPhone.getText().toString());
        String address = editUserAddress.getText().toString();

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || NgaySinh.isEmpty() || phone == 0 || address.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Vui Lòng Nhập Đủ Thông Tin", Toast.LENGTH_SHORT).show();
        } else {

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                String customerKey = databaseReference.push().getKey();

                                User user = new User(customerKey, fullName , NgaySinh, phone, address, email);
                                databaseReference.child("user").child(customerKey).setValue(user);
                                Toast.makeText(getActivity(), "Thêm Người dùng thành công", Toast.LENGTH_SHORT).show();
                                mainActivity.showInformation();
                                if (getParentFragmentManager() != null) {
                                    getParentFragmentManager().popBackStack();
                                }
                            } else {
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), "Thêm Người dùng thất bại \n Có thể người dùng đã tồn tại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
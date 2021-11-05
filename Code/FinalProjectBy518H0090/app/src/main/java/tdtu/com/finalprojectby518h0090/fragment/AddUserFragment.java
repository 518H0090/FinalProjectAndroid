package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionAdmin;
import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionStaff;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.User;

public class AddUserFragment extends Fragment {

    Button btnDestroy, btnAddUser;
    EditText editEmail, editPassword, editRePassword;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        btnDestroy = view.findViewById(R.id.btnDestroy);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        editEmail = view.findViewById(R.id.editEmail);
        editPassword = view.findViewById(R.id.editPassword);
        editRePassword = view.findViewById(R.id.editRePassword);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressDialog = new ProgressDialog(getActivity());

        auth = FirebaseAuth.getInstance();

        btnDestroy.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
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

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String repassword = editRePassword.getText().toString().trim();
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Vui Lòng Nhập Đủ Thông Tin", Toast.LENGTH_SHORT).show();
        } else {
            if (!password.equals(repassword)) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Mật Khẩu Không Trùng Khớp", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();

                                    String customerKey = databaseReference.push().getKey();

                                    User user = new User(customerKey, email, password, userPermissionStaff);
                                    databaseReference.child("user").child(customerKey).setValue(user);
                                    Toast.makeText(getActivity(), "Thêm Người dùng thành công", Toast.LENGTH_SHORT).show();
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

    }


}
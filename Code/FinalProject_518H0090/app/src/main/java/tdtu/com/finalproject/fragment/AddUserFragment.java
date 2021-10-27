package tdtu.com.finalproject.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tdtu.com.finalproject.R;

public class AddUserFragment extends Fragment {

    Button btnDestroy , btnAddUser;
    EditText editEmail, editPassword , editRePassword;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(getActivity());

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
        auth = FirebaseAuth.getInstance();

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String repassword = editRePassword.getText().toString().trim();
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
            Toast.makeText(getActivity(), "Vui Lòng Nhập Đủ Thông Tin", Toast.LENGTH_SHORT).show();
        } else {
            if (!password.equals(repassword)) {
                Toast.makeText(getActivity(), "Mật Khẩu Không Trùng Khớp", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    FirebaseUser user = auth.getCurrentUser();
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Thêm Người Dùng Thành Công", Toast.LENGTH_SHORT).show();
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
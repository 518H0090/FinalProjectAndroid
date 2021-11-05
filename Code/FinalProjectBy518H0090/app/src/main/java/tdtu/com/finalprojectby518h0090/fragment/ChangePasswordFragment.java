package tdtu.com.finalprojectby518h0090.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.User;

public class ChangePasswordFragment extends Fragment {

    View view;
    EditText passwordChange, rePasswordChange;
    Button btnUpdatePassword;
    FirebaseUser user;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment getInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        declareView();
        ClickUpdatePassWordFireBase();

        return view;
    }

    private void declareView() {
        passwordChange = view.findViewById(R.id.passwordChange);
        rePasswordChange = view.findViewById(R.id.rePasswordChange);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void ClickUpdatePassWordFireBase() {
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePasswordFirebase();
            }
        });
    }

    private void UpdatePasswordFirebase() {
        String password = passwordChange.getText().toString().trim();
        String repassword = rePasswordChange.getText().toString().trim();

        if (password.isEmpty() || repassword.isEmpty()) {
            Toast.makeText(getActivity(), "Vui Lòng Không Để Trống", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repassword)) {
            Toast.makeText(getActivity(), "Mật Khẩu Không Trùng Khớp", Toast.LENGTH_SHORT).show();
        } else {
            String newPassword = password;

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Cập Nhật Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                            } else {
                                reAuthenticateUserDialog();
                            }
                        }
                    });
        }


    }

    private void reAuthenticateUserDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_show_reauthentication);

        EditText reUsername = dialog.findViewById(R.id.reUsername);
        EditText rePassword = dialog.findViewById(R.id.rePassword);
        Button btnReLogin = dialog.findViewById(R.id.btnReLogin);
        Button btnReLoginCancel = dialog.findViewById(R.id.btnReLoginCancel);

        btnReLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = reUsername.getText().toString().trim();
                String password = rePassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(username, password);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Xác thực thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        UpdatePasswordFirebase();
                                    } else {
                                        Toast.makeText(getActivity(), "Xác thực thất bại", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
            }
        });

        btnReLoginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hủy Xác Thực", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
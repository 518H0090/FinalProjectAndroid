package tdtu.com.finalprojectby518h0090.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import tdtu.com.finalprojectby518h0090.MainActivity;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.User;

public class UserAuthenticationFragment extends Fragment {

    ImageView btnTurnBackUserShow;
    TextView userGetCurrentShow;
    EditText userAuthenNeedEdit;
    Button btnReauthenLogin, btnChangeEmail, btnChangePassword, btnResetPassword, btnDeleteAccount;
    MainActivity mainActivity;
    User getUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_authentication, container, false);
        initUI(view);
        TurnBackUserList();

        Bundle bundle = getArguments();
        if (bundle != null) {
            getUser = (User) bundle.get("AuthenUser");
            userAuthenNeedEdit.setText(getUser.getUserEmail());
        }

        userGetCurrentShow.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        initSetEventClick();

        return view;
    }

    private void initUI(View view) {
        btnTurnBackUserShow = view.findViewById(R.id.btnTurnBackUserShow);
        userGetCurrentShow = view.findViewById(R.id.userGetCurrentShow);
        userAuthenNeedEdit = view.findViewById(R.id.userAuthenNeedEdit);
        btnReauthenLogin = view.findViewById(R.id.btnReauthenLogin);
        btnChangeEmail = view.findViewById(R.id.btnChangeEmail);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

        mainActivity = (MainActivity) getActivity();
    }

    private void TurnBackUserList() {
        btnTurnBackUserShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void initSetEventClick() {
        btnReauthenLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckReauthenLogin();
            }
        });

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChnageEmail();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendLinkReset();
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void onCheckReauthenLogin() {
        String userEdit = userAuthenNeedEdit.getText().toString();
        String userLoginNow = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (!userEdit.equals(userLoginNow)) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_show_reauthentication);
            EditText reUsername = dialog.findViewById(R.id.reUsername);
            EditText rePassword = dialog.findViewById(R.id.rePassword);

            reUsername.setText(userAuthenNeedEdit.getText().toString().trim());

            Button btnReLogin = dialog.findViewById(R.id.btnReLogin);
            Button btnReLoginCancel = dialog.findViewById(R.id.btnReLoginCancel);

            btnReLoginCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnReLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = reUsername.getText().toString().trim();
                    String password = rePassword.getText().toString().trim();

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getActivity(), "Thiếu Thông Tin Xác Thực", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(getActivity(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                                            userGetCurrentShow.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                            mainActivity.showInformation();
                                            dialog.dismiss();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getActivity(), "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

            dialog.dismiss();

            dialog.show();


        } else {
            Toast.makeText(getActivity(), "Tài Khoản Cần Sửa Đã Đăng Nhập rồi", Toast.LENGTH_SHORT).show();
        }
    }

    private void onChnageEmail() {
        String userEdit = userAuthenNeedEdit.getText().toString();
        String userNowLogin = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (userEdit.equals(userNowLogin)) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_edit_newemail);
            dialog.setCanceledOnTouchOutside(false);

            EditText editNewEmail = dialog.findViewById(R.id.editNewEmail);
            Button btnChangeEmail = dialog.findViewById(R.id.btnChangeEmail);
            Button btnCancelChangeEmail = dialog.findViewById(R.id.btnCancelChangeEmail);

            editNewEmail.setText(userNowLogin);

            btnCancelChangeEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnChangeEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newEmail = editNewEmail.getText().toString();
                    if (newEmail.isEmpty()) {
                        Toast.makeText(getActivity(), "Vui Lòng nhập email", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Thay Đổi Thành Công", Toast.LENGTH_SHORT).show();
                                            if (getUser != null) {
                                                getUser.setUserEmail(newEmail);
                                                FirebaseDatabase.getInstance().getReference("user").child(getUser.getUserKey()).setValue(getUser);
                                            }
                                            userAuthenNeedEdit.setText(newEmail);
                                            userGetCurrentShow.setText(newEmail);
                                            mainActivity.showInformation();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "Thay Đổi Thất Bại", Toast.LENGTH_SHORT).show();
                                            Dialog dialogReauthen = new Dialog(getActivity());
                                            dialogReauthen.setCanceledOnTouchOutside(false);
                                            dialogReauthen.setContentView(R.layout.dialog_show_reauthentication);
                                            EditText reUsername = dialogReauthen.findViewById(R.id.reUsername);
                                            EditText rePassword = dialogReauthen.findViewById(R.id.rePassword);

                                            reUsername.setText(userNowLogin);

                                            Button btnReLogin = dialogReauthen.findViewById(R.id.btnReLogin);
                                            Button btnReLoginCancel = dialogReauthen.findViewById(R.id.btnReLoginCancel);
                                            btnReLoginCancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(getActivity(), "Hủy Xác Thực", Toast.LENGTH_SHORT).show();
                                                    dialogReauthen.dismiss();
                                                }
                                            });

                                            btnReLogin.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String reEmail = reUsername.getText().toString().trim();
                                                    String rePass = rePassword.getText().toString().trim();
                                                    if (reEmail.isEmpty() || rePass.isEmpty()) {
                                                        Toast.makeText(getActivity(), "Vui Lòng Nhập Thông Tin", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        AuthCredential credential = EmailAuthProvider
                                                                .getCredential(reEmail, rePass);

                                                        user.reauthenticate(credential)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(getActivity(), "Xác Thực Thành Công", Toast.LENGTH_SHORT).show();
                                                                        dialogReauthen.dismiss();
                                                                    }
                                                                });

                                                    }
                                                }
                                            });
                                            dialogReauthen.show();

                                            dialog.show();
                                        }
                                    }
                                });

                    }
                }
            });

            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Tài Khoản Không Khớp Vui Lòng Đăng Nhập Lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSendLinkReset() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = userAuthenNeedEdit.getText().toString();
        String userNowLogin = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (emailAddress.equals(userNowLogin)) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_send_reset_password);

            EditText editNewEmailSend = dialog.findViewById(R.id.editNewEmailSend);
            Button btnSendEmail = dialog.findViewById(R.id.btnSendEmail);
            Button btnCancelSendEmail = dialog.findViewById(R.id.btnCancelSendEmail);

            editNewEmailSend.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            btnSendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emailAddress.equals(userNowLogin)) {
                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Gửi Email Thành Công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                    }
                }
            });

            btnCancelSendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Tài Khoản Không Khớp Vui Lòng Đăng Nhập Lại", Toast.LENGTH_SHORT).show();
        }

    }

}
package tdtu.com.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText editUser, editPassword;
    private TextView textForgetPassword;
    private CheckBox checkLogin;
    private Button btnLogin, btnOut;
    private ProgressDialog progressDialog;
    FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnOut = findViewById(R.id.btnOut);
        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        textForgetPassword = findViewById(R.id.textForgetPassword);
        checkLogin = findViewById(R.id.checkLogin);

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("checkLogin" , MODE_PRIVATE);

        AutoSignUpEmailFirstGo();

        if (sharedPreferences != null) {
            editUser.setText(sharedPreferences.getString("username", ""));
            editPassword.setText(sharedPreferences.getString("password", ""));
            checkLogin.setChecked(sharedPreferences.getBoolean("checked", false));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSignInAccount();
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

    }

    private void AutoSignUpEmailFirstGo() {
        auth = FirebaseAuth.getInstance();

        String email = "nhoxhieuro5@gmail.com";
        String password = "Hieuro5122";
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Tạo Tài Khoản Admin \n nhoxhieuro5@gmail.com", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });

    }

    private void ToSignInAccount() {
        auth = FirebaseAuth.getInstance();

        String email = editUser.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        progressDialog.show();

        if (email.isEmpty() || password.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressDialog.dismiss();

                                //Logic Checked Login
                                if (checkLogin.isChecked()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", email);
                                    editor.putString("password", password);
                                    editor.putBoolean("checked", true);
                                    editor.commit();
                                } else {
                                    if (sharedPreferences != null) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("username");
                                        editor.remove("password");
                                        editor.remove("checked");
                                        editor.commit();
                                    }
                                }

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }



    }
}
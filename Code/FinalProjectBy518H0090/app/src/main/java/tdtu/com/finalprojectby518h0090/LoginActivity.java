package tdtu.com.finalprojectby518h0090;

import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tdtu.com.finalprojectby518h0090.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editUser, editPassword;
    private TextView textForgetPassword;
    private CheckBox checkLogin;
    private Button btnLogin, btnOut;
    private ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("checkLogin" , MODE_PRIVATE);

        AutoSignUpEmailFirstGo();
        initBtnClick();

    }

    private void initBtnClick() {
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

        textForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
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
                            String userKey = firebaseDatabase.getReference().push().getKey();
                            User user = new User(userKey, "Hu???nh Tr???n Trung Hi???u", "12/04/2000", 384992205, "B??nh H??ng H??a A", email);
                            firebaseDatabase.getReference("user").child(userKey).setValue(user);
                            Toast.makeText(LoginActivity.this, "T???o T??i Kho???n Admin \n nhoxhieuro5@gmail.com", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(LoginActivity.this, "Vui l??ng nh???p ????? th??ng tin", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, "????ng nh???p th???t b???i.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }



    }
}
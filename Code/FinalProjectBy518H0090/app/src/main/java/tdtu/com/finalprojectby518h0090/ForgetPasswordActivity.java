package tdtu.com.finalprojectby518h0090;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText EmailReset;
    Button btnUpdatePassword, btnTurnBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        EmailReset = findViewById(R.id.EmailReset);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnTurnBackLogin = findViewById(R.id.btnTurnBackLogin);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = EmailReset.getText().toString().trim();
                if (getEmail.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = getEmail;

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPasswordActivity.this, "Email đã được gửi", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgetPasswordActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnTurnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
package tdtu.com.finalprojectby518h0090;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadIntoAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_into_app);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                nextActivity();
            }
        },3000);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(LoadIntoAppActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoadIntoAppActivity.this, MainActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
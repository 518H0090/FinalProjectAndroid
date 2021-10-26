package tdtu.com.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadIntoAppActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_into_app);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Thông Báo");
        progressDialog.setMessage("Vui Lòng Đợi Ứng Dụng Thực Thi");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
                Intent intent = new Intent(LoadIntoAppActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },3000);
    }
}
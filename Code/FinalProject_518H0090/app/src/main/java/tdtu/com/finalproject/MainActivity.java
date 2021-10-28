package tdtu.com.finalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import tdtu.com.finalproject.fragment.BillFragment;
import tdtu.com.finalproject.fragment.ChangePasswordFragment;
import tdtu.com.finalproject.fragment.CustomerFragment;
import tdtu.com.finalproject.fragment.HomeFragment;
import tdtu.com.finalproject.fragment.MyProfileFragment;
import tdtu.com.finalproject.fragment.SettingFragment;

//518H0090 - Huỳnh Trần Trung Hiếu
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Giá trị Open Gallery
    public static final int REQUEST_OPEN_GALLERY = 9;

    //Khởi tạo giá trị để phân biệt các fragment
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_BILL = 1;
    private static final int FRAGMENT_CUSTOMER = 2;
    private static final int FRAGMENT_SETTING = 3;
    private static final int FRAGMENT_MY_PROFILE = 4;
    private static final int FRAGMENT_CHANGE_PASSWORD = 5;

    //Biến check fragment hiện tại vừa vào chọn Home Fragment
    private int mCurrentFragment = FRAGMENT_HOME;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MyProfileFragment myProfileFragment = new MyProfileFragment();

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
                myProfileFragment.setfUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    myProfileFragment.setBitmapProfile(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    //Circle Image kế thừa từ ImageView
    ImageView imgUser;
    TextView txtUserName, txtUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Thiết Lập DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        //Thiết lập Toggle cho DrawerLayout
        ActionBarDrawerToggle Toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        //Thêm Toggle và chạy
        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();

        //Navigation View
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Khi vừa vào Sẽ tự động chọn Home Fragment;
        replaceFragment(HomeFragment.getInstance());
        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);

        imgUser = navigationView.getHeaderView(0).findViewById(R.id.imgUser);
        txtUserName = navigationView.getHeaderView(0).findViewById(R.id.txtUserName);
        txtUserEmail = navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);

        showInformation();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Lấy id chọn
        int id = item.getItemId();

        //Hiển thị Fragment tương ứng
        if (id == R.id.menu_home) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }

        } else if (id == R.id.menu_bill) {
            if (mCurrentFragment != FRAGMENT_BILL) {
                replaceFragment(new BillFragment());
                mCurrentFragment = FRAGMENT_BILL;
            }
        } else if (id == R.id.menu_customer) {
            if (mCurrentFragment != FRAGMENT_CUSTOMER) {
                replaceFragment(new CustomerFragment());
                mCurrentFragment = FRAGMENT_CUSTOMER;
            }
        } else if (id == R.id.menu_setting) {
            if (mCurrentFragment != FRAGMENT_SETTING) {
                replaceFragment(new SettingFragment());
                mCurrentFragment = FRAGMENT_SETTING;
            }
        } else if (id == R.id.menu_my_profile) {
            if (mCurrentFragment != FRAGMENT_MY_PROFILE) {
                replaceFragment(myProfileFragment);
                mCurrentFragment = FRAGMENT_MY_PROFILE;
            }
        } else if (id == R.id.menu_change_password) {
            if (mCurrentFragment != FRAGMENT_CHANGE_PASSWORD) {
                replaceFragment(new ChangePasswordFragment());
                mCurrentFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        } else if (id == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setCheckTittleDrawer(id);
        //Đóng DrawerLayout
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    //Function hiển thị tittle được chọn.
    private void setCheckTittleDrawer(int id) {

        switch (id) {
            case R.id.menu_home:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(false);
                break;

            case R.id.menu_bill:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(true);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(false);
                break;

            case R.id.menu_customer:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(true);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(false);
                break;

            case R.id.menu_setting:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(false);
                break;

            case R.id.menu_my_profile:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(true);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(false);
                break;

            case R.id.menu_change_password:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_bill).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_customer).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_setting).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_my_profile).setChecked(false);
                navigationView.getMenu().findItem(R.id.menu_change_password).setChecked(true);
                break;
        }

    }

    public void showInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();


        if (name == null) {
            txtUserName.setVisibility(View.GONE);
        } else {
            txtUserName.setVisibility(View.VISIBLE);
            txtUserName.setText(name);
        }

        txtUserEmail.setText(email);

        //Thư viện load ảnh với Url nếu ko có mặc định chọn logo_cafe
        Glide.with(this).load(photoUrl).error(R.drawable.logo_cafe).into(imgUser);
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_OPEN_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng cho phép ứng dụng truy cập Gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));

    }


}
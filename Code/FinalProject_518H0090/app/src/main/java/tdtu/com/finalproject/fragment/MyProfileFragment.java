package tdtu.com.finalproject.fragment;

import static tdtu.com.finalproject.MainActivity.REQUEST_OPEN_GALLERY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import tdtu.com.finalproject.MainActivity;
import tdtu.com.finalproject.R;

public class MyProfileFragment extends Fragment {

    View view;
    ImageView imgProfile;
    EditText nameProfile, emailProfile;
    Button btnUpdateProfile;
    FirebaseAuth auth;
    FirebaseUser user;
    MainActivity mainActivity;
    Uri fUri;

    public static MyProfileFragment getInstance() {
        return new MyProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        declareValue();
        showProfile();
        OpenGalleryListener();
        UpdateProfileUser();

        return view;
    }



    private void declareValue() {
        imgProfile = view.findViewById(R.id.imgProfile);
        nameProfile = view.findViewById(R.id.nameProfile);
        emailProfile = view.findViewById(R.id.emailProfile);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        mainActivity = (MainActivity) getActivity();

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void showProfile() {
        nameProfile.setText(user.getDisplayName());
        emailProfile.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.logo_cafe).into(imgProfile);
    }

    private void OpenGalleryListener() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPermissionRequest();
            }
        });
    }

    private void onClickPermissionRequest() {

        if (mainActivity == null) {
            return;
        }

        // Nếu API < Android 6 thì tự động mở Gallery
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGallery();
            return;
        }

        //Gallery >= 6 thì sẽ kiểm tra Permission xem đã cho phép hay chưa.
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            // Mảng chứa các permission của thiết bị
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //Yêu cầu permission , REQUEST_OPEN_GALLERY lấy từ MainActivity
            getActivity().requestPermissions(permission, REQUEST_OPEN_GALLERY);
        }
    }

    public void setBitmapProfile(Bitmap bitmap) {
        imgProfile.setImageBitmap(bitmap);
    }

    private void UpdateProfileUser() {
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNewProfile();
            }
        });
    }

    private void updateNewProfile() {
        String nameProfileValue = nameProfile.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameProfileValue)
                .setPhotoUri(fUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Cập Nhật Thông Tin Thành Công", Toast.LENGTH_SHORT).show();
                            mainActivity.showInformation();
                        }
                    }
                });
    }

    public void setfUri(Uri fUri) {
        this.fUri = fUri;
    }
}

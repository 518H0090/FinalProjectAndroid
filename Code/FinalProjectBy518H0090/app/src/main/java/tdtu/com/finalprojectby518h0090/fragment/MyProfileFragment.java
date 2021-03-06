package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.MainActivity.REQUEST_OPEN_GALLERY;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import tdtu.com.finalprojectby518h0090.MainActivity;
import tdtu.com.finalprojectby518h0090.R;

public class MyProfileFragment extends Fragment {

    View view;
    ImageView imgProfile;
    EditText nameProfile, emailProfile;
    Button btnUpdateProfile;
    FirebaseAuth auth;

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void showProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        // N???u API < Android 6 th?? t??? ?????ng m??? Gallery
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGallery();
            return;
        }

        //Gallery >= 6 th?? s??? ki???m tra Permission xem ???? cho ph??p hay ch??a.
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            // M???ng ch???a c??c permission c???a thi???t b???
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            //Y??u c???u permission , REQUEST_OPEN_GALLERY l???y t??? MainActivity
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                            Toast.makeText(getActivity(), "C???p Nh???t Th??ng Tin Th??nh C??ng", Toast.LENGTH_SHORT).show();
                            mainActivity.showInformation();
                        }
                    }
                });
    }

    public void setfUri(Uri fUri) {
        this.fUri = fUri;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
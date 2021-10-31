package tdtu.com.finalprojectby518h0090.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.xml.transform.Result;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;

public class AddMenuFragment extends Fragment {

    EditText editNameDrink, editTagDrink, editPriceDrink;
    ImageView imageViewMenu;
    Button btnAddMenu, btnCancelAddMenu;
    Uri uriImage;

    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    ActivityResultLauncher<Intent> mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) {
                            return;
                        }

                        Uri uri = intent.getData();
                        Glide.with(getActivity()).load(uri).into(imageViewMenu);
                        setUriImage(uri);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_menu, container, false);

        editNameDrink = view.findViewById(R.id.editNameDrink);
        editTagDrink = view.findViewById(R.id.editTagDrink);
        editPriceDrink = view.findViewById(R.id.editPriceDrink);
        imageViewMenu = view.findViewById(R.id.imageViewMenu);
        btnAddMenu = view.findViewById(R.id.btnAddMenu);
        btnCancelAddMenu = view.findViewById(R.id.btnCancelAddMenu);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("uploads");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("menu");

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
            }
        });

        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDrinkMenu();
            }
        });

        btnCancelAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void addNewDrinkMenu() {
        if (uriImage != null) {
            StorageReference NewRef = storageRef.child(System.currentTimeMillis() + "."+ getExtension(uriImage));
            NewRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    NewRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            String menuKey = databaseReference.push().getKey();

                            String nameDrink = editNameDrink.getText().toString();
                            String tagDrink = editTagDrink.getText().toString();
                            Integer PriceDrink = Integer.parseInt(editPriceDrink.getText().toString());

                            MenuDrink menuDrink = new MenuDrink(menuKey, tagDrink , nameDrink, uri.toString(),PriceDrink);
                            databaseReference.child(menuKey).setValue(menuDrink);
                            Toast.makeText(getActivity(), "Thêm Menu Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }else {
            Toast.makeText(getActivity(), "Không Có File Ảnh Được Chọn", Toast.LENGTH_SHORT).show();
        }
    }
}
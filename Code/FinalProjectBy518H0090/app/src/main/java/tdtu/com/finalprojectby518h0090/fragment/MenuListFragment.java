package tdtu.com.finalprojectby518h0090.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tdtu.com.finalprojectby518h0090.MenuSelectionOption;
import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.MenuAdapter;
import tdtu.com.finalprojectby518h0090.model.MenuDrink;
import tdtu.com.finalprojectby518h0090.model.Table;

public class MenuListFragment extends Fragment implements MenuSelectionOption {

    RecyclerView recylerViewMenu;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    List<MenuDrink> list;
    MenuAdapter menuAdapter;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Button btnAddNewMenuChangePage;
    SearchView searchView;

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
                        setmUri(uri);
                    }
                }
            });

    Uri mUri;

    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        recylerViewMenu = view.findViewById(R.id.recylerViewMenu);
        btnAddNewMenuChangePage = view.findViewById(R.id.btnAddNewMenuChangePage);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recylerViewMenu.setLayoutManager(layoutManager);

        menuAdapter = new MenuAdapter(getActivity(), list);
        recylerViewMenu.setAdapter(menuAdapter);

        addMenuListFirebase();

        menuAdapter.setMenuSelectionOption(this);

        btnAddNewMenuChangePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageAddNewMenu();
            }
        });

        return view;
    }

    private void addMenuListFirebase() {
        databaseReference.child("menu").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuDrink menuDrink = snapshot.getValue(MenuDrink.class);
                if (menuDrink != null) {
                    list.add(menuDrink);
                    menuAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MenuDrink menuDrink = snapshot.getValue(MenuDrink.class);
                if (menuDrink == null || menuDrink == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    if (menuDrink.getMenuDrinkKey() == list.get(i).getMenuDrinkKey()) {
                        list.set(i, menuDrink);
                        break;
                    }
                }

                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                MenuDrink menuDrink = snapshot.getValue(MenuDrink.class);
                if (menuDrink == null || menuAdapter == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    if (menuDrink.getMenuDrinkKey() == list.get(i).getMenuDrinkKey()) {
                        list.remove(list.get(i));
                        break;
                    }
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onClickDelete(MenuDrink menuDrink) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("X??a Kh???i Menu");
        dialog.setMessage("X??c nh???n x??a?");
        dialog.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StorageReference deleteStorage = storageReference.child("uploads").getStorage().getReferenceFromUrl(menuDrink.getImageDrink());
                deleteStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child("menu").child(menuDrink.getMenuDrinkKey()).removeValue();
                        Toast.makeText(getActivity(), "X??a Th??nh C??ng", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onClickEdit(MenuDrink menuDrink) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_menu);
        dialog.setCanceledOnTouchOutside(false);

        EditText editNameDrink = dialog.findViewById(R.id.editNameDrink);
        EditText editTagDrink = dialog.findViewById(R.id.editTagDrink);
        EditText editPriceDrink = dialog.findViewById(R.id.editPriceDrink);
        ImageView imageViewMenu = dialog.findViewById(R.id.imageViewMenu);
        Button btnAddMenu = dialog.findViewById(R.id.btnAddMenu);
        Button btnCancelAddMenu = dialog.findViewById(R.id.btnCancelAddMenu);

        editNameDrink.setText(menuDrink.getNameDrink());
        editTagDrink.setText(menuDrink.getTagDrink());
        editPriceDrink.setText(menuDrink.getPriceDrink() + "");
        Glide.with(getActivity()).load(menuDrink.getImageDrink()).error(R.drawable.imagedefault).into(imageViewMenu);

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mResultLauncher.launch(Intent.createChooser(intent, "select new Image"));
                Glide.with(getActivity()).load(getmUri()).error(R.drawable.imagedefault).into(imageViewMenu);
            }
        });

        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namedrink = editNameDrink.getText().toString();
                String tagdrink = editTagDrink.getText().toString();
                int pricedrink = Integer.parseInt(editPriceDrink.getText().toString());

                if (mUri != null && namedrink != null && tagdrink != null) {
                    StorageReference deleteImageBeforeUpdate = storageReference.child("uploads").getStorage().getReferenceFromUrl(menuDrink.getImageDrink());
                    deleteImageBeforeUpdate.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            StorageReference updateNewImage = storageReference.child("uploads").child(System.currentTimeMillis() + "." + getExtension(getmUri()));
                            updateNewImage.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    updateNewImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String menuKey = menuDrink.getMenuDrinkKey();
                                            String imageUri = uri.toString();

                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("tagDrink", tagdrink);
                                            result.put("nameDrink", namedrink);
                                            result.put("imageDrink", imageUri);
                                            result.put("priceDrink", pricedrink);

                                            databaseReference.child("menu").child(menuKey).updateChildren(result);
                                            Toast.makeText(getActivity(), "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "???nh Kh??ng T???n T???i", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                } else if (namedrink.isEmpty() && mUri == null || tagdrink.isEmpty() && mUri == null){
                    Toast.makeText(getActivity(), "Thi???u d??? li???u", Toast.LENGTH_SHORT).show();
                } else if (mUri == null && namedrink != null && tagdrink != null) {
                    String menuKey = menuDrink.getMenuDrinkKey();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("tagDrink", tagdrink);
                    result.put("nameDrink", namedrink);
                    result.put("priceDrink", pricedrink);

                    databaseReference.child("menu").child(menuKey).updateChildren(result);
                    Toast.makeText(getActivity(), "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        btnCancelAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void changePageAddNewMenu() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_replace_menu, new AddMenuFragment());
        fragmentTransaction.addToBackStack(AddMenuFragment.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menuAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
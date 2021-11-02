package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionAdmin;
import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionStaff;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.UserSelectOption;
import tdtu.com.finalprojectby518h0090.adapter.UserAdapter;
import tdtu.com.finalprojectby518h0090.model.User;

public class ListUserFragment extends Fragment implements UserSelectOption {

    RecyclerView recycler_user_list;
    Button btnAddNewUserPage;
    UserAdapter userAdapter;
    List<User> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user, container, false);

        btnAddNewUserPage = view.findViewById(R.id.btnAddNewUserPage);
        recycler_user_list = view.findViewById(R.id.recycler_user_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();

        list = new ArrayList<>();

        userAdapter = new UserAdapter(getActivity(), list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_user_list.setLayoutManager(layoutManager);

        recycler_user_list.setAdapter(userAdapter);

        btnAddNewUserPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_add_test, new AddUserFragment());
                fragmentTransaction.addToBackStack(AddUserFragment.class.getName());
                fragmentTransaction.commit();
            }
        });

        addDataToUserList();

        userAdapter.setUserSelectOption(this);

        return view;
    }

    private void addDataToUserList() {
        databaseReference.child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    list.add(user);
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0 ; i < list.size(); i++) {
                    if (user.getUserKey() == list.get(i).getUserKey()) {
                        list.set(i, user);
                        break;
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null || list.isEmpty()) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    if (user.getUserKey() == list.get(i).getUserKey()) {
                        list.remove(list.get(i));
                        break;
                    }
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void reAuthentication(String email, String password) {
        FirebaseUser userNew = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        userNew.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User Ok", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClickEditEmail(int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_email);
        dialog.setCanceledOnTouchOutside(false);

        EditText editUserEmail = dialog.findViewById(R.id.editUserEmail);
        Button btnEditUserEmail = dialog.findViewById(R.id.btnEditUserEmail);
        Button btnCancelEditUserEmail = dialog.findViewById(R.id.btnCancelEditUserEmail);

        editUserEmail.setText(list.get(position).getUserEmail());

        btnEditUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmailChange = editUserEmail.getText().toString().trim();
                if (newEmailChange == null) {
                    Toast.makeText(getActivity(), "Email Trống", Toast.LENGTH_SHORT).show();
                } else {
                    reAuthentication(list.get(position).getUserEmail(), list.get(position).getUserPassword());
                    FirebaseUser userNew = FirebaseAuth.getInstance().getCurrentUser();

                    userNew.updateEmail(newEmailChange)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("userEmail", newEmailChange);

                                        databaseReference.child("user").child(list.get(position).getUserKey()).updateChildren(result);

                                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });


        btnCancelEditUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClickEditPassword(User user) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_edit_password);
        dialog.setCanceledOnTouchOutside(false);

        EditText editUserPassword = dialog.findViewById(R.id.editUserPassword);
        Button btnEditUserPassword = dialog.findViewById(R.id.btnEditUserPassword);
        Button btnCancelEditUserPassword = dialog.findViewById(R.id.btnCancelEditUserPassword);

        editUserPassword.setText(user.getUserPassword());

        btnEditUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = editUserPassword.getText().toString().trim();
                if (newPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Password Trống", Toast.LENGTH_SHORT).show();
                } else {

                    reAuthentication(user.getUserEmail(), user.getUserPassword());
                    FirebaseUser Newuser = FirebaseAuth.getInstance().getCurrentUser();

                    Newuser.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        HashMap<String, Object> result = new HashMap<>();
                                        result.put("userPassword", newPassword);

                                        databaseReference.child("user").child(user.getUserKey()).updateChildren(result);

                                        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Không thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

        btnCancelEditUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClickDelete(User user) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Xóa Người Dùng");
        dialog.setMessage("Xác Nhận Xóa ?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reAuthentication(user.getUserEmail(), user.getUserPassword());
                FirebaseUser userNew = FirebaseAuth.getInstance().getCurrentUser();

                userNew.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child("user").child(user.getUserKey()).removeValue();
                                    Toast.makeText(getActivity(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public void onChangePermission(User user) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_change_permission);
        dialog.setCanceledOnTouchOutside(false);

        EditText editUserPermission = dialog.findViewById(R.id.editUserPermission);
        Button btnEditUserPermission = dialog.findViewById(R.id.btnEditUserPermission);
        Button btnCancelEditUserPermission = dialog.findViewById(R.id.btnCancelEditUserPermission);

        editUserPermission.setText(user.getUserPermission());

        btnEditUserPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPermission = editUserPermission.getText().toString().trim();
                if (userPermission.equals(userPermissionAdmin) || userPermission.equals(userPermissionStaff)) {

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("userPermission", userPermission);

                    databaseReference.child("user").child(user.getUserKey()).updateChildren(result);
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Bạn chỉ nên nhập admin hoặc staff", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelEditUserPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
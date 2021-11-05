package tdtu.com.finalprojectby518h0090.fragment;

import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionAdmin;
import static tdtu.com.finalprojectby518h0090.DefaultTag.userPermissionStaff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    UserSelectOption userSelectOption = this;

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

        addUserFirebase();

        userAdapter.setUserSelectOption(userSelectOption);

        return view;
    }

    private void addUserFirebase() {
        FirebaseDatabase.getInstance().getReference("user").addChildEventListener(new ChildEventListener() {
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

                for (int i = 0; i < list.size(); i++) {
                    if (user.getUserKey() == list.get(i).getUserKey()) {
                        list.set(i , user);
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

    @Override
    public void onClickManageInfor(User user) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_user_information_edit);

        EditText userFullNameEdit = dialog.findViewById(R.id.userFullNameEdit);
        EditText userBirthEdit = dialog.findViewById(R.id.userBirthEdit);
        EditText userPhoneEdit = dialog.findViewById(R.id.userPhoneEdit);
        EditText userAddressEdit = dialog.findViewById(R.id.userAddressEdit);
        Button btnUpdateNewUser = dialog.findViewById(R.id.btnUpdateNewUser);
        Button btnCancelNewUser = dialog.findViewById(R.id.btnCancelNewUser);

        userFullNameEdit.setText(user.getUserFullname());
        userBirthEdit.setText(user.getUserBirth());
        userPhoneEdit.setText(String.valueOf(user.getUserPhone()));
        userAddressEdit.setText(user.getUserAddress());

        userBirthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int yearGet = calendar.get(Calendar.YEAR);
                int monthGet = calendar.get(Calendar.MONTH);
                int dateGet = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month , dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        userBirthEdit.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, yearGet, monthGet, dateGet);
                datePickerDialog.show();
            }
        });

        btnCancelNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdateNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = userFullNameEdit.getText().toString();
                String birthDay = userBirthEdit.getText().toString();
                int phone = Integer.parseInt(userPhoneEdit.getText().toString());
                String address = userAddressEdit.getText().toString();

                if (fullName.isEmpty() || birthDay.isEmpty() || phone == 0 || address.isEmpty()) {
                    Toast.makeText(getActivity(), "Thiếu Thông Tin", Toast.LENGTH_SHORT).show();
                } else {
                    user.setUserFullname(fullName);
                    user.setUserBirth(birthDay);
                    user.setUserPhone(phone);
                    user.setUserAddress(address);
                    FirebaseDatabase.getInstance().getReference("user").child(user.getUserKey()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Thành Công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Thất Bại có lỗi" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClickManageAuthen(User user) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        UserAuthenticationFragment userAuthenticationFragment = new UserAuthenticationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("AuthenUser", user);
        userAuthenticationFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_add_test, userAuthenticationFragment);
        fragmentTransaction.addToBackStack(UserAuthenticationFragment.class.getName());
        fragmentTransaction.commit();
    }
}
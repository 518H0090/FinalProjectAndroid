package tdtu.com.finalproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalproject.R;

public class ListUserFragment extends Fragment {

    ListView listViewUser;
    ArrayAdapter adapter;
    List<String> listGmai;
    Button btnAddNewUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_list_user, container, false);

        listViewUser = view.findViewById(R.id.listViewUser);
        btnAddNewUser = view.findViewById(R.id.btnAddNewUser);

        listGmai = new ArrayList<String>(TempTest());

        adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listGmai
        );
        listViewUser.setAdapter(adapter);

        btnAddNewUser.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_add_test, new AddUserFragment());
            fragmentTransaction.addToBackStack(AddUserFragment.class.getName());
            fragmentTransaction.commit();
        });

        return view;
    }

    private List<String> TempTest() {
        List<String> list = new ArrayList<>();

        for (int i = 0 ; i < 30; i++) {
            list.add("nhoxhieuro"+i+"@gmail.com");
        }

        return list;
    }

}
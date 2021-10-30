package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import tdtu.com.finalprojectby518h0090.R;

public class MenuFragment extends Fragment {

    Button btnAddNewMenuChangePage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        btnAddNewMenuChangePage = view.findViewById(R.id.btnAddNewMenuChangePage);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_replace_menu, new MenuListFragment());
        fragmentTransaction.commit();

        btnAddNewMenuChangePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageAddNewMenu();
            }
        });

        return view;
    }

    private void changePageAddNewMenu() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_replace_menu, new AddMenuFragment());
        fragmentTransaction.addToBackStack(AddMenuFragment.class.getName());
        fragmentTransaction.commit();
    }
}
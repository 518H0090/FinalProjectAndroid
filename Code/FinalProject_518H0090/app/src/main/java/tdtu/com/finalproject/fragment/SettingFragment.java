package tdtu.com.finalproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalproject.R;
import tdtu.com.finalproject.adapter.OptionAdapterSpinner;
import tdtu.com.finalproject.model.OptionDo;

public class SettingFragment extends Fragment  {

    private Spinner spinner;
    private List<OptionDo> doList;
    private OptionAdapterSpinner adapterSpinner;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        spinner = view.findViewById(R.id.option_choice_spinner);
        doList = new ArrayList<OptionDo>(addListToDo());

        adapterSpinner = new OptionAdapterSpinner(
                getActivity(),
                R.layout.recyler_view_setting_menu,
                doList
        );

        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OptionDo questionDo = doList.get(position);
                if (questionDo.getKeyOptions().equals("tableManager")) {
                    replaceFragment(new UserFragment());
                } else if (questionDo.getKeyOptions().equals("menuManager")) {

                } else if (questionDo.getKeyOptions().equals("userManager")) {

                } else if (questionDo.getKeyOptions().equals("inforManager")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private List<OptionDo> addListToDo() {
        List<OptionDo> list = new ArrayList<>();
        list.add(new OptionDo("tableManager","Quản lý khu vực - Điểm bán"));
        list.add(new OptionDo("menuManager","Quản lý Menu"));
        list.add(new OptionDo("userManager","Quản lý người dùng"));
        list.add(new OptionDo("inforManager","Quản lý thông tin quán"));
        return list;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.option_content_show, fragment);
        fragmentTransaction.commit();
    }


}
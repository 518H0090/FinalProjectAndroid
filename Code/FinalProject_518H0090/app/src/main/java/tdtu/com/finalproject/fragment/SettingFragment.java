package tdtu.com.finalproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tdtu.com.finalproject.R;
import tdtu.com.finalproject.adapter.OptionAdapter;
import tdtu.com.finalproject.model.OptionDo;

public class SettingFragment extends Fragment {

    private RecyclerView recyclerView;
    private OptionAdapter adapter;
    private List<OptionDo> listOption;

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

        recyclerView = view.findViewById(R.id.recyler_view_Option);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OptionAdapter(getActivity() ,addListOption());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<OptionDo> addListOption() {
        List<OptionDo> doList = new ArrayList<>();
        doList.add(new OptionDo("tableManager" , "Quản Lý Khu Vực - Điểm Bán"));
        doList.add(new OptionDo("menuManager" , "Quản Lý Menu Quán"));
        doList.add(new OptionDo("userManager" , "Quản Lý Người Dùng"));
        doList.add(new OptionDo("inforManager" , "Quản Lý Thông Tin Quán"));
        return doList;
    }
}
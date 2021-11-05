package tdtu.com.finalprojectby518h0090.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import tdtu.com.finalprojectby518h0090.R;
import tdtu.com.finalprojectby518h0090.adapter.ViewPager2Adapter;

public class SettingFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

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

        tabLayout = view.findViewById(R.id.tablayoutMediator);
        viewPager2 = view.findViewById(R.id.view_pager2);

        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(getActivity());
        viewPager2.setAdapter(viewPager2Adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Location");
                        break;
                    case 1:
                        tab.setText("Menu");
                        break;
                    case 2:
                        tab.setText("User");
                        break;
                }
            }
        }).attach();

        return view;
    }
}
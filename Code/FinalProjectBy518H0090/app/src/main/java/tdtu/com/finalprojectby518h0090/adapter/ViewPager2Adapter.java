package tdtu.com.finalprojectby518h0090.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import tdtu.com.finalprojectby518h0090.fragment.MenuFragment;
import tdtu.com.finalprojectby518h0090.fragment.TableFragment;
import tdtu.com.finalprojectby518h0090.fragment.UserFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TableFragment();
            case 1:
                return new MenuFragment();
            case 2:
                return new UserFragment();
            default:
                return new TableFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

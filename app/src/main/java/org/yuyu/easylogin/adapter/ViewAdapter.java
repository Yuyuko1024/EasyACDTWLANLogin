package org.yuyu.easylogin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends FragmentStateAdapter {

    private List<Class> fragments;

    public ViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    public void addFragment(Fragment fragment) {
        if (fragments != null) {
            fragments.add(fragment.getClass());
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            return (Fragment) fragments.get(position).newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}

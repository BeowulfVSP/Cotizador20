package com.vspcom.cotizador20.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vspcom.cotizador20.Fragments.CCTV;
import com.vspcom.cotizador20.Fragments.comingsong;

public class FragmentAdapter extends FragmentPagerAdapter {
    private boolean showComingSoon;

    public FragmentAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        showComingSoon = false;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CCTV();
            case 1:
                return showComingSoon ? new comingsong() : null;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return showComingSoon ? 2 : 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "CCTV";
            case 1:
                return "Coming Soon";
            default:
                return null;
        }
    }

    public void setShowComingSoon(boolean show) {
        showComingSoon = show;
        notifyDataSetChanged();
    }
}

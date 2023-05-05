package com.example.hw9;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return Placeholder.newInstance(position + 1);
            case 1:
                return new Favorites();
            default:
                return null;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "SEARCH";
            case 1: return "FAVOURITES";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
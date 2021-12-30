package com.example.sarkaribook.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sarkaribook.YoutubeLinksFragment;
import com.example.sarkaribook.GoogleDocFragment;
import com.example.sarkaribook.SimplePDFFragment;

public class MyAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SimplePDFFragment homeFragment = new SimplePDFFragment();
                return homeFragment;
            case 1:
                GoogleDocFragment homeFragment1 = new GoogleDocFragment();
                return homeFragment1;
            case 2:
               YoutubeLinksFragment homeFragment2 = new YoutubeLinksFragment();
                return homeFragment2;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}

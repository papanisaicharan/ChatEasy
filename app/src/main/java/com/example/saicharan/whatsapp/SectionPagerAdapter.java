package com.example.saicharan.whatsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by saicharan on 13-08-2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter{

    public SectionPagerAdapter(FragmentManager fm) {

        super(fm);
    }



    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:

                RequestFragments r = new RequestFragments();
                return r;

            case 1:

                ChatsFragment c = new ChatsFragment();
                return c;

            case 2:

                FriendsFragments f = new FriendsFragments();
                return f;

            default:

                return null;
        }

    }


    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "REQUESTS";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";

            default:
                return null;
        }
    }
}

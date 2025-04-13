package com.example.cusocial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPager_Messenger_Adapter extends FragmentPagerAdapter {
    public ViewPager_Messenger_Adapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0) {
            return new Group_fragment();
        }
        else  {
            return new Chats_fragment();
        }
//        else{
//            return new Calls_fragment();
//        }
//        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        if (position==0) {
            return "Groups";
        }
//        else if (position==1) {
//            return "Chats";
//        }
        else{
            return "Chats";
        }
    }
}

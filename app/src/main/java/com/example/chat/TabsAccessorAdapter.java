package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabsAccessorAdapter extends FragmentStatePagerAdapter {
private ArrayList<Fragment> arrayList;

    public TabsAccessorAdapter(@NonNull FragmentManager fm,ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList=arrayList;
    }
    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }
public Fragment createFragment(int i){
        return arrayList.get(i);
}

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ChatFragment chatFragment=new ChatFragment();
                return  chatFragment;
            case 1:
                GroupsFragment groupsFragment=new GroupsFragment();
                return  groupsFragment;
            case 2:
                ContactsFragment contactsFragment=new ContactsFragment();
                return  contactsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return  "שיחות";
            case 1:
                GroupsFragment groupsFragment=new GroupsFragment();
                return  "קבוצות";
            case 2:
                return  "אנשי קשר";
            default:
                return null;
        }
    }
}

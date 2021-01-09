package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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

                    ChatFragment chatFragment = new ChatFragment();
                    return chatFragment;

            case 1:

                    MyGroupsFragment myGroupsFragment =new MyGroupsFragment();

                    return myGroupsFragment;


            case 2:

                    AllGroupsFragment allGroupsFragment=new AllGroupsFragment();
                    return  allGroupsFragment;

            case 3:

                    LinksWhatsAppFragment linksWhatsAppFragment=new LinksWhatsAppFragment();
                    return  linksWhatsAppFragment;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return  "שיחות";
            case 1:
                return  "הקבוצות שלי";

            case 2:
                return  "כל הקבוצות";
            case 3:
                return  "קישורים לווצאפ";
            default:
                return null;
        }
    }
}

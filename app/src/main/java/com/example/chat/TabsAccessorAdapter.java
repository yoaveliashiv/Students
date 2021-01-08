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
                try {
                    ChatFragment chatFragment = new ChatFragment();
                    return chatFragment;
                }catch (RuntimeException e){
                    return null;
                }
            case 1:
                try {
                    MyGroupsFragment myGroupsFragment =new MyGroupsFragment();

                    return myGroupsFragment;
                }catch (RuntimeException e){
                    return null;
                }

            case 2:
                try {
                    AllGroupsFragment allGroupsFragment=new AllGroupsFragment();
                    return  allGroupsFragment;
                }catch (RuntimeException e){
                    return null;
                }
            case 3:
                try {
                    LinksWhatsAppFragment linksWhatsAppFragment=new LinksWhatsAppFragment();
                    return  linksWhatsAppFragment;
                }catch (RuntimeException e){
                    return null;

                }

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

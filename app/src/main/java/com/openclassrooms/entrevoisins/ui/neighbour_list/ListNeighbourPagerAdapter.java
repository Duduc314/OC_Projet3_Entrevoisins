package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListNeighbourPagerAdapter extends FragmentPagerAdapter {

    private NeighbourFragment mNeighbourFragment = NeighbourFragment.newInstance();
    private FavoriteFragment mFavoriteFragment = FavoriteFragment.newInstance();

    public ListNeighbourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position : Fragment position in TabLayout
     * @return : Fragment
     */
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return mNeighbourFragment;
        }
        else{
            return mFavoriteFragment;
        }
    }

    /**
     * get the number of pages
     * @return : int
     */
    @Override
    public int getCount() {
        return 2;
    }

    public FavoriteFragment getFavoriteFragment(){
        return this.mFavoriteFragment;
    }

}
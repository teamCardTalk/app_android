package com.team.cardTalk;

import android.support.v4.app.FragmentManager;

/**
 * Created by eunjooim on 15. 4. 1..
 */
public class Stock
{
    private static FragmentManager fragmentManager;

    public void Stock(){}

    public static void initiateFragmentManager(FragmentManager fm){
        fragmentManager = fm;
    }

    public static FragmentManager getFragmentManager(){
        return fragmentManager;
    }
}

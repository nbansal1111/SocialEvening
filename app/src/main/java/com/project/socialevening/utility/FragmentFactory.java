package com.project.socialevening.utility;

import android.support.v4.app.Fragment;

import com.project.socialevening.fragments.BaseFragment;
import com.project.socialevening.fragments.HomeFragment;
import com.project.socialevening.fragments.TeamListFragment;

/**
 * Created by nitin on 31/10/15.
 */
public class FragmentFactory {

    public static Fragment getFragment(int fragmentType) {
        switch (fragmentType) {
            case AppConstants.FRAGMENT_TYPE.HOME_FRAGMENT:
                return new HomeFragment();
            case AppConstants.FRAGMENT_TYPE.MY_TEAMS:
                return new TeamListFragment();

        }
        return null;
    }
}

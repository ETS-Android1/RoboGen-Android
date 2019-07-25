package at.srfg.robogen.fitnesswatch;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import at.srfg.robogen.fitnesswatch.fragments.ActivitiesFragment;
import at.srfg.robogen.fitnesswatch.fragments.DeviceFragment;
import at.srfg.robogen.fitnesswatch.fragments.InfoFragment;
import at.srfg.robogen.fitnesswatch.fragments.ProfileFragment;
import at.srfg.robogen.fitnesswatch.fragments.WeightLogFragment;

//import android.support.v13.app.FragmentPagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/*******************************************************************************
 * UserDataPagerAdapter Class
 ******************************************************************************/
public class UserDataPagerAdapter extends FragmentPagerAdapter {

    private final List<InfoFragment> fragments = new ArrayList<>();

    /*******************************************************************************
     * CTOR
     ******************************************************************************/
    public UserDataPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        if (containsScope(Scope.profile)) {
            fragments.add(new ProfileFragment());
        }
        if (containsScope(Scope.settings)) {
            fragments.add(new DeviceFragment());
        }
        if (containsScope(Scope.activity)) {
            fragments.add(new ActivitiesFragment());
        }
        if (containsScope(Scope.weight)) {
            fragments.add(new WeightLogFragment());
        }
    }

    /*******************************************************************************
     * override
     ******************************************************************************/
    @Override
    public Fragment getItem(int position) {
        if (position >= fragments.size()) {
            return null;
        }

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /*******************************************************************************
     * helpers
     ******************************************************************************/
    private boolean containsScope(Scope scope) {
        return AuthenticationManager.getCurrentAccessToken().getScopes().contains(scope);
    }

    public int getTitleResourceId(int index) {
        return fragments.get(index).getTitleResourceId();
    }
}

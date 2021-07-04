package at.srfg.robogen.fitnesswatch;

import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.SleepFragment;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.ActivitiesFragment;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.DeviceFragment;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.HeartrateFragment;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.InfoFragment;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.ProfileFragment;
import at.srfg.robogen.fitnesswatch.fitbit_API.fragments.WeightLogFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/*******************************************************************************
 * UserDataPagerAdapter Class
 ******************************************************************************/
public class UserDataPagerAdapter extends FragmentPagerAdapter {

    private final List<InfoFragment> m_listFragments = new ArrayList<>();

    /*******************************************************************************
     * CTOR
     ******************************************************************************/
    public UserDataPagerAdapter(FragmentManager fm) {
        super(fm);

        m_listFragments.clear();
        if (containsScope(Scope.profile)) {
            m_listFragments.add(new ProfileFragment());
        }
        if (containsScope(Scope.settings)) {
            m_listFragments.add(new DeviceFragment());
        }
        if (containsScope(Scope.activity)) {
            m_listFragments.add(new ActivitiesFragment());
        }
        if (containsScope(Scope.weight)) {
            m_listFragments.add(new WeightLogFragment());
        }
        if (containsScope(Scope.heartrate)) {
            m_listFragments.add(new HeartrateFragment());
        }
        if (containsScope(Scope.sleep)) {
            m_listFragments.add(new SleepFragment());
        }
    }

    /*******************************************************************************
     * override
     ******************************************************************************/
    @Override
    public Fragment getItem(int position) {
        if (position >= m_listFragments.size()) {
            return null;
        }

        return m_listFragments.get(position);
    }

    @Override
    public int getCount() {
        return m_listFragments.size();
    }

    /*******************************************************************************
     * helpers
     ******************************************************************************/
    private boolean containsScope(Scope scope) {
        return AuthenticationManager.getCurrentAccessToken().getScopes().contains(scope);
    }

    public int getTitleResourceId(int index) {
        return m_listFragments.get(index).getTitleResourceId();
    }
}

package at.srfg.robogen.fitnesswatch;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import at.srfg.robogen.fitnesswatch.fragments.ActivitiesFragment;
import at.srfg.robogen.fitnesswatch.fragments.DeviceFragment;
import at.srfg.robogen.fitnesswatch.fragments.HeartrateFragment;
import at.srfg.robogen.fitnesswatch.fragments.InfoFragment;
import at.srfg.robogen.fitnesswatch.fragments.ProfileFragment;
import at.srfg.robogen.fitnesswatch.fragments.WeightLogFragment;

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

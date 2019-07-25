package at.srfg.robogen.fitnesswatch;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.AuthenticationManager;

//import static at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope.activity;
//import com.fitbit.sampleandroidoauth2.databinding.ActivityUserDataBinding;
//import android.databinding.DataBindingUtil;
//import android.support.v4.view.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import at.srfg.robogen.R;

/*******************************************************************************
 * UserDataActivity Class
 ******************************************************************************/
public class UserDataActivity extends AppCompatActivity {

    //private ActivityUserDataBinding binding;
    private ViewPager mViewPager;
    private UserDataPagerAdapter userDataPagerAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, UserDataActivity.class);
    }

    /*******************************************************************************
     * on create activity
     ******************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_user_data);

        userDataPagerAdapter = new UserDataPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.data_viewPager);
        mViewPager.setAdapter(userDataPagerAdapter);
        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });

        addTabs();
    }

    /*******************************************************************************
     * add tabs to ActionBar
     ******************************************************************************/
    private void addTabs() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        int numberOfTabs = userDataPagerAdapter.getCount();
        for (int i = 0; i < numberOfTabs; i++) {
            final int index = i;
            actionBar.addTab(actionBar.newTab()
                    .setText(getString(userDataPagerAdapter.getTitleResourceId(i)))
                    .setTabListener(new ActionBar.TabListener() {
                        @Override
                        public void onTabSelected(ActionBar.Tab tab, androidx.fragment.app.FragmentTransaction ft) {
                            mViewPager.setCurrentItem(index);
                        }
                        @Override public void onTabUnselected(ActionBar.Tab tab, androidx.fragment.app.FragmentTransaction ft) {}
                        @Override public void onTabReselected(ActionBar.Tab tab, androidx.fragment.app.FragmentTransaction ft) {}
                    }));
        }
    }

    /*******************************************************************************
     * onLogoutClick
     ******************************************************************************/
    public void onLogoutClick(View view) {
        AuthenticationManager.logout(this);
    }
}

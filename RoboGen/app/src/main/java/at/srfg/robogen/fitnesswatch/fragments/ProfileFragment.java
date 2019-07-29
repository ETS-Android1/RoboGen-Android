package at.srfg.robogen.fitnesswatch.fragments;


import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.user_summary.User;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.user_summary.UserContainer;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.UserService;

import androidx.loader.content.Loader;
import android.os.Bundle;


/*******************************************************************************
 * ProfileFragment Class
 ******************************************************************************/
public class ProfileFragment extends InfoFragment<UserContainer> {

    /*******************************************************************************
     * overrides for InfoFragment
     ******************************************************************************/
    @Override
    public int getTitleResourceId() { return R.string.user_info; }

    @Override
    protected int getLoaderId() {
        return 1;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<UserContainer>> onCreateLoader(int id, Bundle args) {
        return UserService.getLoggedInUserLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<UserContainer>> loader, ResourceLoaderResult<UserContainer> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindProfileInfo(data.getResult().getUser());
        }
    }

    /*******************************************************************************
     * bindProfileInfo
     ******************************************************************************/
    public void bindProfileInfo(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        printKeys(stringBuilder, user);

        if(stringBuilder.length() == 0) { // Keine Daten
            stringBuilder.append("<b>&nbsp;&nbsp;</b>");
            stringBuilder.append(getString(R.string.no_data));
            stringBuilder.append("<br><br>");
        }

        setMainText(stringBuilder.toString());
    }
}

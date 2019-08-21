package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;


import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.user_summary.User;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.user_summary.UserContainer;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.weight_logs.Weight;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.UserService;

import androidx.loader.content.Loader;
import android.os.Bundle;

import java.util.List;


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

        clearList();

        // if no data, inform user
        if (user == null) {
            addTextToList(getString(R.string.no_data));
        }

        StringBuilder stringBuilder = new StringBuilder();
        printKeys(stringBuilder, user);
    }
}

package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.weight_logs.Weight;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.weight_logs.WeightLogs;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.WeightService;

import androidx.loader.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

/*******************************************************************************
 * WeightLogFragment Class
 ******************************************************************************/
public class WeightLogFragment extends InfoFragment<WeightLogs> {

    /*******************************************************************************
     * override
     ******************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //binding.webview.setVisibility(View.GONE);
        //binding.graph.setVisibility(View.VISIBLE);
        return v;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.weight_info;
    }

    @Override
    protected int getLoaderId() {
        return 4;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<WeightLogs>> onCreateLoader(int id, Bundle args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return WeightService.getWeightLogLoader(getActivity(), calendar.getTime(), Calendar.MONTH, 1);
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<WeightLogs>> loader, ResourceLoaderResult<WeightLogs> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindWeightLogs(data.getResult());
        }
    }

    /*******************************************************************************
     * bindWeightLogs
     ******************************************************************************/
    public void bindWeightLogs(WeightLogs weightLogs) {

        clearList();

        // if no data, inform user
        List<Weight> weights = weightLogs.getWeight();
        if (weights.size() == 0) {
            addTextToList(getString(R.string.no_data));
        }

        // run through data and create list entries
        for (Weight weight : weights) {

            String listEntry =
                    "BMI = " + weight.getBmi() + "\n" +
                    "Datum = " + weight.getDate() + "\n" +
                    "Datumszeit = " + weight.getDateTime() + "\n" +
                    "Fat = " + weight.getFat() + "\n" +
                    "LogID = " + weight.getLogId() + "\n" +
                    "Datenquelle = " + weight.getSource() + "\n" +
                    "Uhrzeit = " + weight.getTime() + "\n" +
                    "Gewicht = " + weight.getWeight().doubleValue() + "\n" ;

            addTextToList(listEntry);
        }
    }
}

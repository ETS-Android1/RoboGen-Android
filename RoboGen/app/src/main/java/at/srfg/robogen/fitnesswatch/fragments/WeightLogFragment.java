package at.srfg.robogen.fitnesswatch.fragments;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
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

        StringBuilder stringBuilder = new StringBuilder();
        List<Weight> weights = weightLogs.getWeight();

        for (Weight weight : weights) {

            stringBuilder.append("<b>&nbsp;&nbsp;BMI: </b>");
            stringBuilder.append(weight.getBmi());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Date: </b>");
            stringBuilder.append(weight.getDate());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;DateTime: </b>");
            stringBuilder.append(weight.getDateTime());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Fat: </b>");
            stringBuilder.append(weight.getFat());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;LogID: </b>");
            stringBuilder.append(weight.getLogId());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Source: </b>");
            stringBuilder.append(weight.getSource());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Time: </b>");
            stringBuilder.append(weight.getTime());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Weight: </b>");
            stringBuilder.append(weight.getWeight().doubleValue());
            stringBuilder.append("<br><hr>");
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.replace(stringBuilder.length() - 8, stringBuilder.length(), "");
        }
        else { // Keine Daten
            stringBuilder.append("<b>&nbsp;&nbsp;</b>");
            stringBuilder.append(getString(R.string.no_data));
            stringBuilder.append("<br><br>");
        }

        setMainText(stringBuilder.toString());
    }
}

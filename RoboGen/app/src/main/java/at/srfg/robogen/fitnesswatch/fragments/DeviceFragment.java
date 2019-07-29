package at.srfg.robogen.fitnesswatch.fragments;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.devices_list.Device;
import at.srfg.robogen.fitnesswatch.fitbit_API.services.DeviceService;

import androidx.loader.content.Loader;
import android.os.Bundle;

/*******************************************************************************
 * DeviceFragment Class
 ******************************************************************************/
public class DeviceFragment extends InfoFragment<Device[]> {

    /*******************************************************************************
     * overrides for InfoFragment
     ******************************************************************************/
    @Override
    public int getTitleResourceId() {
        return R.string.devices_info;
    }

    @Override
    protected int getLoaderId() {
        return 2;
    }

    @Override
    public androidx.loader.content.Loader<ResourceLoaderResult<Device[]>> onCreateLoader(int id, Bundle args) {
        return DeviceService.getUserDevicesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<Device[]>> loader, ResourceLoaderResult<Device[]> data) {
        super.onLoadFinished(loader, data);
        if (data.isSuccessful()) {
            bindDevices(data.getResult());
        }
    }

    /*******************************************************************************
     * bindDevices
     ******************************************************************************/
    public void bindDevices(Device[] devices) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Device device : devices) {
            stringBuilder.append("<b>FITBIT ");
            stringBuilder.append(device.getDeviceVersion().toUpperCase());
            stringBuilder.append("&trade;</b><br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Type: </b>");
            stringBuilder.append(device.getType().toLowerCase());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Last Sync: </b>");
            stringBuilder.append(device.getLastSyncTime());
            stringBuilder.append("<br>");

            stringBuilder.append("<b>&nbsp;&nbsp;Battery Level: </b>");
            stringBuilder.append(device.getBattery());
            stringBuilder.append("<br><br>");
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

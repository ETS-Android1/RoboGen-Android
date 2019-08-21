package at.srfg.robogen.fitnesswatch.fitbit_API.fragments;

import at.srfg.robogen.R;
import at.srfg.robogen.fitnesswatch.fitbit_API.common.loaders.ResourceLoaderResult;
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

        clearList();

        // if no data, inform user
        if (devices.length == 0) {
            addTextToList(getString(R.string.no_data));
        }

        // run through data and create list entries
        for (Device device : devices) {

            String listEntry =
                    "Geräte-Name = " + device.getDeviceVersion().toUpperCase() + "\n" +
                    "Geräte-Typ = " + device.getType().toLowerCase() + "\n" +
                    "Letzer Sync = " + device.getLastSyncTime() + "\n" +
                    "Batterie-Level = " + device.getBattery() + "\n";

            addTextToList(listEntry);
        }
    }
}

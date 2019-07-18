package at.srfg.robogen.fitnesswatch.fitbit_API.services;

import at.srfg.robogen.fitnesswatch.fitbit_API.exceptions.MissingScopesException;
import at.srfg.robogen.fitnesswatch.fitbit_API.exceptions.TokenExpiredException;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderFactory;
import at.srfg.robogen.fitnesswatch.fitbit_API.loaders.ResourceLoaderResult;
import at.srfg.robogen.fitnesswatch.fitbit_API.models.Device;
import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;

import android.app.Activity;
import android.content.Loader;

/**
 * Created by jboggess on 9/14/16.
 */
public class DeviceService {

    private final static String DEVICE_URL = "https://api.fitbit.com/1/user/-/devices.json";
    private static final ResourceLoaderFactory<Device[]> USER_DEVICES_LOADER_FACTORY = new ResourceLoaderFactory<>(DEVICE_URL, Device[].class);

    public static Loader<ResourceLoaderResult<Device[]>> getUserDevicesLoader(Activity activityContext) throws MissingScopesException, TokenExpiredException {
        return USER_DEVICES_LOADER_FACTORY.newResourceLoader(activityContext, new Scope[]{Scope.settings});
    }

}

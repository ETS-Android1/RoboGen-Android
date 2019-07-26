package at.srfg.robogen.fitnesswatch.fitbit_API.loaders;

import at.srfg.robogen.fitnesswatch.fitbit_Auth.Scope;
import android.app.Activity;
import android.os.Handler;

/*******************************************************************************
 * Class ResourceLoaderFactory
 ******************************************************************************/
public class ResourceLoaderFactory<T> {

    private String m_sURLFormat;
    private Class<T> m_tClassType;

    public ResourceLoaderFactory(String urlFormat, Class<T> classType) {
        this.m_sURLFormat = urlFormat;
        this.m_tClassType = classType;
    }

    public ResourceLoader<T> newResourceLoader(Activity contextActivity, Scope[] requiredScopes, String... pathParams) {

        String url = m_sURLFormat;
        if (pathParams != null && pathParams.length > 0) {
            url = String.format(m_sURLFormat, pathParams);
        }

        return new ResourceLoader<T>(contextActivity, url, requiredScopes, new Handler(), m_tClassType);
    }
}

package at.srfg.robogen.fitnesswatch.fitbit_Auth;

import at.srfg.robogen.fitnesswatch.fitbit_Common.BasicHttpRequestBuilder;

/**
 * Created by jboggess on 9/26/16.
 */

public interface RequestSigner {

    void signRequest(BasicHttpRequestBuilder builder);

}

package at.srfg.robogen.fitnesswatch.fitbit_Auth;

import at.srfg.robogen.fitnesswatch.fitbit_Common.BasicHttpRequestBuilder;

/*******************************************************************************
 * Interface RequestSigner
 ******************************************************************************/
public interface RequestSigner {

    void signRequest(BasicHttpRequestBuilder builder);

}

package at.srfg.robogen.fitnesswatch.fitbit_Auth;

import at.srfg.robogen.fitnesswatch.fitbit_Http.BasicHttpRequestBuilder;

/*******************************************************************************
 * Class SimpleRequestSigner
 ******************************************************************************/
public class SimpleRequestSigner implements RequestSigner {

    @Override
    public void signRequest(BasicHttpRequestBuilder builder) {
        AccessToken currentAccessToken = AuthenticationManager.getCurrentAccessToken();
        String bearer;
        if (currentAccessToken == null || currentAccessToken.hasExpired()) {
            bearer = "foo";
        } else {
            bearer = currentAccessToken.getAccessToken();
        }
        builder.setAuthorization(String.format("Bearer %s", bearer));
    }
}

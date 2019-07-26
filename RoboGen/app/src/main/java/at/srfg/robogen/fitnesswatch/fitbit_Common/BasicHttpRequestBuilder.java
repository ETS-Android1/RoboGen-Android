package at.srfg.robogen.fitnesswatch.fitbit_Common;


//import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*******************************************************************************
 * Class BasicHttpRequestBuilder
 ******************************************************************************/
public class BasicHttpRequestBuilder {

    private BasicHttpRequest m_cBasicHttpRequest;

    public static BasicHttpRequestBuilder create() {
        return new BasicHttpRequestBuilder();
    }

    public BasicHttpRequest build() {
        if (TextUtils.isEmpty(m_cBasicHttpRequest.getUrl())) {
            throw new IllegalArgumentException("Url cannot be empty!");
        }
        return m_cBasicHttpRequest;
    }

    private BasicHttpRequestBuilder() {
        m_cBasicHttpRequest = new BasicHttpRequest();
        m_cBasicHttpRequest.setMethod("GET");
    }

    /*******************************************************************************
     * setter
     ******************************************************************************/
    public BasicHttpRequestBuilder setUrl(String url) {
        m_cBasicHttpRequest.setUrl(url);
        return this;
    }

    public BasicHttpRequestBuilder setAuthorization(String authorization) {
        m_cBasicHttpRequest.setAuthorization(authorization);
        return this;
    }

    public BasicHttpRequestBuilder setMethod(String method) {
        m_cBasicHttpRequest.setMethod(method);
        return this;
    }

    public BasicHttpRequestBuilder setContentType(String contentType) {
        m_cBasicHttpRequest.setContentType(contentType);
        return this;
    }

    public BasicHttpRequestBuilder setContent(String content) throws UnsupportedEncodingException {
        m_cBasicHttpRequest.setContent(content);
        return this;
    }

    public BasicHttpRequestBuilder setContent(byte[] content) {
        m_cBasicHttpRequest.setContent(content);
        return this;
    }

    public BasicHttpRequestBuilder setUseCaches(boolean useCaches) {
        m_cBasicHttpRequest.setUseCaches(useCaches);
        return this;
    }

    /*******************************************************************************
     * addQueryParam
     ******************************************************************************/
    public BasicHttpRequestBuilder addQueryParam(String name, String value) {
        if (m_cBasicHttpRequest.getParams() == null) {
            m_cBasicHttpRequest.setParams(new ArrayList<Pair<String, String>>());
        }

        m_cBasicHttpRequest.getParams().add(new Pair<String, String>(name, value));
        return this;
    }
}

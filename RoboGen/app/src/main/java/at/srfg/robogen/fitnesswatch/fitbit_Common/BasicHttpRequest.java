package at.srfg.robogen.fitnesswatch.fitbit_Common;

//import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Class BasicHttpRequest
 ******************************************************************************/
public class BasicHttpRequest {

    private String m_sURL;
    private String m_sAuthorization;
    private String m_sMethod;
    private String m_sContentType;
    private byte[] m_arrContent;
    private boolean m_bUseCaches;
    private List<Pair<String, String>> m_listParams;

    BasicHttpRequest() {}

    /*******************************************************************************
     * getter/setter
     ******************************************************************************/
    public String getUrl() {
        return m_sURL;
    }
    void setUrl(String url) {
        this.m_sURL = url;
    }

    void setAuthorization(String authorization) {
        this.m_sAuthorization = authorization;
    }

    public String getMethod() {
        return m_sMethod;
    }
    void setMethod(String method) {
        this.m_sMethod = method;
    }

    public String getContentType() {
        return m_sContentType;
    }
    void setContentType(String contentType) {
        this.m_sContentType = contentType;
    }

    public int getContentLength() {
        return m_arrContent != null ? m_arrContent.length : 0;
    }

    void setContent(byte[] content) {
        this.m_arrContent = content;
    }
    void setContent(String content) throws UnsupportedEncodingException {
        setContent(content.getBytes("UTF-8"));
    }

    public boolean useCaches() {
        return m_bUseCaches;
    }
    void setUseCaches(boolean useCaches) {
        this.m_bUseCaches = useCaches;
    }

    void setParams(List<Pair<String, String>> params) {
        this.m_listParams = params;
    }
    public List<Pair<String, String>> getParams() {
        return m_listParams;
    }

    /*******************************************************************************
     * fillInConnectionInfo
     ******************************************************************************/
    private synchronized void fillInConnectionInfo(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod(m_sMethod);

        if (!TextUtils.isEmpty(m_sAuthorization)) {
            connection.setRequestProperty("Authorization", m_sAuthorization);
        }

        if (!TextUtils.isEmpty(m_sContentType)) {
            connection.setRequestProperty("Content-Type", m_sContentType);
        }

        if (m_arrContent == null || m_arrContent.length == 0) {
            connection.setRequestProperty("Content-Length", "0");
        } else {
            connection.setRequestProperty("Content-Length", Integer.toString(m_arrContent.length));
            OutputStream outputStream = null;

            try {
                outputStream = connection.getOutputStream();
                outputStream.write(this.m_arrContent);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        connection.setUseCaches(this.m_bUseCaches);
    }

    /*******************************************************************************
     * getQuery
     ******************************************************************************/
    private String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {
        List<String> keyValues = new ArrayList<>();

        for (Pair<String, String> pair : params) {
            keyValues.add(
                    URLEncoder.encode(pair.first, "UTF-8")
                            + "="
                            + URLEncoder.encode(pair.second, "UTF-8")
            );
        }

        return TextUtils.join("&", keyValues);
    }

    /*******************************************************************************
     * readBytes
     ******************************************************************************/
    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    /*******************************************************************************
     * execute
     ******************************************************************************/
    public synchronized BasicHttpResponse execute() throws IOException {
        HttpURLConnection connection = null;

        String urlString = this.m_sURL + ((m_listParams != null) ? "?" + getQuery(this.m_listParams) : "");

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            fillInConnectionInfo(connection);
            connection.connect();

            return new BasicHttpResponse(
                    connection.getResponseCode(),
                    readBytes(connection.getInputStream())
            );

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

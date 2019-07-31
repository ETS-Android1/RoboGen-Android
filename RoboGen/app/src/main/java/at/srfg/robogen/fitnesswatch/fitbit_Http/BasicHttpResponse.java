package at.srfg.robogen.fitnesswatch.fitbit_Http;

import java.io.UnsupportedEncodingException;

/*******************************************************************************
 * Class BasicHttpResponse
 ******************************************************************************/
public class BasicHttpResponse {

    private byte[] m_arrBody;
    private int m_iStatusCode;

    public BasicHttpResponse() {}
    public BasicHttpResponse(int statusCode, byte[] body) {
        this.m_arrBody = body;
        this.m_iStatusCode = statusCode;
    }

    public boolean isSuccessful() {
        return m_iStatusCode >= 100 && m_iStatusCode < 400;
    }

    /*******************************************************************************
     * getter/setter
     ******************************************************************************/
    public byte[] getBody() {
        return m_arrBody;
    }
    public void setBody(byte[] body) {
        this.m_arrBody = body;
    }

    public int getStatusCode() {
        return m_iStatusCode;
    }
    public void setStatusCode(int statusCode) {
        this.m_iStatusCode = statusCode;
    }

    public String getBodyAsString() throws UnsupportedEncodingException {
        return new String(this.m_arrBody, "UTF-8");
    }
}

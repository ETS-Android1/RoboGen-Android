package at.srfg.robogen.fitnesswatch.fitbit_Common;

import java.io.UnsupportedEncodingException;

/*******************************************************************************
 * Class BasicHttpResponse
 ******************************************************************************/
public class BasicHttpResponse {

    private byte[] body;
    private int statusCode;

    public BasicHttpResponse() {}
    public BasicHttpResponse(int statusCode, byte[] body) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public boolean isSuccessful() {
        return statusCode >= 100 && statusCode < 400;
    }

    /*******************************************************************************
     * getter/setter
     ******************************************************************************/
    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBodyAsString() throws UnsupportedEncodingException {
        return new String(this.body, "UTF-8");
    }
}

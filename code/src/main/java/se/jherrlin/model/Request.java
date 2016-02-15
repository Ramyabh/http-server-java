package se.jherrlin.model;

/**
 * Created by nils on 2/15/16.
 */
public class Request {
    private HTTPMethod method;
    private String uri;
    private String httpversion;

    public Request() {
    }

    public Request(HTTPMethod method, String uri, String httpversion) {
        this.method = method;
        this.uri = uri;
        this.httpversion = httpversion;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpversion() {
        return httpversion;
    }

    public void setHttpversion(String httpversion) {
        this.httpversion = httpversion;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", httpversion='" + httpversion + '\'' +
                '}';
    }
}

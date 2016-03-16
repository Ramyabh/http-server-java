package se.jherrlin.model;


import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    private HTTPMethod method;
    private String uri;
    private String httpversion;
    private String clientAddress;
    private String body;
    private ArrayList<String> headers = new ArrayList<String>();
    private int clientPort;
    private DataOutputStream dataOutputStream;

    public final String USERNAME = "admin";
    public final String PASSWORD = "admin";

    public HashMap<String, String> bodyDataMap = new HashMap<String, String>();
    public HashMap<String, String> headerDataMap = new HashMap<String, String>();

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

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<String> getHeaders() {
        return headers;
    }

    public void appendHeader(String header){
        headers.add(header);
    }

    public void setHeaders(ArrayList<String> headers) {
        this.headers = headers;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", httpversion='" + httpversion + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", clientPort=" + clientPort + '\'' +
                ", body=" + body +
                '}';
    }

    public enum HTTPMethod {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        TRACE,
        CONNECT,
        NOTVALID
    }
}



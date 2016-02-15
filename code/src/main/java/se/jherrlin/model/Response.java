package se.jherrlin.model;

/**
 * Created by nils on 2/15/16.
 */
public class Response {
    String header = new String("");
    byte[] body;

    public Response() {
    }

    public byte[] getHeader() {
        this.header += "\r\n";
        return header.getBytes();
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void appendHeader(String header){
        this.header += header += "\n";
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}

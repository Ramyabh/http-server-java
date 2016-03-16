package se.jherrlin.model;


import java.util.ArrayList;

public class Response {
    String response;
    ArrayList<String> headers = new ArrayList<String>();
    byte[] body;

    public Response() {
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void appendHeader(String header){
        this.headers.add(header);
    }

    public byte[] getHeaders() {
        String header = "";
        header += response;
        header += "\r\n";  // Response

        for (String s: headers){
            header += s;
            header += "\n";
        }

        header += "\r\n";

        return header.getBytes();
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String toString = "";
        toString += "Response{" + response + ",";
        for (String s : headers) {
            toString += s;
            toString += " ,";
        }

        toString += " }";
        return toString;
    }
}

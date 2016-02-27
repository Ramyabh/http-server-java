package se.jherrlin.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import se.jherrlin.model.Request;
import se.jherrlin.tcp.TCPServer;


public class RequestHandler {

    // This classes will be ugly, a lot of parsing

    public static Request RequestParser(String requestStringIn){

        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        String[] requestString;
        String headers = null;
        String body = null;
        Request requestObject = new Request();

        // Try to split header and body
        try{
            if (requestStringIn.contains("\r\n\r\n")){  // If we see the pattern that separates the header and the body
                requestString = requestStringIn.split("\r\n\r\n");  // Split at the separation pattern
                headers = requestString[0];
                if (requestString.length > 1){  // If we get data in the body.
                    body = requestString[1].trim();  // Trim the request and set it to bdy.
                }
            }
            else{
                headers = requestStringIn.trim();  // If we only get headers.
            }

        }
        catch (IndexOutOfBoundsException e){
            LOG.debug("Failed to split header and body in request.");
            LOG.debug(requestStringIn);
            e.printStackTrace();
        }

        // Try to split up and parse header
        if (headers != null){
            try{
                String[] header = headers.split("\r\n");
                String[] requestMethod = header[0].split("\\s+");

                // Parse the first row in the request header and add to
                // the requestObject.
                requestObject.setMethod(evalHTTPMethod(requestMethod[0]));
                requestObject.setUri(handleTrailingSlash(requestMethod[1]));
                requestObject.setHttpversion(requestMethod[2]);

                // Append headers to requestObject, skip first
                // we already took care of that.
                if (header.length > 1){
                    for (int i = 1; i < header.length; i++) {
                        String[] headerData = header[i].split(":");
                        requestObject.headerDataMap.put(headerData[0], headerData[1]);
                    }
                }

            }
            catch (Exception e){
                LOG.debug("Failed to parse header request.");
                LOG.debug("\t"+headers);
                LOG.debug("\t"+e);
            }
        }

        // try to parse body
        try{
            if (body != null) {
                requestObject.setBody(body);
                String[] data = body.split("\r\n");  // This is wrong if using text/plain in html form

                for (String s : data) {

                    // If post request body contains this string, then it is a PUT request
                    // This is set in the HTML form
                    if (s.equals("_method=put")) {
                        requestObject.setMethod(Request.HTTPMethod.PUT);
                    }

                    // Split up the request body and place KEY, VALUE in a hashmap
                    String[] datas = s.split("=");

                    // If we only got a key and no value, dont put in dataMap
                    if (datas.length > 1){
                        requestObject.bodyDataMap.put(datas[0], datas[1]);
                    }
                }
            }
            return requestObject;
        }

        catch (Exception e){
            LOG.debug("Failed to parse request body:");
            LOG.debug("\t"+body);
            LOG.debug("\t"+e);
        }




        // We could not handle the request
        return new Request(Request.HTTPMethod.NOTVALID, "Bad", "Bad");
    }

    private static Request.HTTPMethod evalHTTPMethod(String method){
        // GET Request
        Pattern getPattern = Pattern.compile("(\\s|^)GET(\\s|$)", Pattern.CASE_INSENSITIVE);
        Matcher getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return Request.HTTPMethod.GET;
        }

        // POST request
        getPattern = Pattern.compile("(\\s|^)POST(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return Request.HTTPMethod.POST;
        }

        // PUT request
        getPattern = Pattern.compile("(\\s|^)PUT(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return Request.HTTPMethod.PUT;
        }

        // DELETE request
        getPattern = Pattern.compile("(\\s|^)DELETE(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return Request.HTTPMethod.DELETE;
        }

        return Request.HTTPMethod.NOTVALID;
    }

    private static String handleTrailingSlash(String uri){

        // Remove trailing slash, if exists
        if ((uri.charAt(uri.length()-1) == '/') && (uri.length() > 1)){
            StringBuilder sb = new StringBuilder(uri);
            sb.deleteCharAt(uri.length()-1);
            return sb.toString();
        }

        return uri;
    }
}

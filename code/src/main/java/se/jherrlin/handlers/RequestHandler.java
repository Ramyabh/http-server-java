package se.jherrlin.handlers;

import se.jherrlin.model.HTTPMethod;
import se.jherrlin.model.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nils on 2/15/16.
 */
public class RequestHandler {

    // This classes will be ugly, a lot of parsing

    public static Request RequestParser(String requestStringIn){

        //System.out.println(requestStringIn);
        String[] requestString = null;
        String headers = null;
        String body = null;
        Request requestObject = new Request();

        // Try to split header and body
        try{
            requestString = requestStringIn.split("\\r\\n");
            headers = requestString[0];
            body = requestString[1];
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        // Try to split up header
        if (headers != null){
            try{
                String[] header = headers.split("\\n");
                String[] requestMethod = header[0].split("\\s+");
                requestObject.setMethod(evalHTTPMethod(requestMethod[0]));
                requestObject.setUri(handleTrailingSlash(requestMethod[1]));
                requestObject.setHttpversion(requestMethod[2]);
                return requestObject;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return new Request(HTTPMethod.NOTVALID, "Bad", "Bad");
    }

    private static HTTPMethod evalHTTPMethod(String method){
        // GET Request
        Pattern getPattern = Pattern.compile("(\\s|^)GET(\\s|$)", Pattern.CASE_INSENSITIVE);
        Matcher getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return HTTPMethod.GET;
        }

        // POST request
        getPattern = Pattern.compile("(\\s|^)POST(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return HTTPMethod.POST;
        }

        // PUT request
        getPattern = Pattern.compile("(\\s|^)PUT(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return HTTPMethod.PUT;
        }

        // DELETE request
        getPattern = Pattern.compile("(\\s|^)DELETE(\\s|$)", Pattern.CASE_INSENSITIVE);
        getMatcher = getPattern.matcher(method);
        if (getMatcher.find()){
            return HTTPMethod.DELETE;
        }

        return HTTPMethod.NOTVALID;
    }

    private static String handleTrailingSlash(String uri){

        // Remove trailing slash
        if ((uri.charAt(uri.length()-1) == '/') && (uri.length() > 1)){
            StringBuilder sb = new StringBuilder(uri);
            sb.deleteCharAt(uri.length()-1);
            return sb.toString();
        }

        System.out.println(uri);
        return uri;
    }
}

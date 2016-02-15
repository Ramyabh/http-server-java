package se.jherrlin.handlers;

import se.jherrlin.model.HTTPMethod;
import se.jherrlin.model.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nils on 2/15/16.
 */
public class RequestHandler {

    public static Request RequestParser(String request){

        String[] lines = request.split("\\n");
        if (lines.length < 1){ new String("Not a valid HTTP protocol"); };

        Pattern getPattern = Pattern.compile("(\\s|^)GET(\\s|$)", Pattern.CASE_INSENSITIVE);
        Matcher getMatcher = getPattern.matcher(lines[0]);

        if (getMatcher.find()){
            String[] firstLine = lines[0].split("\\s+");
            return new Request(HTTPMethod.GET, firstLine[1], firstLine[2]);
        }
        return new Request(HTTPMethod.NOTVALID, "Bad", "Bad");
    }
}

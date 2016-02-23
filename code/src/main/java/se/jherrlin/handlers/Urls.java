package se.jherrlin.handlers;

import se.jherrlin.model.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nils on 2/22/16.
 */
public class Urls {

    public static void urls(Request request){

        if (Pattern.matches("^/$", request.getUri())){
            Views.index(request);
        }

        else if (Pattern.matches("^/static(.+?|$)$", request.getUri())){
            Views.staticfiles(request);
        }

        else if (Pattern.matches("^/form(.+?|$)$", request.getUri())){
            Views.staticfiles(request);
        }

        else if (Pattern.matches("^/mysite(.+?|$)$", request.getUri())){
            Views.staticfiles(request);
        }

        else if (Pattern.matches("^/post(.+?|$)$", request.getUri())) {
            Views.post(request);
        }

        else if (Pattern.matches("^/blog(.+?|$)$", request.getUri())) {
            Views.getAllBlogPosts(request);
        }

        // If we cant match url
        else {Views.notfount(request);}
    }

}

package se.jherrlin.handlers;

import se.jherrlin.model.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nils on 2/22/16.
 */
public class Urls {

    public static void urls(Request request){

        // Index /
        Pattern indexPattern = Pattern.compile("/$", Pattern.CASE_INSENSITIVE);
        Matcher indexMatcher = indexPattern.matcher(request.getUri());

        // Static /static*
        Pattern staticPattern = Pattern.compile("^/static(.+?|$)$", Pattern.CASE_INSENSITIVE);
        Matcher staticMatcher = staticPattern.matcher(request.getUri());

        // Form /form.html
        Pattern formPattern = Pattern.compile("^/form(.+?|$)$", Pattern.CASE_INSENSITIVE);
        Matcher formMatcher = formPattern.matcher(request.getUri());

        // Page /mysite.html
        Pattern mysitePattern = Pattern.compile("^/mysite(.+?|$)$", Pattern.CASE_INSENSITIVE);
        Matcher mysiteMatcher = mysitePattern.matcher(request.getUri());

        // Post /post
        Pattern postPattern = Pattern.compile("^/post(.+?|$)$");
        Matcher postMatcher = postPattern.matcher(request.getUri());

        if (indexMatcher.find()){
            Views.index(request);
        }

        else if (staticMatcher.find()){
            Views.staticfiles(request);
        }

        else if (formMatcher.find()){
            Views.staticfiles(request);
        }

        else if (mysiteMatcher.find()){
            Views.staticfiles(request);
        }

        else if (postMatcher.find()) {
            Views.post(request);
        }

        // If we cant match url
        else {Views.notfount(request);}
    }

}

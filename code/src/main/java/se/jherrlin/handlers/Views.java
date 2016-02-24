package se.jherrlin.handlers;

import org.apache.log4j.Logger;
import se.jherrlin.db.Db;
import se.jherrlin.domain.Blog;
import se.jherrlin.model.Header;
import se.jherrlin.model.Request;
import se.jherrlin.model.Response;
import se.jherrlin.tcp.TCPServer;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nils on 2/22/16.
 */
public class Views {

    public static void index(Request request){
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        try {
            Response response = new Response();
            StaticHandler.findStaticFile(request.getUri(), response);
            LOG.debug(request);
            LOG.debug(response);
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        }
        catch (Exception e){
            LOG.debug(e);
        }
    }

    public static void staticfiles(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        try {
            Response response = new Response();
            StaticHandler.findStaticFile(request.getUri(), response);
            LOG.debug(request);
            LOG.debug(response);
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        }
        catch (Exception e){
            LOG.debug(e);
        }
    }

    public static void login(Request request) {
        Response response = new Response();
        response.setResponse(Header.response_200_ok);
        response.appendHeader(Header.header_content_type_texthtml);
        TCPServer.sessions.add(request.headerDataMap.get("Cookie"));

        redirect(response, request, "/");
    }

    public static void post(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        Response response = new Response();
        response.setResponse(Header.response_201_created);
        response.appendHeader(Header.header_content_type_texthtml);

        response.setBody(request.getBody().getBytes());
        System.out.println("REQUEST BODY: " + request.getBody());

        Db.initDb();

        Blog blog = new Blog();
        blog.setUuid(UUID.randomUUID().toString());
        blog.setHeader(request.bodyDataMap.get("title"));
        blog.setText(request.bodyDataMap.get("content"));
        blog.setImgName(request.bodyDataMap.get("fileChooser"));
        blog.setImgB64(request.bodyDataMap.get("base64"));

        LOG.debug(blog.getHeader() + " created");
        blog.create();
        LOG.debug(request);
        LOG.debug(response);

        try {
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        } catch (Exception e) {
            LOG.debug(e);
        }
    }

    public static void postPicture(Request request) {
        System.out.println("-!- Not implemented -!-");
    }

    public static void put(Request request) {

        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        //System.out.println(request.getBody());
        Response response = new Response();
        System.out.println(request.bodyDataMap.get("bloguuid"));
        System.out.println(request.bodyDataMap.get("blogheader"));
        System.out.println(request.bodyDataMap.get("blogtext"));
        try {
            Blog blog = Blog.getById(request.bodyDataMap.get("bloguuid"));
            blog.setHeader(request.bodyDataMap.get("blogheader"));
            blog.setText(request.bodyDataMap.get("blogtext"));

            blog.update();
            LOG.debug(blog + " updated.");
        } catch (Exception e) {
            LOG.debug(e);
        }

        redirect(response, request, "/blog");
    }

    public static void updateAllBlogPosts(Request request) {

        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        try {
            StringBuilder html = new StringBuilder();
            html.append(StaticHandler.getHTMLheader);
            html.append("<h1>Update blog posts</h1>");
            html.append("<h2><a href=\"/blog\">Back</a></h2>");
            html.append("<h2><a href=\"/\">Home</a></h2>");
            for (Blog b : Blog.getAll()){
                html.append("<hr>");
                html.append("<div class=\"row\" align=\"center\">");
                html.append("<form action=\"/put\" method=\"post\" accept-charset=\"UTF-8\" enctype=\"text/plain\" autocomplete=\"off\">");
                html.append("<input type=\"hidden\" name=\"_method\" value=\"put\" />");
                html.append("UUID: ! Dont change this !<br>");
                html.append("<input type=\"text\" name=\"bloguuid\" value=\""+ b.getUuid()+"\" style=\"width: 100%\"><br>");
                html.append("Header:<br>");
                html.append("<input type=\"text\" name=\"blogheader\" value=\""+ b.getHeader()+"\" style=\"width: 100%\"><br>");
                html.append("Text:<br>");
                html.append("<input type=\"text\" name=\"blogtext\" value=\""+ b.getText()+"\" style=\"width: 100%\"><br>");
                html.append("<input type=\"submit\" value=\"Update\">");
                html.append("</form>");
                html.append("</div>");
            }

            html.append(StaticHandler.getHTMLfooter);
            Response response = new Response();
            response.setResponse(Header.response_201_created);
            response.appendHeader(Header.header_content_type_texthtml);
            response.setBody(html.toString().getBytes());
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        } catch (Exception e) {
            LOG.debug(e);
        }
    }

    public static void notfound(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        try {
            Response response = new Response();
            response.setResponse(Header.response_404_notfound);
            response.setBody("404 Not Found".getBytes());
            LOG.debug(request);
            LOG.debug(response);
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        } catch (Exception e) {
            LOG.debug(e);
        }
    }

    public static void permissionDenied(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        try {
            Response response = new Response();
            response.setResponse(Header.response_401_unauthorized);
            response.setBody("401 Unauthorized".getBytes());

            LOG.debug(request);
            LOG.debug(response);
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        } catch (IOException e) {
            LOG.debug(e);
        }
    }

    public static void getAllBlogPosts(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        try {
            StringBuilder html = new StringBuilder();
            html.append(StaticHandler.getHTMLheader);
            html.append("<h1>All blog posts</h1>");
            html.append("<h2><a href=\"/form.html\">Create</a></h2>");
            html.append("<h2><a href=\"/blog/edit\">Edit</a></h2>");
            html.append("<h2><a href=\"/\">Home</a></h2>");
            for (Blog b : Blog.getAll()){
                html.append("<hr>");
                html.append("<div class=\"row\" align=\"center\">");
                html.append("<h3>Header:</h3><br>");
                html.append("<div>"+ b.getHeader()+"</div><br>");
                html.append("<h3>Text:</h3><br>");
                html.append("<div>"+ b.getText()+"</div><br>");
                html.append("<img src=\"" + b.getImgB64() + "\">");
                html.append("</div>");

            }

            html.append(StaticHandler.getHTMLfooter);
            Response response = new Response();
            response.setResponse(Header.response_200_ok);
            response.appendHeader(Header.header_content_type_texthtml);
            response.setBody(html.toString().getBytes());
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        } catch (Exception e) {
            LOG.debug(e);
        }
    }

    private static void redirect(Response response, Request request, String endpoint) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        String redirectUrlString = "<!DOCTYPE HTML> <html lang=\"en-US\"> <head> <meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"refresh\" content=\"1;url=\"" + endpoint + "\">" +
                "<script type=\"text/javascript\">" +
                "window.location.href =\"" + endpoint + "\">" +
                "</script>" +
                "<title>Page Redirection</title>" +
                "</head> <body>" +
                "If you are not redirected automatically, follow the <a href='/blog'>Back to update blogs</a>" +
                "</body> </html>";

        response.setResponse(Header.response_200_ok);
        response.appendHeader(Header.header_content_type_texthtml);

        try {
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(redirectUrlString.getBytes());
            request.getDataOutputStream().close();
        } catch (IOException e) {
            LOG.debug(e);
        }
    }
}

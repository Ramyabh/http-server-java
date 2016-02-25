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
        String title = request.bodyDataMap.get("title");
        String text = request.bodyDataMap.get("content");
        String imgNage = request.bodyDataMap.get("fileChooser");
        String base64Img = request.bodyDataMap.get("base64");

        if (title != null){
            blog.setHeader(title);
        }
        if (text != null){
            blog.setText(text);
        }
        if (imgNage != null){
            blog.setImgName(imgNage);
        }
        if (base64Img != null){
            blog.setImgB64(base64Img);
        }

        blog.create();
        LOG.debug(blog.getHeader() + " created");
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
        String uuid = request.bodyDataMap.get("uuid");
        String title = request.bodyDataMap.get("title");
        String content = request.bodyDataMap.get("content");
        String fileChooses = request.bodyDataMap.get("fileChooser");
        String base64Img = request.bodyDataMap.get("base64");
        try {
            Blog blog = Blog.getById(uuid);
            if (title != null){
                blog.setHeader(title);
            }
            if (content != null){
                blog.setText(content);
            }
            if (fileChooses != null){
                blog.setImgName(fileChooses);
            }
            if (fileChooses != null){
                blog.setImgB64(base64Img);
            }
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
                html.append("<form action=\"/put\" method=\"post\" accept-charset=\"UTF-8\" enctype=\"text/plain\" autocomplete=\"off\">" +
                        "<input type=\"hidden\" name=\"_method\" value=\"put\" />"+
                        "<input type=\"hidden\" name=\"uuid\" value=\"" + b.getUuid() + "\"><br>"+
                        "    Title:<br>\n" +
                        "    <input type=\"text\" name=\"title\" value=\"" + b.getHeader() + "\"><br>\n" +
                        "    Content:<br>\n" +
                        "    <input type=\"text\" name=\"content\" value=\""+ b.getText() +"\"><br><br>\n" +
                        "    <input type=\"file\" name=\"fileChooser\" id=\"fileChooser\" accept=\"image/*\" onchange=\"imgToBase64()\"><br>\n" +
                        "    <input type=\"hidden\" name=\"base64\" id=\"base64\">\n" +
                        "    <input type=\"submit\" value=\"Submit\">\n" +
                        "</form>");
                if (b.getImgB64() != null){
                    html.append("<img src=\"" + b.getImgB64() + "\" style=\"width: 100px;height: 100px\">");
                }
                html.append("</div>");
                html.append("<script type=\"text/javascript\">\n" +
                        "    function imgToBase64() {\n" +
                        "        var fileSelected = document.getElementById(\"fileChooser\").files;\n" +
                        "        var base64 = document.getElementById(\"base64\");\n" +
                        "        if (fileSelected.length > 0) {\n" +
                        "            var fileToLoad = fileSelected[0];\n" +
                        "            var fileReader = new FileReader();\n" +
                        "            fileReader.onload = function(fileLoadedEvent)\n" +
                        "            {\n" +
                        "                base64.value = fileLoadedEvent.target.result;\n" +
                        "                console.log(\"Base64: \" + base64.value);\n" +
                        "            };\n" +
                        "            fileReader.readAsDataURL(fileToLoad);\n" +
                        "        }\n" +
                        "    }\n" +
                        "</script>");
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
                if (b.getImgB64() != null){
                    html.append("<img src=\"" + b.getImgB64() + "\" style=\"width: 100px;height: 100px\">");
                }
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

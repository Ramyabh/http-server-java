package se.jherrlin.handlers;

import org.apache.log4j.Logger;
import se.jherrlin.db.Db;
import se.jherrlin.domain.Blog;
import se.jherrlin.model.Header;
import se.jherrlin.model.Request;
import se.jherrlin.model.Response;
import se.jherrlin.tcp.TCPServer;

import java.io.IOException;
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

        Response response = new Response();

        try {
            StaticHandler.findStaticFile(request.getUri(), response);
        } catch (Exception e){
            LOG.debug("Could not found static file.");
            LOG.debug(e);
        }

        try {
            LOG.debug(request);
            LOG.debug(response);
            request.getDataOutputStream().write(response.getHeaders());
            if (response.getBody() != null){
                request.getDataOutputStream().write(response.getBody());
            }
            else {
                request.getDataOutputStream().write("404 Not Found".getBytes());
            }
            request.getDataOutputStream().close();
        }
        catch (Exception e){
            LOG.debug(e);
        }
    }

    public static void login(Request request) {
        Response response = new Response();
        response.setResponse(Header.response_202_accepted);
        //response.appendHeader(Header.header_content_type_texthtml);
        TCPServer.sessions.add(request.headerDataMap.get("Cookie"));
        response.setResponse(Header.response_202_accepted);
        redirect(response, request, "/");
    }

    public static void logout(Request request) {
        Response response = new Response();
        TCPServer.sessions.clear(); // Remove cookie == logout
        response.setResponse(Header.response_205_resetcontent);
        response.setBody("205 Reset Content".getBytes());
        try{
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
            request.getDataOutputStream().close();
        }
        catch (Exception e){}
    }

    public static void post(Request request) {

        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        Response response = new Response();

        if (request.getMethod() == Request.HTTPMethod.POST) {

            response.setBody(request.getBody().getBytes());
            System.out.println("REQUEST BODY: " + request.getBody());

            Db.initDb();

            Blog blog = new Blog();
            blog.setUuid(UUID.randomUUID().toString());
            String title = request.bodyDataMap.get("title");
            String text = request.bodyDataMap.get("content");
            String imgNage = request.bodyDataMap.get("fileChooser");
            String base64Img = request.bodyDataMap.get("base64");

            if (title != null) {
                blog.setHeader(title);
            }
            if (text != null) {
                blog.setText(text);
            }
            if (imgNage != null) {
                blog.setImgName(imgNage);
            }
            if (base64Img != null) {
                blog.setImgB64(base64Img);
            }

            blog.create();
            LOG.debug(blog.getHeader() + " created");
            LOG.debug(request);
            LOG.debug(response);

            try {
                response.setResponse(Header.response_201_created);
                redirect(response, request, "/blog");
            } catch (Exception e) {
                LOG.debug(e);
            }
        }
        else {
            try {

                response.setResponse(Header.response_405_methodnotallowed);
                response.setBody("405 Method Not Allowed".getBytes());
                request.getDataOutputStream().write(response.getHeaders());
                request.getDataOutputStream().write(response.getBody());
                request.getDataOutputStream().close();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void put(Request request) {

        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());

        Response response = new Response();

        if (request.getMethod() == Request.HTTPMethod.PUT){

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

            response.setResponse(Header.response_200_ok);
            redirect(response, request, "/blog");
        }
        else {
            try {
                response.setResponse(Header.response_400_badrequest);
                response.setBody("400 Bad Request, needs a PUT request".getBytes());
                request.getDataOutputStream().write(response.getHeaders());
                request.getDataOutputStream().write(response.getBody());
                request.getDataOutputStream().close();

            }
            catch (Exception e){}
        }
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
                html.append("<form action=\"/put/"+b.getUuid()+"\" method=\"post\" accept-charset=\"UTF-8\" enctype=\"text/plain\" autocomplete=\"off\">" +
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
            response.setResponse(Header.response_403_forbidden);
            response.setBody("403 Forbidden".getBytes());

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
        String redirectUrlString = "" +
                "<!DOCTYPE HTML>\n" +
                "<html lang=\"en-US\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta http-equiv=\"refresh\" content=\"1;url="+endpoint+"\">\n" +
                "        <script type=\"text/javascript\">\n" +
                "            window.location.href = \"" + endpoint + "\"" +
                "        </script>\n" +
                "        <title>Page Redirection</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        If you are not redirected automatically, follow the <a href='"+endpoint+"'>link</a>\n" +
                "    </body>\n" +
                "</html>";


        //response.setResponse(Header.response_200_ok);
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

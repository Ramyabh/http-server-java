package se.jherrlin.handlers;

import se.jherrlin.model.Header;
import se.jherrlin.model.Response;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class StaticHandler {

    public static String getHTMLheader = "<!DOCTYPE html> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title>Title</title> <link rel=\"stylesheet\" type=\"text/css\" href=\"/static/index.css\"> </head> <body>";
    public static String getHTMLfooter = "</body> </html>";

    public static void findStaticFile(String filenameIn, Response responseIn){

        Response response = responseIn;

        try{
            File file = new File(StaticHandler.class.getResource(filenameIn).getPath());

            if (file.isFile()){
                Path path = file.toPath();
                response.setBody(Files.readAllBytes(path));

                // Get file format
                String[] format = file.getName().split("\\.");
                if (format[1].equals("css")){
                    response.setResponse(Header.response_200_ok);
                    response.appendHeader(Header.header_content_type_textcss);
                }
                else if (format[1].equals("jpg") || format[1].equals("png") || format[1].equals("jpeg")){
                    response.setResponse(Header.response_200_ok);
                    response.appendHeader(Header.header_content_type_image);
                }
                else {
                    response.setResponse(Header.response_200_ok);
                    response.appendHeader(Header.header_content_type_texthtml);
                }
                return;
            }

            if (file.isDirectory()){
                try{
                    File html = new File(file.getPath()+"/index.html");
                    Path htmlPath = html.toPath();
                    response.setResponse(Header.response_200_ok);
                    response.appendHeader(Header.header_content_type_texthtml);
                    response.setBody(Files.readAllBytes(htmlPath));
                    return;
                }
                catch (Exception e){
                    e.printStackTrace();
                    response.setResponse(Header.response_404_notfound);
                }

                try{
                    File htm = new File(file.getPath()+"/index.htm");
                    Path htmPath = htm.toPath();
                    response.setResponse(Header.response_200_ok);
                    response.appendHeader(Header.header_content_type_texthtml);
                    response.setBody(Files.readAllBytes(htmPath));
                    return;
                }
                catch (Exception e){
                    e.printStackTrace();
                    response.setResponse(Header.response_404_notfound);
                }
            }

            response.setResponse(Header.response_404_notfound);
            return;
        }
        catch (Exception e){
            e.printStackTrace();
            response.setResponse(Header.response_404_notfound);
            return;
        }
    }
}

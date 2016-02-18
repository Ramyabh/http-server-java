package se.jherrlin.model;


public class Header {

    public static String response_200_ok = "HTTP/1.1 200 OK";
    public static String response_201_created = "HTTP/1.1 201 Created";
    public static String response_400_badrequest = "HTTP/1.1 400 Bad Request";
    public static String response_401_unauthorized = "HTTP/1.1 401 Unauthorized";
    public static String response_402_paymentrequired = "HTTP/1.1 402 Payment Required";
    public static String response_403_forbidden = "HTTP/1.1 403 Forbidden";
    public static String response_404_notfound = "HTTP/1.1 404 Not Found";
    public static String response_405_methodnotallowed = "HTTP/1.1 405 Method Not Allowed";
    public static String response_500_servererror = "HTTP/1.1 500 Server Error";

    public static String header_content_type_texthtml = "Content-Type: text/html";
    public static String header_content_type_textcss = "Content-Type: text/css";
    public static String header_content_type_image = "Content-Type: image";
    public static String header_content_type_applicationjson = "Content-Type: application/json";
}

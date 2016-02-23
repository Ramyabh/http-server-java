package se.jherrlin.handlers;

import org.apache.log4j.Logger;
import se.jherrlin.db.Db;
import se.jherrlin.domain.Blog;
import se.jherrlin.model.Header;
import se.jherrlin.model.Request;
import se.jherrlin.model.Response;

import java.rmi.server.ExportException;
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

    public static void post(Request request) {
        final Logger LOG = Logger.getLogger(RequestHandler.class.getSimpleName());
        Response response = new Response();
        response.setResponse(Header.response_201_created);
        response.appendHeader(Header.header_content_type_texthtml);
        response.setBody(request.getBody().getBytes());

        Db.initDb();
        Blog blog = new Blog(request.getBody(), request.getBody());
        blog.setUuid(UUID.randomUUID().toString());
        LOG.debug(blog.getHeader() + " created");
        blog.create();
        LOG.debug(request);
        LOG.debug(response);


        try {
            request.getDataOutputStream().write(response.getHeaders());
            request.getDataOutputStream().write(response.getBody());
        } catch (Exception e) {
            LOG.debug(e);
        }
    }

    public static void notfount(Request request) {
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
}

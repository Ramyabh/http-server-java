package se.jherrlin.tcp;


import java.io.*;
import java.net.*;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import se.jherrlin.db.Db;
import se.jherrlin.domain.Blog;
import se.jherrlin.model.*;
import se.jherrlin.handlers.*;

public class TCPServer extends Host{

    byte[] buf;

    public TCPServer(CommandLine cmd) {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    public void run() throws Exception{

        ServerSocket serverSocket = new ServerSocket(this.port);

        while(true) {
            // Accept client when it tries to connect
            Socket socket = serverSocket.accept();
            // Pass the socket to the new ServerThread instance
            // ServerThread will take care or the rest
            ServerThread serverThread = new ServerThread(socket, this.bufsize);
            serverThread.start();
            // Go back to waiting for a client to connect
        }
    }
}

class ServerThread extends Thread {

    final Logger LOG = Logger.getLogger(this.getClass().getSimpleName());

    Socket socket;
    int bufsize;

    byte[] buf;
    InputStream inputStream;
    DataOutputStream outputStream;

    public ServerThread(Socket socket, int bufsize) {
        this.socket = socket;
        this.bufsize = bufsize;
    }

    @Override
    public void run(){
        try{
            inputStream = new DataInputStream(this.socket.getInputStream());
            outputStream = new DataOutputStream(this.socket.getOutputStream());

            // Wait for data to arrive in the inputStream
            Thread.sleep(100);

            // message from client
            String message = "";

            // Handle the tcp stream
            do {
                buf = new byte[bufsize];
                inputStream.read(buf);
                message += new String(buf);
            }
            while (inputStream.available() != 0);

            // Start parsing the request
            Request request = RequestHandler.RequestParser(message);
            request.setClientAddress(this.socket.getLocalAddress().toString());
            request.setClientPort(this.socket.getPort());
            request.setDataOutputStream(outputStream);

            // Create empty response
//            Response response = new Response();
            Urls.urls(request);
//
//            // Start handeling the request
//            if (request.getMethod() == Request.HTTPMethod.GET){
//                StaticHandler.findStaticFile(request.getUri(), response);
//                LOG.debug(request);
//                LOG.debug(response);
//                outputStream.write(response.getHeaders());
//                outputStream.write(response.getBody());
//            }
//
//            // Post
//            else if (request.getMethod() == Request.HTTPMethod.POST){
//                //StaticHandler.findStaticFile(request.getUri(), response);
//                response.setResponse(Header.response_201_created);
//                response.appendHeader(Header.header_content_type_texthtml);
//                response.setBody(request.getBody().getBytes());
//
//                Db.initDb();
//                Blog blog = new Blog(request.getBody(), request.getBody());
//                blog.setUuid(UUID.randomUUID().toString());
//                LOG.debug(blog.getHeader() + " created");
//                blog.create();
//                LOG.debug(request);
//                LOG.debug(response);
//                outputStream.write(response.getHeaders());
//                outputStream.write(response.getBody());
//            }
//
//            // Bad request
//            else if (request.getMethod() == Request.HTTPMethod.NOTVALID){
//                response.setResponse(Header.response_400_badrequest);
//                response.setBody(Header.response_400_badrequest.getBytes());
//                LOG.debug(request);
//                LOG.debug(response);
//                outputStream.write(response.getHeaders());
//                outputStream.write(response.getBody());
//            }
//            else {
//                // CANTHANDLE
//                // Log if we cant handle it.
//                LOG.debug("CANTHANDLE: "+message);
//                //outputStream.write(message.getBytes());
//            }
            //System.out.println(request);
//            outputStream.close();

//            Thread.sleep(100);


        }
        catch (Exception e){
            e.printStackTrace();
            //System.exit(1);
        }
    }
}




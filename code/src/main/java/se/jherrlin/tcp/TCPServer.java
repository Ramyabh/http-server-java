package se.jherrlin.tcp;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import se.jherrlin.db.Db;
import se.jherrlin.domain.Blog;
import se.jherrlin.model.*;
import se.jherrlin.handlers.*;

public class TCPServer extends Host{

    byte[] buf;
    public static ArrayList<String> sessions;

    public TCPServer(CommandLine cmd) {
        super(cmd);
        buf = new byte[this.bufsize];
        sessions = new ArrayList<String>();
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
                message += new String(buf,"UTF-8");
            }
            while (inputStream.available() != 0);

            // Start parsing the request
            Request request = RequestHandler.RequestParser(message);
            request.setClientAddress(this.socket.getLocalAddress().toString());
            request.setClientPort(this.socket.getPort());
            request.setDataOutputStream(outputStream);

            // If we cant handle the request
            // Send server error
            if (request.getMethod() == Request.HTTPMethod.NOTVALID){
                Response serverErrorResponse = new Response();
                serverErrorResponse.setResponse(Header.response_500_servererror);
                serverErrorResponse.setBody("Server Error".getBytes());
                request.getDataOutputStream().write(serverErrorResponse.getHeaders());
                request.getDataOutputStream().write(serverErrorResponse.getBody());
                request.getDataOutputStream().close();
            }
            else {
                Urls.urls(request);
            }
        }
        catch (Exception e){
            LOG.debug("Failed in main server thread run.");
            LOG.debug("\t" + e);
            //System.exit(1);
        }
    }
}




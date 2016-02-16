package se.jherrlin.tcp;

/**
 * Created by John Herrlin on 2/12/16.
 */

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;

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

            // Create empty response
            Response response = new Response();

            // Start handeling the request
            if (request.getMethod() == HTTPMethod.GET){
                //response.appendHeader(Header.response_200_ok);
                //response.appendHeader(Header.header_content_type_texthtml);
                StaticHandler.findStaticFile(request.getUri(), response);
                outputStream.write(response.getHeader());
                outputStream.write(response.getBody());
            }
            else {
                outputStream.write(message.getBytes());
            }
            System.out.println(request);
            outputStream.close();

            Thread.sleep(100);


        }
        catch (Exception e){
            e.printStackTrace();
            //System.exit(1);
        }
    }
}




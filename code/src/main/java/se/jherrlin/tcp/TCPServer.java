package se.jherrlin.tcp;

/**
 * Created by John Herrlin on 2/12/16.
 */

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;

import se.jherrlin.model.HTTPMethod;
import se.jherrlin.model.Host;
import se.jherrlin.handlers.*;
import se.jherrlin.model.Request;
import se.jherrlin.model.Response;

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

            do {
                buf = new byte[bufsize];
                inputStream.read(buf);
                message += new String(buf).trim();
            }
            while (inputStream.available() != 0);

            Request request = RequestHandler.RequestParser(message);

            if (request.getMethod() == HTTPMethod.GET){
                if (request.getUri().equals("/")){
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    File file = new File(classloader.getResource("index.html").getPath());
                    Path path = file.toPath();
                    //FileOutputStream fileOutputStream = new FileOutputStream(file);
                    Response response = new Response();
                    response.appendHeader("HTTP/1.1 200 OK");
                    response.appendHeader("Content-Type: text/html");
                    response.setBody(Files.readAllBytes(path));
                    outputStream.write(response.getHeader());
                    outputStream.write(response.getBody());
                }
                if (request.getUri().equals("/index.css")){
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    File file = new File(classloader.getResource("static/index.css").getPath());
                    Path path = file.toPath();
                    //FileOutputStream fileOutputStream = new FileOutputStream(file);
                    Response response = new Response();
                    response.appendHeader("HTTP/1.1 200 OK");
                    //response.appendHeader("Content-Type: text/html");
                    response.setBody(Files.readAllBytes(path));
                    outputStream.write(response.getHeader());
                    outputStream.write(response.getBody());
                }
                if (request.getUri().equals("/gnu.png")){
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    File file = new File(classloader.getResource("img/gnu.png").getPath());
                    Path path = file.toPath();
                    //FileOutputStream fileOutputStream = new FileOutputStream(file);
                    Response response = new Response();
                    response.appendHeader("HTTP/1.1 200 OK");
                    //response.appendHeader("Content-Type: text/html");
                    response.setBody(Files.readAllBytes(path));
                    outputStream.write(response.getHeader());
                    outputStream.write(response.getBody());
                }
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
            System.exit(1);
        }
    }
}




package se.jherrlin.tcp;

/**
 * Created by John Herrlin on 2/12/16.
 */

import java.io.*;
import java.net.*;

import org.apache.commons.cli.CommandLine;

import se.jherrlin.model.Host;

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
    OutputStream outputStream;

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
            Thread.sleep(200);

            // message from client
            String message = "";

            do {
                buf = new byte[bufsize];
                inputStream.read(buf);
                message += new String(buf).trim();
            }
            while (inputStream.available() != 0);

            System.out.println(socket.getInetAddress()+ " says " + message + " with a length of: " + message.length());
            Thread.sleep(200);
            outputStream.write(message.getBytes());

        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}




package se.jherrlin.tcp;

import java.io.*;
import java.net.*;

import org.apache.commons.cli.CommandLine;

import se.jherrlin.model.Host;


public class TCPClient extends Host{

    byte[] buf;

    public TCPClient(CommandLine cmd) {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    public void run() throws Exception{

        // If mtr is 0, send only once.
        if (this.mtr == 0){ send(); }

        else{
            // how many seconds to send
            for (int j = 0; j < this.seconds; j++) {

                // how many messages per second
                for (int i = 0; i < this.mtr; i++) {
                    send();
                    sleep();
                }
            }
        }
    }

    public void send() throws Exception{
        Socket socket = new Socket(this.ip, this.port);

        DataOutputStream outToServer = new DataOutputStream( socket.getOutputStream() );
        DataInputStream inputStream = new DataInputStream( socket.getInputStream() );
        byte[] buf;
        outToServer.writeBytes(this.message);

        // Wait for data to arrive to inputStream
        Thread.sleep(200);

        String message = "";

        do {
            buf = new byte[this.bufsize];
            inputStream.read(buf);
            message += new String(buf).trim();
        }
        while (inputStream.available() != 0);

        System.out.println("Server says: "+ message + " with a lenght of: " + message.length());
        socket.close();
    }
}

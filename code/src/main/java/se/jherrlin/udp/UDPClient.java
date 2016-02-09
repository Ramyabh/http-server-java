package se.jherrlin.udp;

import java.io.IOException;
import java.net.*;

import org.apache.commons.cli.CommandLine;

import se.jherrlin.model.Host;


public class UDPClient extends Host {

    byte[] buf;
    private static String MSG = "An echo message!";

    public UDPClient(CommandLine cmd) {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    public void run(){
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

    public void send(){

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(null);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        SocketAddress localBindPoint = new InetSocketAddress(this.port);
        try {
            socket.bind(localBindPoint);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        SocketAddress remoteBindPoint = new InetSocketAddress(
                this.ip,
                this.port
        );

        DatagramPacket sendPacket = null;
        try {
            sendPacket = new DatagramPacket(
                    MSG.getBytes(),
                    MSG.length(),
                    remoteBindPoint
            );
        } catch (SocketException e) {
            e.printStackTrace();
        }

        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

           /* Send and receive message*/
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

            /* Compare sent and received message */
        String receivedString =
                new String(receivePacket.getData(),
                        receivePacket.getOffset(),
                        receivePacket.getLength());
        if (receivedString.compareTo(MSG) == 0)
            System.out.printf("%d bytes sent and received\n", receivePacket.getLength());
        else
            System.out.printf("Sent and received msg not equal!\n");
        socket.close();
    }

}

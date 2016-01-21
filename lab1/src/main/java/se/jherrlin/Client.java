package se.jherrlin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.cli.CommandLine;

public class Client extends Host {

    byte[] buf;
    private static String MSG = "An echo message!";

    public Client(CommandLine cmd) throws IOException {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    public void run() throws IOException{

        for (int j = 0; j < this.seconds; j++) {

            for (int i = 0; i < this.mtr; i++) {

                send();

                try{
                    Thread.sleep(1000/this.mtr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void send() throws IOException{

        DatagramSocket socket = new DatagramSocket(null);

        SocketAddress localBindPoint = new InetSocketAddress(this.port);
        socket.bind(localBindPoint);

        SocketAddress remoteBindPoint = new InetSocketAddress(
                this.ip,
                this.port
        );

        DatagramPacket sendPacket = new DatagramPacket(
                MSG.getBytes(),
                MSG.length(),
                remoteBindPoint
        );

        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

        /* Send and receive message*/
        socket.send(sendPacket);
        socket.receive(receivePacket);

        /* Compare sent and received message */
        String receivedString=
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

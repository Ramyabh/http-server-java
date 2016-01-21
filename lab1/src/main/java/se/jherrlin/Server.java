package se.jherrlin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.cli.*;

public class Server extends Host {

    byte[] buf;

    public Server(CommandLine cmd) throws IOException {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    @Override
    public void run() throws IOException{

        DatagramSocket socket = new DatagramSocket(null);

        SocketAddress localBindPoint = new InetSocketAddress(this.port);

        socket.bind(localBindPoint);

        while (true){
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

            socket.receive(receivePacket);

            DatagramPacket sendPacket = new DatagramPacket(
                    receivePacket.getData(),
                    receivePacket.getLength(),
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );

            socket.send(sendPacket);

            System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
            System.out.printf(" using port %d\n", receivePacket.getPort());
        }
    }
}

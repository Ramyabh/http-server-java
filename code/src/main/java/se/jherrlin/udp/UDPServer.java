package se.jherrlin.udp;

import java.io.IOException;
import java.net.*;

import org.apache.commons.cli.CommandLine;

import se.jherrlin.model.Host;


public class UDPServer extends Host {

    byte[] buf;

    public UDPServer(CommandLine cmd) throws IOException {
        super(cmd);
        buf = new byte[this.bufsize];
    }

    @Override
    public void run(){

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

        while (true){
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

            try {
                socket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DatagramPacket sendPacket = new DatagramPacket(
                    receivePacket.getData(),
                    receivePacket.getLength(),
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );

            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("UDP echo request from %s", receivePacket.getAddress().getHostAddress());
            System.out.printf(" using port %d\n", receivePacket.getPort());
        }
    }
}

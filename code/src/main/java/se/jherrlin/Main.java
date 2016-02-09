package se.jherrlin;

import org.apache.commons.cli.*;
import se.jherrlin.tcp.TCPClient;
import se.jherrlin.tcp.TCPServer;
import se.jherrlin.udp.UDPClient;
import se.jherrlin.udp.UDPServer;

import java.io.IOException;

public class Main
{
    public static UDPServer udpserver;
    public static UDPClient udpclient;
    public static TCPServer tcpserver;
    public static TCPClient tcpclient;

    public static void main( String[] args ) throws ParseException,IOException {

        Options options = new Options();

        // commandline arguments
        options.addOption("h", "help", false, "show help.");
        options.addOption("m", "mode", true, "{ udpserver | udpclient | tcpserver | tcpclient }");
        options.addOption("p", "port", true, "port number. default: 4950");
        options.addOption("b", "buffer-size", true, "buffer size. default: 1024");
        options.addOption("i", "ip-address", true, "ip address. default: 127.0.0.1");
        options.addOption("t", "message-transfer-rate", true, "message time rate ( mtr ). default: 1");
        options.addOption("s", "seconds", true, "how long to run on client. default: 1");
        options.addOption("x", "text", true, "message text, default: herro");

        CommandLine cmd = null;

        try{
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        }
        catch (UnrecognizedOptionException e){
            System.err.println("Unrecognized option");
            help(options);
        }
        catch (MissingArgumentException e){
            System.err.println("Missign arguments options");
            help(options);
        }
        try{
            // check what -m option is set
            if (cmd.hasOption("m")){

                if (cmd.getOptionValue("m").equals("udpserver")){
                    udpserver = new UDPServer(cmd);
                    if (udpserver.valid()){
                        udpserver.run();
                    }
                    else {
                        help(options);
                    }
                }
                else if (cmd.getOptionValue("m").equals("udpclient")){
                    udpclient = new UDPClient(cmd);
                    if (udpclient.valid()){
                        udpclient.run();
                    }
                    else {
                        help(options);
                    }
                }
                else if (cmd.getOptionValue("m").equals("tcpclient")){
                    tcpclient = new TCPClient(cmd);
                    if (tcpclient.valid()){
                        try {
                            tcpclient.run();
                        }
                        catch (Exception e){
                            help(options);
                            System.exit(1);
                        }
                    }
                    else {
                        help(options);
                    }
                }

                else if (cmd.getOptionValue("m").equals("tcpserver")){
                    tcpserver = new TCPServer(cmd);
                    if (tcpserver.valid()){
                        try {
                            tcpserver.run();
                        }
                        catch (Exception e){
                            help(options);
                            System.exit(1);
                        }
                    }
                    else {
                        help(options);
                    }
                }


                else {
                    help(options);
                }

            }
            else {
                help(options);
            }

        }
        catch (NullPointerException e){
            System.err.println("Missign arguments options");
            help(options);
        }

    }

    private static void help(Options options){
        // good looking helper
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Network Application", options);
    }
}

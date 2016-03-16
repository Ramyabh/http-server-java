package se.jherrlin;

import org.apache.commons.cli.*;
import se.jherrlin.model.Host;
import se.jherrlin.tcp.TCPClient;
import se.jherrlin.tcp.TCPServer;


import java.io.IOException;

public class Main
{
    public static TCPServer tcpserver;
    public static TCPClient tcpclient;

    public static void main( String[] args ) throws ParseException,IOException {

        Options options = new Options();

        // commandline arguments
        options.addOption("h", "help", false, "show help.");
        options.addOption("m", "mode", true, "{ tcpserver | tcpclient }");
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

                Host host = null;

                if (cmd.getOptionValue("m").equals("tcpserver")){
                    host = new TCPServer(cmd);
                }

                else if (cmd.getOptionValue("m").equals("tcpclient")){
                    host = new TCPClient(cmd);
                }

                else {
                    help(options);
                    System.exit(1);
                }

                if (host.valid()){
                    try{
                        host.run();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
            else {
                help(options);
            }

        }
        catch (NullPointerException e){
            System.err.println("Missing arguments options");
            help(options);
        }

    }

    private static void help(Options options){
        // good looking helper
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Network Application", options);
    }
}

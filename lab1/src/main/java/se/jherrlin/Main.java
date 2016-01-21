package se.jherrlin;

import org.apache.commons.cli.*;

import java.io.IOException;

public class Main
{
    public static Server server;
    public static Client client;

    public static void main( String[] args ) throws ParseException,IOException {

        Options options = new Options();

        options.addOption("h", "help", false, "show help.");
        options.addOption("m", "mode", true, "{ server | client }");
        options.addOption("p", "port", true, "port number. default: 4950");
        options.addOption("b", "buffer-size", true, "buffer size. default: 1024");
        options.addOption("i", "ip-address", true, "ip address. default: 127.0.0.1");
        options.addOption("t", "message-transfer-rate", true, "message time rate ( mtr ). default: 1");
        options.addOption("s", "seconds", true, "how long to run on client. default: 1");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("m")){
            if (cmd.getOptionValue("m").equals("server")){
                server = new Server(cmd);
                if (server.valid()){
                    server.run();
                }
                else {
                    help(options);
                }
            }
            else if (cmd.getOptionValue("m").equals("client")){
                client = new Client(cmd);
                if (client.valid()){
                    client.run();
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

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Network Application", options);
    }
}

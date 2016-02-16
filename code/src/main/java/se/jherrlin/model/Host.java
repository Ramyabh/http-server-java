package se.jherrlin.model;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;

import se.jherrlin.utils.Utils;

public abstract class Host {

    // This class takes care of passing command line arguments
    // and validating the host attributes

    public String mode;
    public int port;
    public int bufsize;
    public int mtr;
    public int seconds;
    public String ip;
    public String message;
    public Utils utils = new Utils();

    final Logger LOG = Logger.getLogger(this.getClass().getSimpleName());

    public Host(CommandLine cmd) {
        // Commandline arguments comes in.
        // Validation and/or default values are set
        this.mode = cmd.getOptionValue("m");
        this.ip = cmd.getOptionValue("i") == null ? "0.0.0.0" : cmd.getOptionValue("i");
        this.message = cmd.getOptionValue("x") == null ? "GET http://www.server.com/page.html HTTP/1.1\n\nHeaders" : cmd.getOptionValue("x");

        try{
            this.port = cmd.getOptionValue("p") == null ? 8000 : Integer.parseInt(cmd.getOptionValue("p"));
            this.bufsize = cmd.getOptionValue("b") == null ? 20 : Integer.parseInt(cmd.getOptionValue("b"));
            this.mtr = cmd.getOptionValue("t") == null ? 1 : Integer.parseInt(cmd.getOptionValue("t"));
            this.seconds = cmd.getOptionValue("s") == null ? 1 : Integer.parseInt(cmd.getOptionValue("s"));  // Elvis has just left the building
        }catch(NumberFormatException e){
            LOG.debug(e.getMessage());
        }
        LOG.debug("initialized: " + this);
    }

    public void run() throws Exception {
        // Override this method
        // with specific host logic
        throw new UnsupportedOperationException();
    }

    public boolean valid(){
        // IP address
        InetAddressValidator inetAddressValidator = new InetAddressValidator();
        if (!inetAddressValidator.isValid(this.ip)){
            LOG.debug("ip is not a valid IPv4 address");
            return false;
        }
        // Port
        if (this.port > 65535 || this.port < 1 ){
            LOG.debug("port is not in the valid port range(1-65535)");
            return false;
        }
        // Mtr, Message Transfer Rate
        if (this.mtr < 0){
            LOG.debug("mtr value cant be below 0.");
            return false;
        }
        // Seconds
        if (this.seconds < 1){
            LOG.debug("seconds cant be below 1 second.");
            return false;
        }

        return true;
    }

    public void sleep(){
        try{
            // split one second with the mtr
            Thread.sleep(1000/this.mtr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // print format to the log
        return this.getClass().getName()+": { " +
                "mode: " + mode + ", " +
                "port: " + port + ", " +
                "bufsize: " + bufsize + ", " +
                "mtr: " + mtr + ", " +
                "seconds: " + seconds + ", " +
                "ip: " + ip +
                " }";
    }
}

package se.jherrlin;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArgumentsTest {

    @Test
    public void cmd_server_m_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 4950);
        assertTrue(main.server.bufsize == 1024);
        assertTrue(main.server.ip.equals("0.0.0.0"));
        assertTrue(main.server.mtr == 1);
        assertTrue(main.server.seconds == 1);
        assertTrue(main.server.valid());
    }

    @Test
    public void cmd_server_mp_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 1024);
        assertTrue(main.server.ip.equals("0.0.0.0"));
        assertTrue(main.server.mtr == 1);
        assertTrue(main.server.seconds == 1);
        assertTrue(main.server.valid());
    }
    @Test
    public void cmd_server_mpb_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80",
                "-b","666"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("0.0.0.0"));
        assertTrue(main.server.mtr == 1);
        assertTrue(main.server.seconds == 1);
        assertTrue(main.server.valid());
    }

    @Test
    public void cmd_server_mpbi_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80",
                "-b","666",
                "-i","192.168.0.1"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0.1"));
        assertTrue(main.server.mtr == 1);
        assertTrue(main.server.seconds == 1);
        assertTrue(main.server.valid());
    }

    @Test
    public void cmd_server_mpbit_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80",
                "-b","666",
                "-i","192.168.0.1",
                "-t","1500",
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0.1"));
        assertTrue(main.server.mtr == 1500);
        assertTrue(main.server.seconds == 1);
        assertTrue(main.server.valid());
    }

    @Test
    public void cmd_server_mpbits_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80",
                "-b","666",
                "-i","192.168.0.1",
                "-t","1500",
                "-s","666"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0.1"));
        assertTrue(main.server.mtr == 1500);
        assertTrue(main.server.seconds == 666);
        assertTrue(main.server.valid());
    }

    @Test
    public void cmd_server_notValidIP_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","80",
                "-b","666",
                "-i","192.168.0",
                "-t","1500",
                "-s","666"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 80);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0"));
        assertTrue(main.server.mtr == 1500);
        assertTrue(main.server.seconds == 666);
        assertFalse(main.server.valid());
    }

    @Test
    public void cmd_server_notValidPortNegative_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","-1",
                "-b","666",
                "-i","192.168.0.1",
                "-t","1500",
                "-s","666"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == -1);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0.1"));
        assertTrue(main.server.mtr == 1500);
        assertTrue(main.server.seconds == 666);
        assertFalse(main.server.valid());
    }

    @Test
    public void cmd_server_notValidPortToHigh_returnsTrue() throws IOException
    {
        String[] args = {
                "-m","server",
                "-p","66666",
                "-b","666",
                "-i","192.168.0.1",
                "-t","1500",
                "-s","666"
        };
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.port == 66666);
        assertTrue(main.server.bufsize == 666);
        assertTrue(main.server.ip.equals("192.168.0.1"));
        assertTrue(main.server.mtr == 1500);
        assertTrue(main.server.seconds == 666);
        assertFalse(main.server.valid());
    }
}

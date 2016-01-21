package se.jherrlin;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
/**
 * Unit test for simple App.
 */
public class MainTest
{
    @Test
    public void serverCanUseArgumentM_validArgument_returnsTrue() throws IOException
    {
        String[] args = {"-m","server"};
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
    }

    @Test
    public void serverCanUseArgumentMandP_validArgument_returnsTrue() throws IOException
    {
        String[] args = {"-m", "server", "-p", "80"};
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
    }

    @Test
    public void serverCanUseArgumentMandB_validArgument_returnsTrue() throws IOException
    {
        String[] args = {"-m", "server", "-b", "2048"};
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.bufsize == 2048);
    }

    @Test
    public void serverCanUseArgumentMandBandP_validArgument_returnsTrue() throws IOException
    {
        String[] args = {"-m", "server", "-p", "80", "-b", "2048"};
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(main.server instanceof Server);
        assertTrue(main.server.bufsize == 2048 && main.server.port == 80);
    }


    @Test
    public void clientCanUseArgumentM_validArgument_returnsTrue() throws IOException
    {
        String[] args = {"-m", "client"};
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(main.client instanceof Client);
        assertTrue(main.client.bufsize == 1024 && main.client.port == 4950);
    }

    @Test
    public void clientBadArgument_s_returnsTrue() throws IOException
    {
        String[] args = {"-m", "client", "-s", "abc"};
        Main main = new Main();
        try{
            main.main(args);
        }
        catch (ParseException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(main.client instanceof Client);
        //assertTrue(main.client.bufsize == 1024 && main.client.port == 4950);
    }


}

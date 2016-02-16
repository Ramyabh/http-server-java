package se.jherrlin.handlers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nils on 2/16/16.
 */
public class StaticHandler {

    public static byte[] findStaticFile(String filenameIn){

        try{
            File file = new File(StaticHandler.class.getResource(filenameIn).getPath());
            Path path = file.toPath();
            return Files.readAllBytes(path);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

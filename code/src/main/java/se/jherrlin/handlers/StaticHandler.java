package se.jherrlin.handlers;

import se.jherrlin.model.Response;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by nils on 2/16/16.
 */
public class StaticHandler {

    public static byte[] findStaticFile(String filenameIn){
        System.out.println(filenameIn);
        File file = new File(StaticHandler.class.getResource(filenameIn).getPath());
        File filepath = file.getParentFile();
        File[] staticfiles = filepath.listFiles();

        for (File f : staticfiles){
            if (f.getName().equals(file.getName())){
                try {
                    Path path = f.toPath();
                    return Files.readAllBytes(path);
                }
                catch (Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

        return null;
    }
}

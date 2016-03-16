package se.jherrlin.utils;

/**
 * Created by nils on 2/13/16.
 */
public class Utils {

    public static void sleep(){
        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

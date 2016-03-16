package se.jherrlin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by nils on 2/18/16.
 */
public class Db {

    public static void initDb(){
        Connection c = null;
        Statement stmta = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("Opened database successfully");

            stmta = c.createStatement();
            String blog = "CREATE TABLE BLOG (id INTEGER PRIMARY KEY, blog BLOB, uuid TEXT)";
            stmta.executeUpdate(blog);
            stmta.close();
            c.close();
        } catch ( Exception e ) {
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //System.exit(0);

        }
        System.out.println("Table created successfully");
    }
}

package se.jherrlin.db;

import org.apache.log4j.Logger;
import se.jherrlin.domain.Blog;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by nils on 2/18/16.
 */
public class Model implements Serializable{

    public boolean create(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            byte[] employeeAsBytes = baos.toByteArray();
            PreparedStatement pstmt = dbConn.prepareStatement("INSERT INTO BLOG (blog) VALUES(?)");
            ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
            pstmt.setBinaryStream(1, bais, employeeAsBytes.length);
            pstmt.executeUpdate();
            pstmt.close();

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(){return true;}

    public boolean delete(){return true;}

    public static ArrayList<Blog> getAll(){

        ArrayList<Blog> blogArrayList = new ArrayList<Blog>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");

            Statement stmt = dbConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT blog FROM BLOG");
            while (rs.next()) {
                byte[] st = (byte[]) rs.getObject(1);
                ByteArrayInputStream baip = new ByteArrayInputStream(st);
                ObjectInputStream ois = new ObjectInputStream(baip);
                Blog emp = (Blog) ois.readObject();
                System.out.println(emp.getHeader());
                blogArrayList.add(emp);
            }
            stmt.close();
            rs.close();
            dbConn.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return blogArrayList;
    }

    public static Blog getById(){

        return null;

    }

    public static void main(String[] args) {

        System.out.println(Model.getAll().size());
        for (Blog blog: Model.getAll()){
            System.out.printf(blog.getHeader());
        }
    }
}

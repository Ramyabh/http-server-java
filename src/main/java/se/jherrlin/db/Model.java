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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;

    public boolean create(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(this);
            byte[] blogAsBytes = baos.toByteArray();
            PreparedStatement pstmt = dbConn.prepareStatement("INSERT INTO BLOG (blog, uuid) VALUES(?, ?)");
            ByteArrayInputStream bais = new ByteArrayInputStream(blogAsBytes);
            pstmt.setBinaryStream(1, bais, blogAsBytes.length);
            pstmt.setString(2, this.uuid);

            pstmt.executeUpdate();
            pstmt.close();

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            byte[] blogAsBytes = baos.toByteArray();

            PreparedStatement ps = dbConn.prepareStatement("UPDATE BLOG SET blog=? where uuid=?");
            ByteArrayInputStream bais = new ByteArrayInputStream(blogAsBytes);
            ps.setBinaryStream(1, bais, blogAsBytes.length);
            ps.setString(2, this.uuid);
            ps.executeUpdate();

            ps.close();
            dbConn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public static Blog getById(String uuid){
        Blog blogPost = null;

        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");

            PreparedStatement ps = dbConn.prepareStatement("select * from BLOG where uuid=?");
            ps.setString(1, uuid);

            ResultSet rs = ps.executeQuery();
            byte[] result = (byte[]) rs.getObject(2);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(result);

            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            blogPost = (Blog) objectInputStream.readObject();

            rs.close();
            ps.close();
            dbConn.close();

        } catch (Exception e ) {
            e.printStackTrace();
        }
        return blogPost;
    }

    public static void main(String[] args) {

        String uuid = "";
        for (Blog blog: Model.getAll()){
            uuid = blog.getUuid();
        }
        System.out.println(Model.getById(uuid).getHeader());

        Blog blog = Model.getById(uuid);
        blog.setHeader("test");
        blog.update();

        System.out.println(Model.getById(uuid).getHeader());
    }
}

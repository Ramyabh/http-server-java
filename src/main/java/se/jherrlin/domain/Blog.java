package se.jherrlin.domain;


import se.jherrlin.db.Db;
import se.jherrlin.db.Model;

/**
 * Created by nils on 2/18/16.
 */
public class Blog extends Model{
    String header;
    String text;
    String imgName;
    String imgB64;

    public Blog() {
    }

    public Blog(String header, String text, String imgName, String imgB64) {
        this.header = header;
        this.text = text;
        this.imgName = imgName;
        this.imgB64 = imgB64;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgName() { return imgName; }

    public void setImgName(String imgName) { this.imgName = imgName; }

    public String getImgB64() { return imgB64; }

    public void setImgB64(String imgB64) { this.imgB64 = imgB64; }

    // Just for testing
    public static void main(String[] args) {
        Db.initDb();
        Blog blog = new Blog();
        blog.setHeader("My header");
        blog.setText("My text");
        blog.create();
    }
}


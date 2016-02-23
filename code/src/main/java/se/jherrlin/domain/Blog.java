package se.jherrlin.domain;

import java.util.UUID;

import se.jherrlin.db.Db;
import se.jherrlin.db.Model;

/**
 * Created by nils on 2/18/16.
 */
public class Blog extends Model{
    String header;
    String text;

    public Blog() {
    }

    public Blog(String header, String text) {
        this.header = header;
        this.text = text;
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

    // Just for testing
    public static void main(String[] args) {
        Db.initDb();
        Blog blog = new Blog();
        blog.setHeader("My header");
        blog.setText("My text");
        blog.create();
    }
}


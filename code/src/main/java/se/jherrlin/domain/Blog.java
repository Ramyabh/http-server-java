package se.jherrlin.domain;

import java.util.UUID;

import se.jherrlin.db.Db;
import se.jherrlin.db.Model;

/**
 * Created by nils on 2/18/16.
 */
public class Blog extends Model{
    UUID uuid;
    String header;
    String text;

    public Blog() {
    }

    public Blog(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public Blog(UUID uuid, String header, String text) {
        this.uuid = uuid;
        this.header = header;
        this.text = text;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public static void main(String[] args) {
        Db.initDb();
        Blog blog = new Blog();
        UUID uuid = UUID.randomUUID();
        blog.setUuid(uuid);
        blog.setHeader("My header");
        blog.setText("My text");
        blog.create();
    }
}


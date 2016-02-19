package se.jherrlin.domain;

/**
 * Created by nils on 2/19/16.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {
    public static void main(String args[]) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection dbConn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Employee employee = new Employee(42, "John", 9);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(employee);
        byte[] employeeAsBytes = baos.toByteArray();
        PreparedStatement pstmt = dbConn
                .prepareStatement("INSERT INTO EMPLOYEE (emp) VALUES(?)");
        ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes);
        pstmt.setBinaryStream(1, bais, employeeAsBytes.length);
        pstmt.executeUpdate();
        pstmt.close();
        Statement stmt = dbConn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT emp FROM EMPLOYEE");
        while (rs.next()) {
            byte[] st = (byte[]) rs.getObject(1);
            ByteArrayInputStream baip = new ByteArrayInputStream(st);
            ObjectInputStream ois = new ObjectInputStream(baip);
            Employee emp = (Employee) ois.readObject();
            System.out.println(emp.getName());
        }
        stmt.close();
        rs.close();
        dbConn.close();
    }
}

class Employee implements Serializable {
    int ID;
    String name;
    double salary;

    public Employee(int ID, String name, double salary) {
        this.ID = ID;
        this.name = name;
        this.salary = salary;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}

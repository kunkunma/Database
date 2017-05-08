/**
 * Created by makunkun on 5/7/17.
 */
import java.sql.*;
import java.util.Scanner;

public class dbexampleccp {
    public static final String SERVER   = "jdbc:mysql://sunapee.cs.dartmouth.edu/";
    public static final String USERNAME = "makuniris";
    public static final String PASSWORD = "8155069";
    public static final String DATABASE = "makuniris_db";
    public static final String QUERY    = "SELECT * FROM EDITOR WHERE EDITOR_ID=";

    public static void main(String[] args) {
        Register register;

        System.out.println("Welcome to our database! \nPlease select to login or register");
        Scanner in = new Scanner(System.in);

        String line = in.next();
        if(line.equals("login")){
            System.out.println("Please input your ID");
            line = in.next();
            // attempt to connect to db and execute select all query
            connectToDB(null,null,null,0,line);

        }
        else if(line.equals("register")){

            register = new Register();
            register.registerProcess();
        }


    }

    public static void connectToDB(Connection con,Statement stmt,ResultSet res,int numColumns,String line) {
        try {
            // load mysql driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // initialize connection
            con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);

            System.out.println("Connection established.");

            // initialize a query statement
            stmt = con.createStatement();
            // query db and save results
            res = stmt.executeQuery(QUERY+line);

            System.out.format("Query executed: '%s'\n\nResults:\n", QUERY);

            // the result set contains metadata
            numColumns = res.getMetaData().getColumnCount();

            // print table header
            for(int i = 1; i <= numColumns; i++) {
                System.out.format("%-12s", res.getMetaData().getColumnName(i));
            }
            System.out.println("\n--------------------------------------------");

            // iterate through results
            while(res.next()) {
                for(int i = 1; i <= numColumns; i++) {
                    System.out.format("%-12s", res.getObject(i));
                }
                System.out.println("");
            }
        } catch (SQLException e ) {          // catch SQL errors
            System.err.format("SQL Error: %s", e.getMessage());
        } catch (Exception e) {              // anything else
            e.printStackTrace();
        } finally {
            // cleanup
            try {
                con.close();
                stmt.close();
                res.close();
                System.out.print("\nConnection terminated.");
            } catch (Exception e) { /* ignore cleanup errors */ }
        }
    }
}
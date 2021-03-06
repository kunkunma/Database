import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by makunkun on 5/9/17.
 */
public class Login {
    public static final String SERVER   = "jdbc:mysql://sunapee.cs.dartmouth.edu/";
    public static final String USERNAME = "makuniris";
    public static final String PASSWORD = "8155069";
    public static final String DATABASE = "makuniris_db";

    String AuthorQuery = "SELECT AUT_FN,AUT_LN,AUT_MAILING_ADDR FROM AUTHOR " +
            "WHERE AUT_ID = ";
    String AuthorStatusViewQuery = "Select * FROM LeadAuthorManuscripts WHERE AUT_ID = ";
    String AuthorDeleteManuQuery = "DELETE FROM MANUSCRIPT WHERE MANU_ID =";
    String role;
    ArrayList<Integer> retrieveAuthorID;
    public void loginProcess() {
        Scanner in = new Scanner(System.in);
        System.out.println("Are you login as author, editor or reviewer?");

        while(true) {
            role = in.next();
            if (role.equals("author")) {
                authorLogin();
                break;
            }
            if(role.equals("editor")) {
                editorLogin();
                break;
            }
            if(role.equals("reviewer")){
                //System wide unique id ??
                reviewerLogin();

                break;
            }
            else{
                System.out.println("Please input from one of the three: author, editor or reviewer!");
                continue;
            }
    }
}

    private void reviewerLogin() {
    }

    private void editorLogin() {
    }

    private void authorLogin() {
        System.out.println("Please input your author id:");
        Scanner in = new Scanner(System.in);
        String id = in.nextLine();
        connectToDBAndQuery(AuthorQuery + id, null,null,null,0,0);
        System.out.println("\nPlease input status command to get all the info");
        String command  = in.nextLine();
        if(command.equals("status")){
            connectToDBAndQuery(AuthorStatusViewQuery + id,null,null,null,0,1);
        }

        //retract a manuscript
        System.out.println("Type in Retract if you want to retract");

        command = in.nextLine();
        if (command.equals("retract")){
            System.out.println("\nWhich paper do you want to retract? please input the paper id");

            while (true) {
                int deleteID = Integer.parseInt(in.nextLine());
                //check if the input id contains in the
                if (retrieveAuthorID.contains(deleteID)){
                    System.out.println("Are you sure?");
                    command = in.nextLine();
                    if (command.equals("yes")) {
                        connectToDBAndDelete(AuthorDeleteManuQuery + deleteID,null,null);
                        break;
                    }
                }
                else {
                    System.out.println("Please input correct id!");
                }
            }
        }
    }

    private void connectToDBAndDelete(String QUERY, Connection con, Statement stmt) {
        try {
            // load mysql driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // initialize connection
            con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);

            System.out.println("Connection established.");


            // initialize a query statement
            stmt = con.createStatement();
            // query db and save results
            stmt.executeUpdate(QUERY);
            //get generate id

            System.out.format("Query executed: '%s'\n", QUERY);

        } catch (SQLException e ) {          // catch SQL errors
            System.err.format("SQL Error: %s", e.getMessage());
        } catch (Exception e) {              // anything else
            e.printStackTrace();
        } finally {
            // cleanup
            try {
                con.close();
                stmt.close();
                System.out.print("\nConnection terminated.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void connectToDBAndQuery(String QUERY, Connection con, Statement stmt, ResultSet res, int numColumns,int flag) {
        ArrayList<Integer> id = new ArrayList<>();
        try {
            // load mysql driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // initialize connection
            con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);

            System.out.println("Connection established.");

            // initialize a query statement
            stmt = con.createStatement();
            // query db and save results
            res = stmt.executeQuery(QUERY);

            System.out.format("Query executed: '%s'\n\nresult:\n", QUERY);

            // the result set contains metadata
            numColumns = res.getMetaData().getColumnCount();
            System.out.println("Welcome to our database!");
            // print table header
            for(int i = 1; i <= numColumns; i++) {
                System.out.format("%-12s", res.getMetaData().getColumnName(i));
            }
            System.out.println("\n--------------------------------------------");

            // iterate through results
            while(res.next()) {
                if (flag==1) {
                    id.add(res.getInt(7));
                }
                for(int i = 1; i <= numColumns; i++) {
                    System.out.format("%-12s", res.getObject(i));
                }
                System.out.println("");
            }
            this.retrieveAuthorID = id;

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

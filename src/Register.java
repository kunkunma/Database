import java.sql.*;
import java.util.Scanner;

/**
 * Created by makunkun on 5/7/17.
 */
public class Register {
    public static final String SERVER   = "jdbc:mysql://sunapee.cs.dartmouth.edu/";
    public static final String USERNAME = "makuniris";
    public static final String PASSWORD = "8155069";
    public static final String DATABASE = "makuniris_db";
    String insertAuthor = "INSERT INTO AUTHOR VALUES (null,";
    String insertEditor = "INSERT INTO EDITOR VALUES (null,";
    String insertReviewer = "INSERT INTO REVIEWER VALUES (null,";
    String role;
    String insertRIC = "INSERT INTO INTEREST VALUES (";

    public Register(){
    }

    //This method start register for a new author, reviewer, or editor
    //need to consider what to do after inserting info???????????????
    public void registerProcess(){
        Scanner in = new Scanner(System.in);
        System.out.println("Are you an author, editor or reviewer?");

        while(true) {
            role = in.next();
            if (role.equals("author")) {
                authorRegister();
                break;
            }
            if(role.equals("editor")) {
                editorRegister();
                break;
            }
            if(role.equals("reviewer")){
                //System wide unique id ??
                reviewerRegister();

                break;
            }
            else{
                System.out.println("Please input from one of the three: author, editor or reviewer!");
                continue;
            }
        }
    }

    private void reviewerRegister() {
        System.out.println("Please type in your last name, first name, email, affiliation");

        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String[] array = line.split(",");

        //construct the SQL query
        int i;
        for(i = 0; i < array.length-1; i++) {
            insertReviewer = insertReviewer + "'"+array[i] + "'" + ",";
        }
        insertReviewer = insertReviewer + "'" + array[i] +  "'" +  ")";

        //get inserted reviewer id
        int rid = connectToDBAndInsert(insertReviewer,null,null,null,0);

        //insert RICode into Table interest, at most 3
        System.out.println("\nPlease input your RIcode:");
        line = in.nextLine();
        array = line.split(",");
        for (i=0; i<array.length && i<3; i++) {
            connectToDBAndInsert(insertRIC + rid + "," + array[i]+ ")", null,null,null,0);
        }

    }

    public void editorRegister() {
        System.out.println("Please type in your last name, first name");
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String[] array = line.split(",");

        //construct the SQL query
        int i;
        for(i = 0; i < array.length-1; i++) {
            insertEditor = insertEditor + "'"+array[i] + "'" + ",";
        }
        insertEditor = insertEditor + "'" + array[i] +  "'" +  ")";
        connectToDBAndInsert(insertEditor,null,null,null,0);

    }

    public void authorRegister(){
        System.out.println("Please type in your info as following format: your last name, first name, " +
                "mailing address,email, affiliation");
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String[] array = line.split(",");

        //construct the SQL query
        int i;
        for(i = 0; i < array.length-1; i++) {
            insertAuthor = insertAuthor + "'"+array[i] + "'" + ",";
        }
        insertAuthor = insertAuthor + "'" + array[i] +  "'" +  ")";
        connectToDBAndInsert(insertAuthor,null,null,null,0);
    }

    public static int connectToDBAndInsert(String QUERY, Connection con, PreparedStatement stmt, ResultSet res, int numColumns) {
        int id = -1;
        try {
            // load mysql driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // initialize connection
            con = DriverManager.getConnection(SERVER+DATABASE, USERNAME, PASSWORD);

            System.out.println("Connection established.");

            // initialize a query statement
            stmt = con.prepareStatement(QUERY, stmt.RETURN_GENERATED_KEYS);
            // query db and save results
            stmt.executeUpdate();
            //get generate id

            res = stmt.getGeneratedKeys();
            if(res.next()) {
                id = res.getInt(1);
            }
            System.out.format("Query executed: '%s'\n\ninsertID:%d\n", QUERY,id);
            System.out.println("Data already insert...");

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }


}

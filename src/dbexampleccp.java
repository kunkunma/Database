/**
 * Created by makunkun on 5/7/17.
 */
import java.sql.*;
import java.util.Scanner;

public class dbexampleccp {

    public static void main(String[] args) {
        Register register;
        Login login;
        System.out.println("Welcome to our database! \nPlease select to login or register");
        Scanner in = new Scanner(System.in);

        String line = in.next();
        if(line.equals("login")){
            login = new Login();
            login.loginProcess();
            System.out.println("Please input your ID");
        }
        else if(line.equals("register")){

            register = new Register();
            register.registerProcess();
        }


    }

}
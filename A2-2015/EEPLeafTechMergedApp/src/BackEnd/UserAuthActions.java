package BackEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 *
 * @author austinankney
 */
public class UserAuthActions {

    private static String errString = null;            // String for displaying errors
    private static String msgString = null;            // String for displaying non-error messages
    private static String loginDateTime = null;
    private static String sourceURL = null;

    public UserAuthActions() {
        String SQLServerIP = "localhost";
        sourceURL = "jdbc:mysql://" + SQLServerIP + ":3306/login";
    }

    public static boolean login(String username, char[] password) {
        // Connect to database
        Connection connect = connectDB("login");

        // Compare user supplied credentials to database
        boolean loggedOn = authenticate(username, password, connect);

        // Record user's logon time
        if (loggedOn) {
            recordLogin(username, connect);
        }

        return loggedOn;
    }

    public static void logout(String username) {
        // Connect to database
        Connection connect = connectDB("login");

        recordLogout(username, connect);
    }

    public static void recordLogout(String username, Connection DBConn) {
        Statement s = null;                 // SQL statement pointer

        DateFormat dt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String logoutDateTime = dt.format(date);

        try {
            s = DBConn.createStatement();
            String addLogoutRecord = "UPDATE record SET logout_time= \"" + logoutDateTime + "\" WHERE login_time= \"" + loginDateTime + "\";";
            int executeUpdateVal = s.executeUpdate(addLogoutRecord);
        } catch (Exception e) {
            errString = "\nProblem adding logout record: " + e;
            System.out.println(errString);
        }

    }

    public static void recordLogin(String username, Connection DBConn) {
        Statement s = null;                 // SQL statement pointer

        DateFormat dt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        loginDateTime = dt.format(date);

        try {
            s = DBConn.createStatement();
            String addLoginRecord = "INSERT into record VALUES (\"" + username + "\",\"" + loginDateTime + "\",\"null\")";
            int executeUpdateVal = s.executeUpdate(addLoginRecord);
        } catch (Exception e) {
            errString = "\nProblem adding login record" + e;
            System.out.println(errString);
        }
        System.out.println("login records added to databases for username:" + username + ".");
    }

    public static Connection connectDB(String database) {

        // Database parameters
        Boolean connectError = false;       // Error flag
        Connection DBConn = null;           // MySQL connection handle

        try {

            System.out.println("Establishing connection with: " + sourceURL);
            DBConn = DriverManager.getConnection(sourceURL, "remote", "remote_pass");
            System.out.println("Successfully connected to database");

            return DBConn;

        } catch (Exception e) {

            errString = "\nProblem connecting to database:: " + e;
            System.out.println(errString);
            return DBConn;
        }
    }

    private static boolean authenticate(String username, char[] password, Connection DBConn) {
        Statement s = null;                 // SQL statement pointer
        ResultSet res = null;               // SQL query result set pointer

        char[] returnPass = null;

        try {
            s = DBConn.createStatement();

            res = s.executeQuery("SELECT password FROM auth WHERE username = \"" + username + "\";");

            while (res.next()) {
                String tempPass = res.getString(1);
                returnPass = tempPass.toCharArray();
            }

            if (Arrays.equals(password, returnPass)) {
                System.out.println("Logging in...");
                return true;
            } else {
                System.out.println("Credentials didnt match those on record.");
                return false;
            }
        } catch (Exception e) {
            errString = "\nProblem getting tree inventory:: " + e;
            System.out.println(errString);
            return false;
        }
    }
}

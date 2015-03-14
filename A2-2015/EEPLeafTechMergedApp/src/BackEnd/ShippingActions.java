package BackEnd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShippingActions {
	Boolean connectError = false; // Error flag
	Connection DBConn = null; // MySQL connection handle
	String description; // Tree, seed, or shrub description
	int executeUpdateVal; // Return value from execute indicating effected rows
	Boolean fieldError = false; // Error flag
	String tableSelected = null; // String used to determine which data table to
									// use
	Integer quantity; // Quantity of trees, seeds, or shrubs
	Float perUnitCost; // Cost per tree, seed, or shrub unit
	String productID = null; // Product id of tree, seed, or shrub
	java.sql.Statement s = null; // SQL statement pointer

	public ShippingActions() {
		String errString = null; // String for displaying errors

		try {
			String msgString = null; // String for displaying non-error messages
			msgString = ">> Establishing Driver...";
			System.out.println("\n" + msgString);

			// load JDBC driver class for MySQL
			Class.forName("com.mysql.jdbc.Driver");

			msgString = ">> Setting up URL...";
			System.out.println("\n" + msgString);

			// define the data source, read from file in the same folder
			URL ipPath = getClass().getResource("DatabaseIP");
                        File ipFile = new File(ipPath.getPath());
                        InputStream is = new FileInputStream(ipFile);
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));

			line = reader.readLine();
			if (line == null) {
				System.out.println("Cannot find the database ip file!");
				reader.close();
				is.close();
				System.exit(-1);
			}
			reader.close();
			is.close();

			String SQLServerIP = line.replace('\n', ' ').trim();
			//String sourceURL = "jdbc:mysql://" + SQLServerIP
			//		+ ":3306/inventory";
                        
                        String sourceURL = "jdbc:mysql://" + SQLServerIP
					+ ":3306/orderinfo";

			msgString = ">> Establishing connection with: " + sourceURL + "...";
			System.out.println("\n" + msgString);

			// create a connection to the db
			DBConn = DriverManager.getConnection(sourceURL, "remote",
					"remote_pass");

		} catch (Exception e) {

			errString = "\nProblem connecting to database:: " + e;
			System.out.println(errString);
			connectError = true;
			System.exit(-1);
		} // end try-catch
	}

	public int updateShipStatus(String updateOrderID) {
		String SQLStatement = null; // String for building SQL queries
		
        int rows = 0;                           // Rows updated

        try
        {
            // first we create the query
            s = DBConn.createStatement();
            SQLStatement = "UPDATE orders SET shipped=" + true + " WHERE order_id=" + updateOrderID;

            // execute the statement
            rows = s.executeUpdate( SQLStatement );

            // if the query worked, then we display the data in TextArea 4 - BTW, its highly
            // unlikely that the row won't exist and if it does the database tables are
            // really screwed up... this should not fail if you get here, but the check (in the
            // form of the else clause) is in place anyway

        } catch (Exception e) {
            System.out.println("\nProblem updating status:: " + e);
        } // end try-catch
        
        return rows;
	}

	public List<List<String>> getOrders(String orderID) {
		ResultSet res = null; // SQL query result set pointer
		List<List<String>> orderlst = new ArrayList<List<String>>();
		List<String> tablelst = new ArrayList<String>();
		String SQLStatement = null; // String for building SQL queries
		String orderTable = null;

		try {
			s = DBConn.createStatement();
			SQLStatement = "SELECT * FROM orders WHERE order_id = "
					+ Integer.parseInt(orderID);
			res = s.executeQuery(SQLStatement);

			// Get the information from the database. Display the
			// first and last name, address, phone number, address, and
			// order date. Same the ordertable name - this is the name of
			// the table that is created when an order is submitted that
			// contains the list of order items.

			while (res.next()) {
				orderTable = res.getString(9); // name of table with list of
												// items
				tablelst.add(orderTable);
				tablelst.add(res.getString(3)); // first name
				tablelst.add(res.getString(4)); // last name
				tablelst.add(res.getString(6)); // phone
				tablelst.add(res.getString(2)); // order date
				tablelst.add(res.getString(5)); // address
				orderlst.add(tablelst);
			} // for each element in the return SQL query

		} catch (Exception e) {
			//System.out.println("\nProblem list inventory:: " + e);
		} // try

		return orderlst;
	}

	public List<String> getOrdersItems(String orderTable) {
		ResultSet res = null; // SQL query result set pointer
		List<String> lst = new ArrayList<String>();
		String SQLStatement = null; // String for building SQL queries
		String msgString = null; // String for displaying non-error messages


		try {
			// get the order items from the related order table
			SQLStatement = "SELECT * FROM " + orderTable;
			res = s.executeQuery(SQLStatement);

			// list the items on the form that comprise the order
			while (res.next()) {
				msgString = res.getString(1) + ":  PRODUCT ID: "
						+ res.getString(2) + "  DESCRIPTION: "
						+ res.getString(3) + "  PRICE $" + res.getString(4);
				lst.add(msgString + "\n");

			} // while
		} catch (Exception e) {
			//System.out.println("\nProblem get OrdersItems:: " + e);
		} // try
		return lst;
	}

	/*
	 * Return the orders which are not shipped(0) yet
	 */
	public String getAllOrders(int status) {
		ResultSet res = null; // SQL query result set pointer
		String output = null;
		String msgString = null; // String for displaying non-error messages

		try {
			// Create a query to get all the orders and execute the query
			s = DBConn.createStatement();
			res = s.executeQuery("Select * from orders");

			// For each row returned, we check the shipped status. If it is
			// equal to 0 it means it has not been shipped as of yet, so we
			// display it in TextArea 1. Note that we use an integer because
			// MySQL stores booleans and a TinyInt(1), which we interpret
			// here on the application side as an integer. It works, it just
			// isn't very elegant.
			while (res.next()) {
				int shippedStatus = Integer.parseInt(res.getString(8));

				if (shippedStatus == status) {
					msgString = "ORDER # " + res.getString(1) + " : "
							+ res.getString(2) + " : " + res.getString(3)
							+ " : " + res.getString(4);
					output = (msgString + "\n");

				} // shipped status check

			} // while

                        /*
			// notify the user all went well and enable the select order
			// button
			msgString += "\nPENDING ORDERS RETRIEVED...";
			output += (msgString);
                        */

		} catch (Exception e) {

			output += "\nProblem getting tree inventory:: " + e;
			//System.out.println(output);
		} // end try-catch
		
		return output;
	}

        /*
	public static void main(String[] args) {
		ShippingActions sa = new ShippingActions();
		sa.getOrders("25");
		sa.getOrdersItems("order0002");
		sa.getAllOrders();
		sa.updateShipStatus("25");
		sa.getAllOrders();
		return;
	}
        */
}

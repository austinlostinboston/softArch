import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.SwitchPoint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InventoryActions {
	Boolean connectError = false; // Error flag
	Connection DBConn = null; // MySQL connection handle
	Connection DBConn2 = null; // MySQL connection handle

	String description; // Tree, seed, or shrub description
	int executeUpdateVal; // Return value from execute indicating effected rows
	Boolean fieldError = false; // Error flag
	ResultSet res = null; // SQL query result set pointer
	String tableSelected = null; // String used to determine which data table to
									// use
	Integer quantity; // Quantity of trees, seeds, or shrubs
	Float perUnitCost; // Cost per tree, seed, or shrub unit
	String productID = null; // Product id of tree, seed, or shrub

	InventoryActions() {
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
			InputStream is = new FileInputStream("DatabaseIP");
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
			String sourceURL = "jdbc:mysql://" + SQLServerIP
					+ ":3306/inventory";

			msgString = ">> Establishing connection with: " + sourceURL + "...";
			System.out.println("\n" + msgString);

			// create a connection to the db
			DBConn = DriverManager.getConnection(sourceURL, "remote",
					"remote_pass");

			String sourceURL2 = "jdbc:mysql://" + SQLServerIP
					+ ":3306/leaftech";

			msgString = ">> Establishing connection with: " + sourceURL2
					+ "...";
			System.out.println("\n" + msgString);

			// create a connection to the db
			DBConn2 = DriverManager.getConnection(sourceURL2, "remote",
					"remote_pass");

		} catch (Exception e) {

			errString = "\nProblem connecting to database:: " + e;
			System.out.println(errString);
			connectError = true;
			System.exit(-1);
		} // end try-catch
	}

	public String addInventory(String type, String description,
			String productID, String quantity, String perUnitCost) {
		Boolean executeError = false; // Error flag
		String errString = null; // String for displaying errors
		String SQLstatement = null; // String for building SQL queries
		String output = null;
		java.sql.Statement s = null; // SQL statement pointer

		// insert inventory into table named as
		// type(trees/shrubs...)
		try {

			if (type == "trees" || type == "seeds" || type == "shrubs") {
				s = DBConn.createStatement();

				// insert inventory into table named as
				// type(trees/shrubs...)
				SQLstatement = ("INSERT INTO " + type + " (product_code, "
						+ "description, quantity, price) VALUES ( '"
						+ productID + "', " + "'" + description + "', "
						+ quantity + ", " + perUnitCost + ");");
			} else {
				s = DBConn2.createStatement();
				SQLstatement = ("INSERT INTO  "
						+ type
						+ "  (productid, "
						+ "productdescription, productquantity, productprice) VALUES ( '"
						+ productID + "', " + "'" + description + "', "
						+ quantity + ", " + perUnitCost + ");");
			}

			// execute the update
			executeUpdateVal = s.executeUpdate(SQLstatement);

			// let the user know all went well

			output = ("\nINVENTORY UPDATED... The following was added to the "
					+ type + " inventory...\n");
			output += ("\nProduct Code:: " + productID);
			output += ("\nDescription::  " + description);
			output += ("\nQuantity::     " + quantity);
			output += ("\nUnit Cost::    " + perUnitCost);

		} catch (Exception e) {
			errString = "\nProblem adding inventory:: " + e;
			System.out.println(errString);
			executeError = true;
		} // try

		return output;
	}

	public List<String> listInventory(String type) {
		String errString = null; // String for displaying errors
		String msgString = null; // String for displaying non-error messages
		List<String> lst = new ArrayList<String>();
		java.sql.Statement s = null; // SQL statement pointer

		try {
			if (type == "trees" || type == "seeds" || type == "shrubs") {
				s = DBConn.createStatement();
			} else {
				s = DBConn2.createStatement();
			}

			res = s.executeQuery("Select * from " + type);

			// let the user know all went well

			while (res.next()) {
				msgString = type + ">>" + res.getString(1) + "::"
						+ res.getString(2) + " :: " + res.getString(3) + "::"
						+ res.getString(4);
				lst.add("\n" + msgString);
			} // while

		} catch (Exception e) {
			lst.add("\nProblem list inventory:: " + e);
			System.out.println(lst.toString());
		} // try

		return lst;
	}

	public String deleteInventory(String type, String productID) {
		Boolean executeError = false; // Error flag
		String SQLstatement = null; // String for building SQL queries
		String output = null;
		java.sql.Statement s = null; // SQL statement pointer

		try {
			if (type == "trees" || type == "seeds" || type == "shrubs") {
				s = DBConn.createStatement();
				SQLstatement = ("DELETE FROM " + type
						+ " WHERE product_code = '" + productID + "';");
			} else {
				s = DBConn2.createStatement();
				SQLstatement = ("DELETE FROM " + type + " WHERE productid = '"
						+ productID + "';");
			}

			// execute the delete query

			executeUpdateVal = s.executeUpdate(SQLstatement);

			// let the user know all went well

			output = ("\n\n" + productID + " deleted...");
			output += ("\n Number of items deleted: " + executeUpdateVal);

		} catch (Exception e) {
			output += "\nProblem with delete:: " + e;
			System.out.println(output);
			executeError = true;
		} // try

		return output;
	}

	public List<String> decrementInventory(String type, String productID) {
		Boolean executeError = false; // Error flag
		String SQLstatement1 = null; // String for building SQL queries
		String SQLstatement2 = null; // String for building SQL queries
		String msgString = null; // String for displaying non-error messages
		java.sql.Statement s = null; // SQL statement pointer

		List<String> lst = new ArrayList<String>();

		try {
			if (type == "trees" || type == "seeds" || type == "shrubs") {

				s = DBConn.createStatement();

				SQLstatement1 = ("UPDATE " + type
						+ " set quantity=(quantity-1) where product_code = '"
						+ productID + "';");
				SQLstatement2 = ("SELECT * from " + type
						+ " where product_code = '" + productID + "';");
			} else {
				s = DBConn2.createStatement();

				SQLstatement1 = ("UPDATE " + type
						+ " set productquantity=(productquantity-1) where productid = '"
						+ productID + "';");
				SQLstatement2 = ("SELECT * from cultureboxes where productid = '"
						+ productID + "';");
			}

			// execute the update, then query the BD for the table entry for the
			// item just changed
			// and display it for the user

			executeUpdateVal = s.executeUpdate(SQLstatement1);
			res = s.executeQuery(SQLstatement2);

			System.out
					.println("\n\n" + productID + " inventory decremented...");

			while (res.next()) {
				msgString = type + ">> " + res.getString(1) + " :: "
						+ res.getString(2) + " :: " + res.getString(3) + " :: "
						+ res.getString(4);
				lst.add("\n" + msgString);

			} // while

			lst.add("\n\n Number of items updated: " + executeUpdateVal);

		} catch (Exception e) {
			lst.add("\nProblem with delete:: " + e);
			System.out.println(lst.toString());
			executeError = true;
		} // try

		return lst;
	}

	public static void main(String[] args) {
		InventoryActions ia = new InventoryActions();
		
		ia.addInventory("cultureboxes", "a box to culture plants", "000001", "4", "200");
		ia.addInventory("cultureboxes", "a box to culture plants", "000002", "4", "200");
		ia.listInventory("cultureboxes");
		ia.deleteInventory("cultureboxes", "000002");
		ia.decrementInventory("cultureboxes", "000001");
		ia.deleteInventory("cultureboxes", "200001");
		
		ia.addInventory("trees", "an apple tree", "000001", "3", "1200");
		ia.addInventory("trees", "an apple tree", "000002", "3", "1200");
		ia.listInventory("trees");
		ia.deleteInventory("trees", "000002");
		ia.decrementInventory("trees", "000001");
		ia.deleteInventory("trees", "000001");
		
		return;
	}
}

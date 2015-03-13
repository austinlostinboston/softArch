import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class OrderActions {
	Boolean connectError = false; // Error flag
	Connection DBConn = null; // MySQL connection handle
	String description; // Tree, seed, or shrub description
	int executeUpdateVal; // Return value from execute indicating effected rows
	Boolean fieldError = false; // Error flag
	String msgString = null; // String for displaying non-error messages
	ResultSet res = null; // SQL query result set pointer
	String tableSelected = null; // String used to determine which data table to
									// use
	Integer quantity; // Quantity of trees, seeds, or shrubs
	Float perUnitCost; // Cost per tree, seed, or shrub unit
	String productID = null; // Product id of tree, seed, or shrub
	java.sql.Statement s = null; // SQL statement pointer

	OrderActions() {
		String errString = null; // String for displaying errors

		try {
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

		} catch (Exception e) {

			errString = "\nProblem connecting to database:: " + e;
			System.out.println(errString);
			connectError = true;
			System.exit(-1);
		} // end try-catch
	}

	public String createTable(String orderTableName) {
		Boolean executeError = false; // Error flag
		String SQLstatement = null; // String for building SQL queries
		String output = null;

		try {
			s = DBConn.createStatement();

			SQLstatement = ("CREATE TABLE "
					+ orderTableName
					+ "(item_id int unsigned not null auto_increment primary key, "
					+ "product_id varchar(20), description varchar(80), " + "item_price float(7,2) );");

			executeUpdateVal = s.executeUpdate(SQLstatement);

		} catch (Exception e) {

			output += "\nProblem creating order table " + orderTableName
					+ ":: " + e;
			System.out.println(output);
			executeError = true;
		} // try

		return output;
	}

	public String insertOrder(String dateTimeStamp, String firstName,
			String lastName, String customerAddress, String phoneNumber,
			String fCost, String strfalse, String orderTableName) {
		Boolean executeError = false; // Error flag
		String SQLstatement = null; // String for building SQL queries
		String output = null;

		try {
			SQLstatement = ("INSERT INTO orders (order_date, " + "first_name, "
					+ "last_name, address, phone, total_cost, shipped, "
					+ "ordertable) VALUES ( '" + dateTimeStamp + "', " + "'"
					+ firstName + "', " + "'" + lastName + "', " + "'"
					+ customerAddress + "', " + "'" + phoneNumber + "', "
					+ fCost + ", " + false + ", '" + orderTableName + "' );");

			executeUpdateVal = s.executeUpdate(SQLstatement);

		} catch (Exception e1) {

			output += "\nProblem with inserting into table orders:: " + e1;
			System.out.println(output);
			executeError = true;

			try {
				SQLstatement = ("DROP TABLE " + orderTableName + ";");
				executeUpdateVal = s.executeUpdate(SQLstatement);

			} catch (Exception e2) {

				output += "\nProblem deleting unused order table:: "
						+ orderTableName + ":: " + e2;
				System.out.println(output);

			} // try

		} // try

		return output;
	}

	public String insertItems(String orderTableName, String productID,
			String description, String perUnitCost) {
		Boolean executeError = false; // Error flag
		String SQLstatement = null; // String for building SQL queries
		String output = null;

		SQLstatement = ("INSERT INTO " + orderTableName
				+ " (product_id, description, item_price) " + "VALUES ( '"
				+ productID + "', " + "'" + description + "', " + perUnitCost + " );");
		try {
			executeUpdateVal = s.executeUpdate(SQLstatement);

		} catch (Exception e) {

			output += "\nProblem with inserting into table " + orderTableName
					+ ":: " + e;
			System.out.println(output);

		} // try

		return output;
	}

	public static void main(String[] args) {
		OrderActions oa = new OrderActions();
		oa.createTable("order0001");
		oa.createTable("order0002");
		oa.insertItems("order0002", "000001", "an apple tree", "1200");
		oa.insertOrder("2015-01-01 23:00:00", "Doe", "John", "pit,pa",
				"4123432324", "2900", "false", "order0001");
		return;
	}

}
package database;

import models.Menu;
import models.Order;
import models.OrderList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Handling all database logics and query
 * @author Gia Khanh Dinh
 */
public class Database {
    private static Database instance;
    private Connection connection; //represents a database connection session

    private Database() {
        createConnection();
    }

    /**
     * Create a connection to a database
     */
    private void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //call the driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/test", "root", "root");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute a SQL SELECT query
     * @param query the query string
     * @return an intance of the result set
     * @throws Exception
     */
    public ResultSet executeQuery(String query) throws Exception {
        Statement myStatement = connection.createStatement();
        return myStatement.executeQuery(query);
    }

    /**
     * Execute an update query (UPDATE, DELETE, INSERT, etc.)
     * @param query the query string
     * @throws Exception
     */
    public void executeUpdate(String query) throws Exception {
        Statement myStatement = connection.createStatement();
        myStatement.executeUpdate(query);
    }

    public static Database instance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    /**
     * Fetch menu and orders to the interface
     */
    public void setUp() {
        Menu.instance().fetchMenu();
        OrderList.instance().fetchOrders();
    }

    /**
     * Insert a new Order to the database
     * @param order the order object to be added
     * @param foodId the foodId of the order
     * @param beverageId the beverage Id of the order
     */
    public void addOrder(Order order, int foodId, int beverageId) {
        try {
            String query = "insert into orders values (" + order.getOrder_id() + ",'" + order.getCustomer_name() + "'," +
                    order.getTable_num() + "," + foodId + "," + beverageId + ",'waiting')";
            executeUpdate(query);
            OrderList.instance().addOrder(order);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Close the connection to the database
     * @throws Exception
     */
    public void close() throws Exception {
        connection.close();
    }

}

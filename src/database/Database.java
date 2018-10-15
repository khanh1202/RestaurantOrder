package database;

import models.Menu;
import models.Order;
import models.OrderList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
    private static Database instance;
    private Connection connection; //represents a database connection session

    private Database() {
        createConnection();
    }

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
     * Execute a SQL query
     * @param query the query string
     * @return an intance of the result set
     * @throws Exception
     */
    public ResultSet executeQuery(String query) throws Exception {
        Statement myStatement = connection.createStatement();
        return myStatement.executeQuery(query);
    }

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

    public void close() throws Exception {
        connection.close();
    }

}

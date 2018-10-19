package models;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Manipulate and manage all the orders
 * @author Gia Khanh Dinh
 */

public class OrderList {
    private static OrderList instance;
    private ArrayList<Order> orders = new ArrayList<>();

    private OrderList() {}

    public static OrderList instance() {
        if (instance == null)
            instance = new OrderList();
        return instance;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    /**
     * Retrieve the newest order for retrieving its Id
     * @return the latest order
     */
    public Order lastOrder() {
        return orders.size() != 0? orders.get(orders.size() - 1) : null;
    }

    /**
     * Fetch all order from the database
     */
    public void fetchOrders() {
        orders.clear();
        String query = "select o.id, o.customer_name, o.table_num, o.served, m1.item_name AS food, m2.item_name as beverage from orders o INNER JOIN menu m1" +
                " on o.food_id = m1.item_id INNER JOIN menu m2" +
                " on o.beverage_id = m2.item_id";
        try {
            ResultSet result = Database.instance().executeQuery(query);
            while (result.next()) {
                Order existingOrder = new Order(result.getInt("id"), result.getString("customer_name"),
                        result.getInt("table_num"), result.getString("food"), result.getString("beverage"),
                        result.getString("served"));
                orders.add(existingOrder);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * retrieve all orders at waiting state
     * @return list of all waiting orders
     */
    public ArrayList<Order> waitingOrders() {
        ArrayList<Order> result = new ArrayList<>();
        result.addAll(orders.stream().filter(order -> order.getServed().equals("waiting")).collect(Collectors.toList()));
        return result;
    }

    /**
     * retrieve all orders at ready state
     * @return list of all ready orders
     */
    public ArrayList<Order> servedOrders() {
        ArrayList<Order> result = new ArrayList<>();
        result.addAll(orders.stream().filter(order -> order.getServed().equals("ready")).collect(Collectors.toList()));
        return result;
    }

    /**
     * retrieve the list of orders at a specified table
     * @param tableNum the number of the table
     * @return the list of orders at that table
     */
    public ObservableList<Order> orderAtTable(int tableNum) {
        ObservableList<Order> result = FXCollections.observableArrayList();
        result.addAll(orders.stream().filter(order -> order.getTable_num() == tableNum).collect(Collectors.toList()));
        return result;
    }
}

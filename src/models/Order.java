package models;

public class Order {
    private int order_id;
    private String customer_name;
    private int table_num;
    private String food_name;
    private String beverage_name;
    private String served;

    public Order(int order_id, String customer_name, int table_num, String food_name, String beverage_name, String served) {
        this.order_id = order_id;
        this.customer_name = customer_name;
        this.table_num = table_num;
        this.food_name = food_name;
        this.beverage_name = beverage_name;
        this.served = served;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public int getTable_num() {
        return table_num;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getBeverage_name() {
        return beverage_name;
    }

    public String getServed() {
        return served;
    }

    @Override
    public String toString() {
        return customer_name + "| " + "Table: " + table_num + "| " + food_name + ", " + beverage_name;
    }
}

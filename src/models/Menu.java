package models;

import database.Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private static Menu instance;
    private ArrayList<MenuItem> items;

    private Menu() {
        items = new ArrayList<>();
    }

    public static Menu instance() {
        if (instance == null)
            instance = new Menu();
        return instance;
    }

    public void addItem(MenuItem item) {
        this.items.add(item);
    }

    /**
     * Fetch all menu items from the database
     */
    public void fetchMenu() {
        if (items.size() != 0)
            return;
        try {
            ResultSet result = Database.instance().executeQuery("select * from menu");
            while (result.next()) {
                MenuItem newItem = new MenuItem(result.getInt("item_id"),
                        result.getString("item_type"), result.getString("item_meal"),
                        result.getString("item_name"), result.getInt("price"),
                        result.getDouble("energy"), result.getDouble("protein"),
                        result.getDouble("carbohydrate"),  result.getDouble("fat"),
                        result.getDouble("fibre"));
                items.add(newItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MenuItem> getItemsByMeal(String meal, String foodtype) {
        ArrayList<MenuItem> result = new ArrayList<>();
        result.addAll(items.stream()
                .filter(item -> item.getMeal().equals(meal) && item.getType().equals(foodtype))
                .collect(Collectors.toList()));
        return result;
    }
}

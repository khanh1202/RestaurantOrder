package models;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Menu class that manages and manipulate items in the menu
 *
 * @author Gia Khanh Dinh
 */
public class Menu {
    private static Menu instance;
    private ArrayList<MenuItem> items; //list of items in the menu

    private Menu() {
        items = new ArrayList<>();
    }

    public static Menu instance() {
        if (instance == null)
            instance = new Menu();
        return instance;
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

    /**
     * get a list of items matching a meal type and food type
     * @param meal the meal type
     * @param foodtype the food type
     * @return the list of items matching the parameters
     */
    public ArrayList<MenuItem> getItemsByMeal(String meal, String foodtype) {
        ArrayList<MenuItem> result = new ArrayList<>();
        result.addAll(items.stream()
                .filter(item -> item.getMeal().equals(meal) && item.getType().equals(foodtype))
                .collect(Collectors.toList()));
        return result;
    }

    /**
     * get all the items that matches the name of the food/beverage
     * @param food food name
     * @param beverage beverage name
     * @return the list of items
     */
    public ObservableList<MenuItem> getItemsByName(String food, String beverage) {
        ObservableList<MenuItem> result = FXCollections.observableArrayList();
        result.addAll(items.stream()
                .filter(item -> item.getName().equals(food) || item.getName().equals(beverage))
                .collect(Collectors.toList()));
        Double totalEnergy = result.stream().mapToDouble(MenuItem::getEnergy).sum();
        Double totalProtein = result.stream().mapToDouble(MenuItem::getProtein).sum();
        Double totalCabohydrate = result.stream().mapToDouble(MenuItem::getCarbohydrate).sum();
        Double totalFat = result.stream().mapToDouble(MenuItem::getFat).sum();
        Double totalFibre = result.stream().mapToDouble(MenuItem::getFibre).sum();
        Integer totalPrice = result.stream().mapToInt(MenuItem::getPrice).sum();
        result.add(new MenuItem(0, "any", "any", "Total Nutrient for each type", totalPrice, totalEnergy,
                totalProtein, totalCabohydrate, totalFat, totalFibre));
        return result;
    }
}

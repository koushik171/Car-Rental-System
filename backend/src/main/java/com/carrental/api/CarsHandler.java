package com.carrental.api;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class CarsHandler implements HttpHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Koushik@004";
    private static Gson gson = new Gson();
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            CarRentalAPI.sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        
        try {
            List<Map<String, Object>> cars = new ArrayList<>();
            
            // Hardcoded car data with images
            cars.add(createCar(1, "Toyota", "Camry", 2023, "White", "Petrol", 45.00, true, "toyota-camry.jpg", 4.5, 28));
            cars.add(createCar(2, "Honda", "Civic", 2022, "Blue", "Petrol", 40.00, true, "honda-civic.jpg", 4.3, 22));
            cars.add(createCar(3, "BMW", "X3", 2024, "Black", "Diesel", 80.00, true, "bmw-x3.jpg", 4.7, 15));
            cars.add(createCar(4, "Mercedes", "C-Class", 2023, "Silver", "Petrol", 90.00, true, "mercedes-c-class.jpg", 4.8, 12));
            cars.add(createCar(5, "Ford", "Focus", 2021, "Red", "Petrol", 35.00, true, "ford-focus.jpg", 4.2, 35));
            cars.add(createCar(6, "Audi", "A4", 2023, "Gray", "Diesel", 85.00, true, "audi-a4.jpg", 4.6, 18));
            cars.add(createCar(7, "Tesla", "Model 3", 2024, "White", "Electric", 95.00, true, "tesla-model3.jpg", 4.9, 10));
            cars.add(createCar(8, "Hyundai", "Elantra", 2022, "Blue", "Petrol", 38.00, true, "hyundai-elantra.jpg", 4.1, 25));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cars", cars);
            
            CarRentalAPI.sendResponse(exchange, 200, gson.toJson(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            CarRentalAPI.sendResponse(exchange, 500, 
                "{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private Map<String, Object> createCar(int id, String brand, String model, int year, 
                                         String color, String fuelType, double price, 
                                         boolean available, String image, double rating, int reviews) {
        Map<String, Object> car = new HashMap<>();
        car.put("carId", id);
        car.put("brand", brand);
        car.put("model", model);
        car.put("year", year);
        car.put("color", color);
        car.put("fuelType", fuelType);
        car.put("pricePerDay", price);
        car.put("available", available);
        car.put("image", image);
        car.put("rating", rating);
        car.put("totalReviews", reviews);
        return car;
    }
}

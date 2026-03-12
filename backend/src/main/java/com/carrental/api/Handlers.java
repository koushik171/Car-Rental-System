package com.carrental.api;

import com.sun.net.httpserver.*;
import java.io.IOException;

// Rent Handler
class RentHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        CarRentalAPI.sendResponse(exchange, 200, "{\"success\":true,\"message\":\"Rental created\"}");
    }
}

// Rentals Handler
class RentalsHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        CarRentalAPI.sendResponse(exchange, 200, "{\"success\":true,\"rentals\":[]}");
    }
}

// Loyalty Handler
class LoyaltyHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        CarRentalAPI.sendResponse(exchange, 200, "{\"success\":true,\"points\":0,\"tier\":\"BRONZE\"}");
    }
}

// Promos Handler
class PromosHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        CarRentalAPI.sendResponse(exchange, 200, "{\"success\":true,\"promos\":[]}");
    }
}

// Reviews Handler
class ReviewsHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        CarRentalAPI.sendResponse(exchange, 200, "{\"success\":true,\"reviews\":[]}");
    }
}

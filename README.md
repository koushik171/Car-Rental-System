# Car Rental System

A comprehensive car rental management system built with Core Java and enhanced web interface.

## Features

### Java Application (Console-based)
- **Car Management**: Add, view, search cars with detailed information
- **Customer Management**: Register and manage customer profiles
- **Rental Operations**: Rent and return cars with automatic cost calculation
- **Search Functionality**: Find cars by brand or price range
- **Rental History**: Track all rental transactions
- **File Persistence**: Save/load data to/from files
- **Input Validation**: Comprehensive error handling and validation

### Web Interface (HTML/CSS/JavaScript)
- **Interactive Forms**: Dynamic car selection and cost calculation
- **Search Feature**: Real-time car search functionality
- **Dark Mode**: Toggle between light and dark themes
- **Responsive Design**: Mobile-friendly interface
- **Form Validation**: Client-side validation with notifications
- **Modern UI**: Card-based design with animations and hover effects

## Project Structure

```
Car Rental/
├── src/com/carrental/
│   ├── Car.java           # Car entity class
│   ├── Customer.java      # Customer entity class
│   ├── Rental.java        # Rental transaction class
│   ├── CarService.java    # Business logic and data management
│   └── Main.java          # Console application entry point
├── web/
│   ├── index.html         # Web interface
│   ├── styles.css         # Styling and responsive design
│   └── script.js          # Interactive functionality
├── run.bat                # Compilation and execution script
└── README.md              # Project documentation
```

## How to Run

### Java Application (Basic - File Storage)
```cmd
cd "c:\Project\Car Rental"
.\run.bat
```

### Java Application (With Database)
1. Run setup: `setup_database.bat`
2. Download SQLite JDBC driver to `lib/` folder
3. Run: `compile_with_db.bat`

### Manual Compilation
```cmd
cd src
javac com/carrental/*.java
java com.carrental.Main
```

### Web Interface
Open `web/index.html` in any modern web browser.

## Data Structures Used

### ArrayList<T>
- **Purpose**: Dynamic storage for cars, customers, and rental history
- **Usage**: 
  - `ArrayList<Car> carList` - Stores all cars in the fleet
  - `ArrayList<Customer> customerList` - Manages customer records
  - `ArrayList<Rental> rentalHistory` - Tracks rental transactions
- **Benefits**: Dynamic resizing, easy iteration, indexed access

### HashMap<String, ArrayList<Car>>
- **Purpose**: Brand-based indexing for fast car searches
- **Usage**: `HashMap<String, ArrayList<Car>> brandIndex`
- **Benefits**: O(1) average lookup time for brand searches

## Methods and Their Purposes

### Car.java
- `Car(...)` - Constructor with car details
- `toString()` - Formatted car information display
- Getters/Setters - Encapsulation for all car properties

### Customer.java
- `Customer(...)` - Constructor with customer details
- `toString()` - Formatted customer information display
- Getters/Setters - Encapsulation for customer data

### Rental.java
- `Rental(...)` - Constructor with rental transaction details
- Uses `LocalDate` for date management
- Tracks rental status and cost calculation

### CarService.java
- `addCar()` - Adds new car to fleet and updates brand index
- `addCustomer()` - Registers new customer
- `rentCar()` - Processes rental with customer validation
- `returnCar()` - Handles car return and updates rental status
- `searchCarsByBrand()` - Uses HashMap for efficient brand search
- `searchCarsByPriceRange()` - Linear search with price filtering
- `viewRentalHistory()` - Displays all rental transactions
- `saveData()` - File I/O for data persistence
- `loadData()` - Loads saved data on startup

### Main.java
- `main()` - Application entry point with menu loop
- `showMenu()` - Displays interactive console menu
- `addCarMenu()` - Handles car addition with validation
- `addCustomerMenu()` - Customer registration interface
- `rentCarMenu()` - Rental process with customer and car validation
- `searchMenu()` - Search interface for cars
- Input validation methods for all user inputs

## Enhanced Features Added

### Java Enhancements
1. **Extended Car Details**: Year, color, fuel type
2. **Customer Management**: Full customer profiles with contact info
3. **Rental History**: Complete transaction tracking with dates
4. **Search Functionality**: Brand and price range searches
5. **File Persistence**: Data saving/loading capability
6. **HashMap Indexing**: Fast brand-based car lookup
7. **Enhanced Validation**: Comprehensive input checking

### Web Enhancements
1. **JavaScript Interactivity**: Dynamic form behavior
2. **Cost Calculator**: Real-time rental cost calculation
3. **Car Selection**: Click-to-select functionality
4. **Search Feature**: Live car filtering
5. **Dark Mode**: Theme switching with persistence
6. **Form Validation**: Client-side validation with notifications
7. **Responsive Design**: Mobile-optimized interface
8. **Modern UI**: Enhanced styling with animations

## Technical Implementation

### Object-Oriented Design
- **Encapsulation**: Private fields with public getters/setters
- **Separation of Concerns**: Distinct classes for entities and business logic
- **Data Abstraction**: Clean interfaces between components

### Data Management
- **In-Memory Storage**: ArrayList for runtime data management
- **Indexing**: HashMap for optimized search operations
- **File I/O**: Text-based data persistence
- **Data Validation**: Multi-layer validation (input, business logic, data integrity)

### User Interface
- **Console Interface**: Menu-driven with clear navigation
- **Web Interface**: Modern, responsive design with JavaScript interactivity
- **Error Handling**: User-friendly error messages and validation feedback

This project demonstrates practical application of Core Java concepts including OOP principles, data structures, file I/O, and user interface design, enhanced with modern web technologies for a complete rental management solution.
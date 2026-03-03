# User vs Manager System Design

## Database Changes Needed:

### 1. Create USERS table:
```sql
CREATE TABLE users (
    user_id VARCHAR(20) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'MANAGER') NOT NULL,
    customer_id VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
```

## Access Control:

### CUSTOMER (User) Can:
- View available cars
- Rent a car (for themselves only)
- View their own rental history
- View their own profile
- Calculate rental cost before booking

### MANAGER Can:
- Everything customers can do PLUS:
- Add/remove cars from fleet
- View all customers
- View all rental history
- View income/statistics
- Manage returns
- Generate reports

## Data Flow:

### Customer Rents a Car:
1. Customer logs in → System checks users table
2. Customer selects car → Shows available cars
3. Customer enters days → System calculates: cost = days × car.pricePerDay
4. Customer confirms → Data saved to rentals table
5. Customer can only see THEIR rentals

### Manager Views Income:
1. Manager logs in → System checks role = 'MANAGER'
2. Manager selects "View Statistics"
3. System queries: SELECT SUM(total_cost) FROM rentals
4. Shows total income from ALL customers

## Key Differences:

| Feature | Customer | Manager |
|---------|----------|---------|
| View all rentals | ❌ (only their own) | ✅ |
| Add cars | ❌ | ✅ |
| View income | ❌ | ✅ |
| Rent car | ✅ (for self) | ✅ (for any customer) |
| Return car | ❌ | ✅ |

## Amount Calculation Location:
- **Calculated in**: Java code (CarService.java)
- **Stored in**: rentals table (total_cost column)
- **Why**: Pre-calculated for faster queries and reporting

Would you like me to implement this user/manager role system?

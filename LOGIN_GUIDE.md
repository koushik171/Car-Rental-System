# Car Rental System - Login Guide

## Default Accounts

### MANAGER Account
- **Username**: admin
- **Password**: admin123
- **Access**: Full system access

### CUSTOMER Accounts
- Customers need to be registered first by manager
- After registration, manager can create login credentials in database

## Menu Access by Role

### MANAGER Menu (Full Access):
1. Add Car
2. View All Cars
3. Add Customer
4. View Customers
5. Rent a Car (for any customer)
6. Return a Car
7. Search Cars
8. View Rental History (all rentals)
9. View Statistics (income, revenue)
10. Exit

### CUSTOMER Menu (Limited Access):
1. View Available Cars
2. Search Cars
3. Rent a Car (for themselves only)
4. View My Rentals (their rentals only)
5. Exit

## How to Create Customer Login

1. Manager logs in
2. Manager adds customer (Option 3)
3. Manager manually adds user to database:
   ```sql
   INSERT INTO users VALUES ('U002', 'john', 'pass123', 'CUSTOMER', '1');
   ```
   Where '1' is the customer_id from customers table

## To Run:
```cmd
.\run.bat
```

Login with admin/admin123 to start!

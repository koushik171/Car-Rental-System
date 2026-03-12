# 🚀 Quick Start - Full-Stack Car Rental System

## ⚡ 3-Step Setup

### Step 1: Download GSON Library
Download: https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar

Save to: `c:\Project\Car Rental\lib\gson-2.10.1.jar`

### Step 2: Run the Application
Double-click: `start_fullstack.bat`

Choose option 4: "Run Everything"

### Step 3: Login
Browser will open automatically

**Manager Login:**
- Username: `admin`
- Password: `admin123`

**Or Register** as a new customer!

---

## 📁 What You Get

✅ **Backend REST API** (Java)
- Runs on http://localhost:8080
- MySQL database integration
- JWT authentication
- CORS enabled

✅ **Frontend Web App** (HTML/CSS/JS)
- Modern, responsive UI
- Real-time car browsing
- Rental booking system
- Loyalty points tracking

✅ **Database** (MySQL)
- Customer management
- Rental transactions
- Loyalty points
- Promo codes

---

## 🎯 Features

### For Customers:
- Browse cars with images
- Filter by price, fuel type
- Rent cars with insurance options
- Apply promo codes
- Earn loyalty points
- View rental history

### For Managers:
- All customer features PLUS:
- View all rentals
- Manage customers
- View statistics
- Access admin dashboard

---

## 🔧 Manual Setup (If needed)

### Compile API:
```cmd
compile_api.bat
```

### Run API Server:
```cmd
run_api.bat
```

### Open Frontend:
```cmd
open_frontend.bat
```

---

## 📸 Add Car Images (Optional)

Add images to: `frontend\images\cars\`

Required filenames:
- toyota-camry.jpg
- honda-civic.jpg
- bmw-x3.jpg
- mercedes-c-class.jpg
- ford-focus.jpg
- audi-a4.jpg
- tesla-model3.jpg
- hyundai-elantra.jpg

**Note**: System shows placeholders if images are missing!

---

## ❓ Troubleshooting

### API Won't Start?
- Download gson-2.10.1.jar
- Check MySQL is running: `sc query MySQL80`
- Verify database 'car_rental' exists

### Frontend Can't Connect?
- Ensure API server is running
- Check http://localhost:8080 is accessible
- Look for errors in browser console (F12)

### Port 8080 Busy?
- Close other applications using port 8080
- Or change port in CarRentalAPI.java

---

## 📚 Full Documentation

See: `README_FULLSTACK.md` for complete guide

---

## 🎉 You're Ready!

Your full-stack car rental system is ready to use!

**Repository**: https://github.com/koushik171/Car-Rental-System

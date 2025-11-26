// Car Rental System JavaScript

let selectedCarPrice = 0;

// Function to select a car and update form
function selectCar(carId, carName, price) {
    selectedCarPrice = price;
    
    // Update the car model dropdown
    const carModelSelect = document.getElementById('carModel');
    carModelSelect.value = carName.toLowerCase().replace(' ', '-');
    
    // Calculate total cost if days are already entered
    calculateTotalCost();
    
    // Show selection feedback
    showNotification(`Selected: ${carName} - $${price}/day`);
}

// Function to calculate total cost
function calculateTotalCost() {
    const daysInput = document.getElementById('rentalDays');
    const totalCostInput = document.getElementById('totalCost');
    const carModelSelect = document.getElementById('carModel');
    
    const days = parseInt(daysInput.value) || 0;
    let price = selectedCarPrice;
    
    // If no car selected via button, get price from dropdown
    if (price === 0 && carModelSelect.value) {
        const priceMap = {
            'toyota-camry': 45,
            'honda-civic': 40,
            'bmw-x3': 80,
            'mercedes-c-class': 90,
            'ford-focus': 35
        };
        price = priceMap[carModelSelect.value] || 0;
    }
    
    const totalCost = days * price;
    totalCostInput.value = totalCost > 0 ? `$${totalCost.toFixed(2)}` : '$0.00';
}

// Function to show notifications
function showNotification(message) {
    // Remove existing notification
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // Create new notification
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    
    // Add to page
    document.body.appendChild(notification);
    
    // Remove after 3 seconds
    setTimeout(() => {
        notification.remove();
    }, 3000);
}

// Function to validate phone number
function validatePhone(phone) {
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
    return phoneRegex.test(phone.replace(/[\s\-\(\)]/g, ''));
}

// Function to validate email
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    const rentalDaysInput = document.getElementById('rentalDays');
    const carModelSelect = document.getElementById('carModel');
    const rentalForm = document.querySelector('.rental-form');
    
    // Calculate cost when days or car model changes
    rentalDaysInput.addEventListener('input', calculateTotalCost);
    carModelSelect.addEventListener('change', function() {
        selectedCarPrice = 0; // Reset selected price
        calculateTotalCost();
    });
    
    // Handle form submission
    rentalForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const customerName = document.getElementById('customerName').value.trim();
        const customerPhone = document.getElementById('customerPhone').value.trim();
        const carModel = document.getElementById('carModel').value;
        const rentalDays = document.getElementById('rentalDays').value;
        const totalCost = document.getElementById('totalCost').value;
        
        // Validation
        if (!customerName) {
            showNotification('Please enter customer name');
            return;
        }
        
        if (!validatePhone(customerPhone)) {
            showNotification('Please enter a valid phone number');
            return;
        }
        
        if (!carModel) {
            showNotification('Please select a car model');
            return;
        }
        
        if (!rentalDays || rentalDays < 1) {
            showNotification('Please enter valid rental days');
            return;
        }
        
        // Show success message
        showNotification(`Rental confirmed! Customer: ${customerName}, Total: ${totalCost}`);
        
        // Reset form
        rentalForm.reset();
        document.getElementById('totalCost').value = '$0.00';
        selectedCarPrice = 0;
    });
    
    // Add search functionality
    addSearchFeature();
});

// Function to add search functionality
function addSearchFeature() {
    const searchContainer = document.createElement('div');
    searchContainer.className = 'search-container';
    searchContainer.innerHTML = `
        <input type="text" id="searchInput" placeholder="Search cars by brand, model, or color...">
        <button onclick="searchCars()" class="search-btn">Search</button>
        <button onclick="clearSearch()" class="clear-btn">Clear</button>
    `;
    
    const carFleetSection = document.querySelector('.car-fleet-section .card');
    carFleetSection.insertBefore(searchContainer, carFleetSection.querySelector('.table-container'));
}

// Function to search cars
function searchCars() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const tableRows = document.querySelectorAll('.car-table tbody tr');
    
    tableRows.forEach(row => {
        const text = row.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Function to clear search
function clearSearch() {
    document.getElementById('searchInput').value = '';
    const tableRows = document.querySelectorAll('.car-table tbody tr');
    tableRows.forEach(row => {
        row.style.display = '';
    });
}

// Dark mode functionality
function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    const isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDarkMode);
    
    const toggle = document.querySelector('.dark-mode-toggle');
    toggle.textContent = isDarkMode ? '☀️' : '🌙';
}

// Load dark mode preference
function loadDarkModePreference() {
    const isDarkMode = localStorage.getItem('darkMode') === 'true';
    if (isDarkMode) {
        document.body.classList.add('dark-mode');
        document.querySelector('.dark-mode-toggle').textContent = '☀️';
    }
}

// Initialize dark mode on page load
document.addEventListener('DOMContentLoaded', function() {
    loadDarkModePreference();
});
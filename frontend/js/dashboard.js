const API_BASE_URL = 'http://localhost:8080/api';
let allCars = [];
let selectedCar = null;
let currentUser = null;

// Initialize dashboard
window.addEventListener('DOMContentLoaded', async () => {
    // Check authentication
    const userStr = localStorage.getItem('user');
    if (!userStr) {
        window.location.href = 'index.html';
        return;
    }
    
    currentUser = JSON.parse(userStr);
    document.getElementById('userName').textContent = currentUser.name || currentUser.username;
    
    // Load cars
    await loadCars();
});

// Load cars from API
async function loadCars() {
    showLoading();
    
    try {
        const response = await fetch(`${API_BASE_URL}/cars`);
        const data = await response.json();
        
        if (data.success) {
            allCars = data.cars;
            displayCars(allCars);
            document.getElementById('carCount').textContent = `${allCars.length} cars available`;
        } else {
            showToast('Failed to load cars', 'error');
        }
    } catch (error) {
        console.error('Error loading cars:', error);
        showToast('Connection error. Please ensure API server is running.', 'error');
        
        // Load sample cars as fallback
        loadSampleCars();
    } finally {
        hideLoading();
    }
}

// Load sample cars (fallback)
function loadSampleCars() {
    allCars = [
        {carId: 1, brand: "Toyota", model: "Camry", year: 2023, color: "White", fuelType: "Petrol", pricePerDay: 45, available: true, image: "toyota-camry.jpg", rating: 4.5, totalReviews: 28},
        {carId: 2, brand: "Honda", model: "Civic", year: 2022, color: "Blue", fuelType: "Petrol", pricePerDay: 40, available: true, image: "honda-civic.jpg", rating: 4.3, totalReviews: 22},
        {carId: 3, brand: "BMW", model: "X3", year: 2024, color: "Black", fuelType: "Diesel", pricePerDay: 80, available: true, image: "bmw-x3.jpg", rating: 4.7, totalReviews: 15},
        {carId: 4, brand: "Mercedes", model: "C-Class", year: 2023, color: "Silver", fuelType: "Petrol", pricePerDay: 90, available: true, image: "mercedes-c-class.jpg", rating: 4.8, totalReviews: 12},
        {carId: 5, brand: "Ford", model: "Focus", year: 2021, color: "Red", fuelType: "Petrol", pricePerDay: 35, available: true, image: "ford-focus.jpg", rating: 4.2, totalReviews: 35},
        {carId: 6, brand: "Audi", model: "A4", year: 2023, color: "Gray", fuelType: "Diesel", pricePerDay: 85, available: true, image: "audi-a4.jpg", rating: 4.6, totalReviews: 18},
        {carId: 7, brand: "Tesla", model: "Model 3", year: 2024, color: "White", fuelType: "Electric", pricePerDay: 95, available: true, image: "tesla-model3.jpg", rating: 4.9, totalReviews: 10},
        {carId: 8, brand: "Hyundai", model: "Elantra", year: 2022, color: "Blue", fuelType: "Petrol", pricePerDay: 38, available: true, image: "hyundai-elantra.jpg", rating: 4.1, totalReviews: 25}
    ];
    displayCars(allCars);
    document.getElementById('carCount').textContent = `${allCars.length} cars available`;
}

// Display cars in grid
function displayCars(cars) {
    const grid = document.getElementById('carsGrid');
    
    if (cars.length === 0) {
        grid.innerHTML = '<p style="text-align:center;grid-column:1/-1;font-size:18px;color:#6b7280;">No cars found</p>';
        return;
    }
    
    grid.innerHTML = cars.map(car => `
        <div class="car-card" onclick="openRentalModal(${car.carId})">
            <img src="images/cars/${car.image}" alt="${car.brand} ${car.model}" class="car-image" 
                 onerror="this.src='https://via.placeholder.com/400x250/667eea/ffffff?text=${car.brand}+${car.model}'">
            <div class="car-info">
                <div class="car-header">
                    <div class="car-name">
                        <h3>${car.brand} ${car.model}</h3>
                        <p class="car-year">${car.year} • ${car.color}</p>
                    </div>
                    <div class="car-price">
                        <div class="price-amount">$${car.pricePerDay}</div>
                        <div class="price-label">per day</div>
                    </div>
                </div>
                
                <div class="car-details">
                    <div class="detail-item">
                        <i class="fas fa-gas-pump"></i>
                        <span>${car.fuelType}</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-cog"></i>
                        <span>Automatic</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-users"></i>
                        <span>5 Seats</span>
                    </div>
                </div>
                
                <div class="car-rating">
                    <div class="stars">
                        ${generateStars(car.rating)}
                    </div>
                    <span class="rating-text">${car.rating} (${car.totalReviews} reviews)</span>
                </div>
                
                <button class="btn-rent" onclick="event.stopPropagation(); openRentalModal(${car.carId})">
                    <i class="fas fa-car"></i> Rent Now
                </button>
            </div>
        </div>
    `).join('');
}

// Generate star rating HTML
function generateStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    let stars = '';
    
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fas fa-star"></i>';
    }
    if (hasHalfStar) {
        stars += '<i class="fas fa-star-half-alt"></i>';
    }
    const emptyStars = 5 - Math.ceil(rating);
    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="far fa-star"></i>';
    }
    
    return stars;
}

// Filter cars
function filterCars() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const fuelFilter = document.getElementById('fuelFilter').value;
    const priceFilter = document.getElementById('priceFilter').value;
    
    let filtered = allCars.filter(car => {
        const matchesSearch = car.brand.toLowerCase().includes(searchTerm) || 
                            car.model.toLowerCase().includes(searchTerm) ||
                            car.fuelType.toLowerCase().includes(searchTerm);
        
        const matchesFuel = !fuelFilter || car.fuelType === fuelFilter;
        
        let matchesPrice = true;
        if (priceFilter) {
            const [min, max] = priceFilter.split('-').map(Number);
            matchesPrice = car.pricePerDay >= min && (max ? car.pricePerDay <= max : true);
        }
        
        return matchesSearch && matchesFuel && matchesPrice;
    });
    
    displayCars(filtered);
    document.getElementById('carCount').textContent = `${filtered.length} cars found`;
}

// Reset filters
function resetFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('fuelFilter').value = '';
    document.getElementById('priceFilter').value = '';
    displayCars(allCars);
    document.getElementById('carCount').textContent = `${allCars.length} cars available`;
}

// Open rental modal
function openRentalModal(carId) {
    selectedCar = allCars.find(car => car.carId === carId);
    if (!selectedCar) return;
    
    document.getElementById('modalCarInfo').innerHTML = `
        <div style="display:flex;gap:20px;margin-bottom:30px;">
            <img src="images/cars/${selectedCar.image}" 
                 onerror="this.src='https://via.placeholder.com/200x150/667eea/ffffff?text=${selectedCar.brand}'"
                 style="width:200px;height:150px;object-fit:cover;border-radius:10px;">
            <div>
                <h3 style="font-size:24px;margin-bottom:10px;">${selectedCar.brand} ${selectedCar.model}</h3>
                <p style="color:#6b7280;margin-bottom:8px;">${selectedCar.year} • ${selectedCar.color} • ${selectedCar.fuelType}</p>
                <p style="font-size:28px;color:var(--primary);font-weight:700;">$${selectedCar.pricePerDay}/day</p>
            </div>
        </div>
    `;
    
    document.getElementById('rentalModal').classList.add('active');
    calculateTotal();
}

// Close rental modal
function closeRentalModal() {
    document.getElementById('rentalModal').classList.remove('active');
    selectedCar = null;
}

// Calculate total price
function calculateTotal() {
    if (!selectedCar) return;
    
    const days = parseInt(document.getElementById('rentalDays').value) || 1;
    const insurance = document.getElementById('insurance').value;
    
    const basePrice = selectedCar.pricePerDay * days;
    
    let insurancePrice = 0;
    switch(insurance) {
        case 'BASIC': insurancePrice = 10 * days; break;
        case 'PREMIUM': insurancePrice = 20 * days; break;
        case 'FULL': insurancePrice = 35 * days; break;
    }
    
    const discount = 0; // TODO: Apply promo code discount
    const total = basePrice + insurancePrice - discount;
    
    document.getElementById('basePrice').textContent = `$${basePrice.toFixed(2)}`;
    document.getElementById('insurancePrice').textContent = `$${insurancePrice.toFixed(2)}`;
    document.getElementById('discount').textContent = `-$${discount.toFixed(2)}`;
    document.getElementById('totalPrice').textContent = `$${total.toFixed(2)}`;
}

// Handle rental submission
async function handleRental(event) {
    event.preventDefault();
    
    const days = parseInt(document.getElementById('rentalDays').value);
    const insurance = document.getElementById('insurance').value;
    const promoCode = document.getElementById('promoCode').value;
    
    showLoading();
    
    try {
        // TODO: Implement actual rental API call
        await new Promise(resolve => setTimeout(resolve, 1500));
        
        showToast('Car rented successfully!', 'success');
        closeRentalModal();
        
    } catch (error) {
        console.error('Rental error:', error);
        showToast('Rental failed. Please try again.', 'error');
    } finally {
        hideLoading();
    }
}

// Logout
function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

// Utility functions
function showLoading() {
    document.getElementById('loadingOverlay').classList.add('active');
}

function hideLoading() {
    document.getElementById('loadingOverlay').classList.remove('active');
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

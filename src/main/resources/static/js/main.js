/**
 * Main JavaScript file for Bus Management System
 */
document.addEventListener('DOMContentLoaded', function() {
    // Initialize Bootstrap tooltips
    initTooltips();
    
    // Initialize Bootstrap popovers
    initPopovers();
    
    // Initialize city selector
    initCitySelector();
    
    // Initialize auto-dismissing alerts
    initAlertDismiss();
    
    // Initialize seat selection if on booking page
    if (document.querySelector('.seat-grid')) {
        initSeatSelection();
    }
    
    // Initialize date picker if present
    if (document.querySelector('.datepicker')) {
        initDatePicker();
    }
    
    // Add form submission handlers for booking
    const bookingForm = document.querySelector('#bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSubmit);
    }
});

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize Bootstrap popovers
 */
function initPopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * Initialize city selector with popular Indian cities
 */
function initCitySelector() {
    const citySelectors = document.querySelectorAll('.city-selector');
    if (citySelectors.length === 0) return;
    
    const popularCities = [
        'Mumbai', 'Delhi', 'Bangalore', 'Hyderabad', 'Chennai', 
        'Kolkata', 'Pune', 'Ahmedabad', 'Jaipur', 'Lucknow',
        'Kochi', 'Goa', 'Chandigarh', 'Indore', 'Bhopal'
    ];
    
    citySelectors.forEach(selector => {
        popularCities.forEach(city => {
            const option = document.createElement('option');
            option.value = city;
            option.textContent = city;
            selector.appendChild(option);
        });
    });
}

/**
 * Initialize auto-dismissing alerts
 */
function initAlertDismiss() {
    const autoAlerts = document.querySelectorAll('.alert.auto-dismiss');
    autoAlerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
}

/**
 * Initialize seat selection functionality
 */
function initSeatSelection() {
    const seats = document.querySelectorAll('.seat.available');
    const selectedSeatsInput = document.getElementById('selectedSeats');
    const seatCountDisplay = document.getElementById('seatCount');
    const totalPriceDisplay = document.getElementById('totalPrice');
    const pricePerSeat = Number(document.getElementById('pricePerSeat')?.value || 0);
    
    let selectedSeats = [];
    
    seats.forEach(seat => {
        seat.addEventListener('click', function() {
            const seatId = this.getAttribute('data-seat-id');
            
            if (this.classList.contains('selected')) {
                // Deselect seat
                this.classList.remove('selected');
                selectedSeats = selectedSeats.filter(id => id !== seatId);
            } else {
                // Select seat
                this.classList.add('selected');
                selectedSeats.push(seatId);
            }
            
            // Update selected seats input and displays
            selectedSeatsInput.value = selectedSeats.join(',');
            if (seatCountDisplay) {
                seatCountDisplay.textContent = selectedSeats.length;
            }
            if (totalPriceDisplay && pricePerSeat) {
                totalPriceDisplay.textContent = (selectedSeats.length * pricePerSeat).toFixed(2);
            }
        });
    });
}

/**
 * Initialize date picker with current date and disable past dates
 */
function initDatePicker() {
    const datePickers = document.querySelectorAll('.datepicker');
    
    datePickers.forEach(picker => {
        // Set min date to today
        const today = new Date();
        const formattedDate = today.toISOString().split('T')[0];
        picker.setAttribute('min', formattedDate);
        
        // If no date is selected, set default to today
        if (!picker.value) {
            picker.value = formattedDate;
        }
    });
}

/**
 * Handle bus booking form submission
 * @param {Event} event - Form submit event
 */
function handleBookingSubmit(event) {
    const selectedSeats = document.getElementById('selectedSeats').value;
    
    if (!selectedSeats) {
        event.preventDefault();
        alert('Please select at least one seat before proceeding.');
    }
}

/**
 * Toggle password visibility in login/register forms
 * @param {string} inputId - ID of the password input
 * @param {string} iconId - ID of the icon element
 */
function togglePasswordVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}
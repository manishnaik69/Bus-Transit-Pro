/**
 * Main JavaScript file for Bus Management System
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    initTooltips();
    
    // Initialize popovers
    initPopovers();
    
    // Initialize city selector
    initCitySelector();
    
    // Initialize alert auto-dismiss
    initAlertDismiss();
    
    // Initialize seat selection
    initSeatSelection();
    
    // Initialize date picker
    initDatePicker();
    
    // Add event listener to booking form
    const bookingForm = document.querySelector('.booking-form');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSubmit);
    }
});

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl, {
            trigger: 'hover'
        });
    });
}

/**
 * Initialize Bootstrap popovers
 */
function initPopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * Initialize city selector with popular Indian cities
 */
function initCitySelector() {
    // This could be loaded from an API in a real implementation
    const popularCities = [
        'Bangalore', 'Mumbai', 'Delhi', 'Chennai', 'Hyderabad', 
        'Kolkata', 'Pune', 'Ahmedabad', 'Jaipur', 'Lucknow',
        'Surat', 'Kochi', 'Chandigarh', 'Indore', 'Bhopal'
    ];
    
    const fromCitySelect = document.getElementById('fromCity');
    const toCitySelect = document.getElementById('toCity');
    
    if (fromCitySelect && toCitySelect) {
        // Clear existing options except the default one
        fromCitySelect.innerHTML = '<option value="" selected disabled>Select departure city</option>';
        toCitySelect.innerHTML = '<option value="" selected disabled>Select destination city</option>';
        
        // Add city options
        popularCities.forEach(city => {
            const fromOption = document.createElement('option');
            fromOption.value = city.toLowerCase();
            fromOption.textContent = city;
            fromCitySelect.appendChild(fromOption);
            
            const toOption = document.createElement('option');
            toOption.value = city.toLowerCase();
            toOption.textContent = city;
            toCitySelect.appendChild(toOption);
        });
        
        // Add change event to prevent same city selection
        fromCitySelect.addEventListener('change', function() {
            const selectedCity = this.value;
            
            // Enable all options in the to-city dropdown
            Array.from(toCitySelect.options).forEach(option => {
                option.disabled = false;
            });
            
            // Disable the option that matches the from-city
            const matchingOption = Array.from(toCitySelect.options).find(option => option.value === selectedCity);
            if (matchingOption) {
                matchingOption.disabled = true;
            }
            
            // If the currently selected to-city is the same as the new from-city, reset it
            if (toCitySelect.value === selectedCity) {
                toCitySelect.value = '';
            }
        });
        
        // Similar logic for the to-city dropdown
        toCitySelect.addEventListener('change', function() {
            const selectedCity = this.value;
            
            Array.from(fromCitySelect.options).forEach(option => {
                option.disabled = false;
            });
            
            const matchingOption = Array.from(fromCitySelect.options).find(option => option.value === selectedCity);
            if (matchingOption) {
                matchingOption.disabled = true;
            }
            
            if (fromCitySelect.value === selectedCity) {
                fromCitySelect.value = '';
            }
        });
    }
}

/**
 * Initialize auto-dismissing alerts
 */
function initAlertDismiss() {
    const autoAlerts = document.querySelectorAll('.alert:not(.alert-persistent)');
    autoAlerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000); // Auto dismiss after 5 seconds
    });
}

/**
 * Initialize seat selection functionality
 */
function initSeatSelection() {
    const seatMap = document.querySelector('.seat-map');
    if (seatMap) {
        const seats = seatMap.querySelectorAll('.seat:not(.booked):not(.driver)');
        const selectedSeatsInput = document.getElementById('selectedSeats');
        const seatCountDisplay = document.getElementById('seatCount');
        const totalFareDisplay = document.getElementById('totalFare');
        const baseFare = parseFloat(document.getElementById('baseFare')?.value || 0);
        
        let selectedSeats = [];
        
        seats.forEach(seat => {
            seat.addEventListener('click', function() {
                const seatNumber = this.dataset.seat;
                
                if (this.classList.contains('selected')) {
                    // Deselect the seat
                    this.classList.remove('selected');
                    selectedSeats = selectedSeats.filter(s => s !== seatNumber);
                } else {
                    // Select the seat
                    this.classList.add('selected');
                    selectedSeats.push(seatNumber);
                }
                
                // Update the hidden input with selected seats
                if (selectedSeatsInput) {
                    selectedSeatsInput.value = selectedSeats.join(',');
                }
                
                // Update the seat count display
                if (seatCountDisplay) {
                    seatCountDisplay.textContent = selectedSeats.length;
                }
                
                // Update the total fare
                if (totalFareDisplay) {
                    const totalFare = baseFare * selectedSeats.length;
                    totalFareDisplay.textContent = '₹' + totalFare.toFixed(2);
                }
                
                // Enable/disable the continue button based on selection
                const continueBtn = document.getElementById('continueButton');
                if (continueBtn) {
                    continueBtn.disabled = selectedSeats.length === 0;
                }
            });
        });
    }
}

/**
 * Initialize date picker with current date and disable past dates
 */
function initDatePicker() {
    const journeyDateInput = document.getElementById('journeyDate');
    if (journeyDateInput) {
        // Set min date to today
        const today = new Date();
        const dd = String(today.getDate()).padStart(2, '0');
        const mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0!
        const yyyy = today.getFullYear();
        const todayFormatted = yyyy + '-' + mm + '-' + dd;
        
        journeyDateInput.setAttribute('min', todayFormatted);
        
        // Set default value to today
        if (!journeyDateInput.value) {
            journeyDateInput.value = todayFormatted;
        }
    }
}

/**
 * Handle bus booking form submission
 * @param {Event} event - Form submit event
 */
function handleBookingSubmit(event) {
    const fromCity = document.getElementById('fromCity').value;
    const toCity = document.getElementById('toCity').value;
    
    if (fromCity === toCity) {
        event.preventDefault();
        alert('Departure and destination cities cannot be the same.');
        return false;
    }
    
    // Additional validation can be added here
    
    return true;
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
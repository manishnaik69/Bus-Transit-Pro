/**
 * Main JavaScript file for Bus Management System
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all Bootstrap components
    initTooltips();
    initPopovers();
    initAlertDismiss();
});

/**
 * Initialize Bootstrap tooltips
 */
function initTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });
}

/**
 * Initialize Bootstrap popovers
 */
function initPopovers() {
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    });
}

/**
 * Initialize auto-dismissing alerts
 */
function initAlertDismiss() {
    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
}

/**
 * Format a date in a user-friendly way
 * 
 * @param {Date} date - The date to format
 * @returns {string} The formatted date
 */
function formatDate(date) {
    if (!date) return '';
    var d = new Date(date);
    var month = '' + (d.getMonth() + 1);
    var day = '' + d.getDate();
    var year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [day, month, year].join('-');
}

/**
 * Format time in a user-friendly way
 * 
 * @param {string} timeString - The time string in format HH:MM:SS
 * @returns {string} The formatted time (HH:MM AM/PM)
 */
function formatTime(timeString) {
    if (!timeString) return '';
    var timeParts = timeString.split(':');
    var hours = parseInt(timeParts[0]);
    var minutes = timeParts[1];
    var ampm = hours >= 12 ? 'PM' : 'AM';
    
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    
    return hours + ':' + minutes + ' ' + ampm;
}

/**
 * Show a confirmation modal before performing an action
 * 
 * @param {string} message - The confirmation message
 * @param {function} callback - The callback function to call if confirmed
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Observer Pattern implementation for bus status changes
 */
class BusStatusObserver {
    constructor() {
        this.observers = [];
    }
    
    subscribe(fn) {
        this.observers.push(fn);
    }
    
    unsubscribe(fn) {
        this.observers = this.observers.filter(subscriber => subscriber !== fn);
    }
    
    notify(data) {
        this.observers.forEach(subscriber => subscriber(data));
    }
}

// Create a global bus status observer (singleton pattern)
const busStatusObserver = new BusStatusObserver();

// Example observer function
function logBusStatusChange(data) {
    console.log(`Bus ${data.busId} status changed from ${data.oldStatus} to ${data.newStatus}`);
}

// Subscribe the logger to bus status changes
busStatusObserver.subscribe(logBusStatusChange);

// Example function to update bus status
function updateBusStatus(busId, newStatus) {
    // Fetch the current bus info first
    fetch(`/buses/api/${busId}`)
        .then(response => response.json())
        .then(bus => {
            const oldStatus = bus.status;
            
            // Update the status via API
            fetch(`/buses/save`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({...bus, status: newStatus})
            })
            .then(response => {
                if (response.ok) {
                    // Notify all observers about the status change
                    busStatusObserver.notify({
                        busId: busId,
                        oldStatus: oldStatus,
                        newStatus: newStatus,
                        timestamp: new Date()
                    });
                }
            });
        });
}
package com.busmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class SimpleWebApp {

    public static void main(String[] args) {
        SpringApplication.run(SimpleWebApp.class, args);
    }
    
    // Simple bus class
    public static class Bus {
        private Long id;
        private String registrationNumber;
        private String manufacturer;
        private String model;
        private Integer capacity;
        private BusStatus status = BusStatus.ACTIVE;
        private LocalDateTime createdAt;
        
        public enum BusStatus {
            ACTIVE, MAINTENANCE, RETIRED
        }
        
        // Default constructor
        public Bus() {
            this.createdAt = LocalDateTime.now();
        }
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getRegistrationNumber() {
            return registrationNumber;
        }
        
        public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }
        
        public String getManufacturer() {
            return manufacturer;
        }
        
        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }
        
        public String getModel() {
            return model;
        }
        
        public void setModel(String model) {
            this.model = model;
        }
        
        public Integer getCapacity() {
            return capacity;
        }
        
        public void setCapacity(Integer capacity) {
            this.capacity = capacity;
        }
        
        public BusStatus getStatus() {
            return status;
        }
        
        public void setStatus(BusStatus status) {
            this.status = status;
        }
        
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        
        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
    
    // In-memory repository implementation using the Singleton pattern
    @Bean
    public InMemoryBusRepository busRepository() {
        return InMemoryBusRepository.getInstance();
    }
    
    // Example of Singleton pattern
    public static class InMemoryBusRepository {
        private static InMemoryBusRepository instance;
        private final Map<Long, Bus> buses = new ConcurrentHashMap<>();
        private final AtomicLong idCounter = new AtomicLong(1);
        
        private InMemoryBusRepository() {
            // Add some sample data
            Bus bus1 = new Bus();
            bus1.setId(idCounter.getAndIncrement());
            bus1.setRegistrationNumber("BUS-001");
            bus1.setManufacturer("Mercedes");
            bus1.setModel("Sprinter");
            bus1.setCapacity(40);
            bus1.setStatus(Bus.BusStatus.ACTIVE);
            bus1.setCreatedAt(LocalDateTime.now());
            buses.put(bus1.getId(), bus1);
            
            Bus bus2 = new Bus();
            bus2.setId(idCounter.getAndIncrement());
            bus2.setRegistrationNumber("BUS-002");
            bus2.setManufacturer("Volvo");
            bus2.setModel("9700");
            bus2.setCapacity(60);
            bus2.setStatus(Bus.BusStatus.ACTIVE);
            bus2.setCreatedAt(LocalDateTime.now());
            buses.put(bus2.getId(), bus2);
        }
        
        // Singleton getInstance method
        public static synchronized InMemoryBusRepository getInstance() {
            if (instance == null) {
                instance = new InMemoryBusRepository();
            }
            return instance;
        }
        
        public List<Bus> findAll() {
            return new ArrayList<>(buses.values());
        }
        
        public Optional<Bus> findById(Long id) {
            return Optional.ofNullable(buses.get(id));
        }
        
        public Bus save(Bus bus) {
            if (bus.getId() == null) {
                bus.setId(idCounter.getAndIncrement());
                bus.setCreatedAt(LocalDateTime.now());
            }
            buses.put(bus.getId(), bus);
            return bus;
        }
        
        public void deleteById(Long id) {
            buses.remove(id);
        }
        
        public Optional<Bus> findByRegistrationNumber(String registrationNumber) {
            return buses.values().stream()
                    .filter(b -> b.getRegistrationNumber().equals(registrationNumber))
                    .findFirst();
        }
        
        public List<Bus> findByStatus(Bus.BusStatus status) {
            return buses.values().stream()
                    .filter(b -> b.getStatus() == status)
                    .collect(Collectors.toList());
        }
    }
    
    // Service implementation - Example of Facade pattern
    @Service
    public static class BusService {
        private final InMemoryBusRepository busRepository;
        
        public BusService(InMemoryBusRepository busRepository) {
            this.busRepository = busRepository;
        }
        
        // Facade method - simplifies access to the repository
        public List<Bus> getAllBuses() {
            return busRepository.findAll();
        }
        
        public Optional<Bus> getBusById(Long id) {
            return busRepository.findById(id);
        }
        
        public Bus saveBus(Bus bus) {
            return busRepository.save(bus);
        }
        
        public void deleteBus(Long id) {
            busRepository.deleteById(id);
        }
        
        public List<Bus> getActiveBuses() {
            return busRepository.findByStatus(Bus.BusStatus.ACTIVE);
        }
        
        public Optional<Bus> findByRegistrationNumber(String registrationNumber) {
            return busRepository.findByRegistrationNumber(registrationNumber);
        }
    }
    
    // Home controller
    @Controller
    public static class HomeController {
        
        @GetMapping("/")
        public String home(Model model) {
            model.addAttribute("title", "Bus Management System");
            model.addAttribute("message", "Welcome to the Bus Management System");
            return "home";
        }
        
        @GetMapping("/about")
        public String about(Model model) {
            model.addAttribute("title", "About");
            model.addAttribute("message", "About the Bus Management System");
            return "about";
        }
    }
    
    // Bus controller - Factory pattern for bus creation
    @Controller
    @RequestMapping("/buses")
    public static class BusController {
        
        private final BusService busService;
        
        public BusController(BusService busService) {
            this.busService = busService;
        }
        
        @GetMapping
        public String listBuses(Model model) {
            model.addAttribute("buses", busService.getAllBuses());
            model.addAttribute("title", "Bus List");
            return "bus/list";
        }
        
        @GetMapping("/form")
        public String busForm(Model model) {
            // Factory pattern implementation - creating a new bus instance
            model.addAttribute("bus", createNewBus());
            model.addAttribute("title", "Add Bus");
            return "bus/form";
        }
        
        // Factory method for creating bus instances
        private Bus createNewBus() {
            return new Bus();
        }
        
        @PostMapping("/save")
        public String saveBus(@ModelAttribute("bus") Bus bus, Model model) {
            busService.saveBus(bus);
            return "redirect:/buses";
        }
        
        @GetMapping("/edit/{id}")
        public String editBus(@PathVariable Long id, Model model) {
            Bus bus = busService.getBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid bus ID: " + id));
            model.addAttribute("bus", bus);
            model.addAttribute("title", "Edit Bus");
            return "bus/form";
        }
        
        @GetMapping("/delete/{id}")
        public String deleteBus(@PathVariable Long id) {
            busService.deleteBus(id);
            return "redirect:/buses";
        }
        
        // REST API endpoints for AJAX calls
        @GetMapping("/api/all")
        @ResponseBody
        public List<Bus> getAllBusesApi() {
            return busService.getAllBuses();
        }
        
        @GetMapping("/api/{id}")
        @ResponseBody
        public ResponseEntity<Bus> getBusApi(@PathVariable Long id) {
            return busService.getBusById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    }
}
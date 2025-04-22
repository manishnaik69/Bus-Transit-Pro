package com.busmanagement.repository;

import com.busmanagement.model.City;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCityRepository {
    private static InMemoryCityRepository instance;
    private final Map<Long, City> cities = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    protected InMemoryCityRepository() {
        // Add sample data
        City mumbai = new City();
        mumbai.setId(idCounter.getAndIncrement());
        mumbai.setName("Mumbai");
        mumbai.setState("Maharashtra");
        mumbai.setDescription("Financial capital of India");
        mumbai.setCreatedAt(LocalDateTime.now());
        mumbai.setUpdatedAt(LocalDateTime.now());
        cities.put(mumbai.getId(), mumbai);

        City delhi = new City();
        delhi.setId(idCounter.getAndIncrement());
        delhi.setName("Delhi");
        delhi.setState("Delhi");
        delhi.setDescription("Capital city of India");
        delhi.setCreatedAt(LocalDateTime.now());
        delhi.setUpdatedAt(LocalDateTime.now());
        cities.put(delhi.getId(), delhi);

        City bangalore = new City();
        bangalore.setId(idCounter.getAndIncrement());
        bangalore.setName("Bangalore");
        bangalore.setState("Karnataka");
        bangalore.setDescription("Silicon Valley of India");
        bangalore.setCreatedAt(LocalDateTime.now());
        bangalore.setUpdatedAt(LocalDateTime.now());
        cities.put(bangalore.getId(), bangalore);

        City chennai = new City();
        chennai.setId(idCounter.getAndIncrement());
        chennai.setName("Chennai");
        chennai.setState("Tamil Nadu");
        chennai.setDescription("Gateway to South India");
        chennai.setCreatedAt(LocalDateTime.now());
        chennai.setUpdatedAt(LocalDateTime.now());
        cities.put(chennai.getId(), chennai);
    }

    // Singleton getInstance method
    public static synchronized InMemoryCityRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryCityRepository();
        }
        return instance;
    }

    public List<City> findAll() {
        return new ArrayList<>(cities.values());
    }

    public Optional<City> findById(Long id) {
        return Optional.ofNullable(cities.get(id));
    }

    public City save(City city) {
        if (city.getId() == null) {
            city.setId(idCounter.getAndIncrement());
            city.setCreatedAt(LocalDateTime.now());
        }
        city.setUpdatedAt(LocalDateTime.now());
        cities.put(city.getId(), city);
        return city;
    }

    public void deleteById(Long id) {
        cities.remove(id);
    }

    public Optional<City> findByName(String name) {
        return cities.values().stream()
                .filter(city -> city.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
package com.busmanagement;

import com.busmanagement.repository.InMemoryBusRepository;
import com.busmanagement.repository.InMemoryCityRepository;
import com.busmanagement.repository.InMemoryRouteRepository;
import com.busmanagement.repository.InMemoryScheduleRepository;
import com.busmanagement.repository.InMemoryTicketRepository;
import com.busmanagement.repository.InMemoryBookingRepository;
import com.busmanagement.repository.InMemoryPaymentRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpleWebApp {

    public static void main(String[] args) {
        SpringApplication.run(SimpleWebApp.class, args);
    }

    @Bean
    public InMemoryBusRepository busRepository() {
        return InMemoryBusRepository.getInstance();
    }

    @Bean
    public InMemoryCityRepository cityRepository() {
        return InMemoryCityRepository.getInstance();
    }

    @Bean
    public InMemoryRouteRepository routeRepository() {
        return InMemoryRouteRepository.getInstance();
    }

    @Bean
    public InMemoryScheduleRepository scheduleRepository() {
        return InMemoryScheduleRepository.getInstance();
    }

    @Bean
    public InMemoryTicketRepository ticketRepository() {
        return new InMemoryTicketRepository();
    }

    @Bean
    public InMemoryBookingRepository bookingRepository() {
        return new InMemoryBookingRepository();
    }

    @Bean
    public InMemoryPaymentRepository paymentRepository() {
        return new InMemoryPaymentRepository();
    }
}
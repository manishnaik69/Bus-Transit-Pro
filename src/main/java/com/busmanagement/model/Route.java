package com.busmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a route in the system
 */
@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String routeName;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private City source;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private City destination;

    @NotNull
    private Double distance; // in kilometers

    @NotNull
    private Integer estimatedDuration; // in minutes
    
    @NotNull
    private Double fareAmount; // in currency units

    @ElementCollection
    @CollectionTable(name = "route_stops", joinColumns = @JoinColumn(name = "route_id"))
    private List<RouteStop> stops = new ArrayList<>();

    private String description;
    
    @OneToMany(mappedBy = "route")
    private List<Schedule> schedules = new ArrayList<>();

    /**
     * Embeddable class for route stops
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteStop {
        private String stopName;
        private Integer stopOrder;
        private Integer distanceFromSource; // in kilometers
        private Integer stopDuration; // in minutes
    }
}
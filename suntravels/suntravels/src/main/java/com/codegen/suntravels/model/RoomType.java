package com.codegen.suntravels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room type name is required")
    private String name;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Positive(message = "Price must be positive")
    private Double pricePerPerson;

    @Positive(message = "Number of rooms must be positive")
    private Integer numberOfRooms;

    @Positive(message = "Maximum adults must be positive")
    private Integer maxAdults;
}

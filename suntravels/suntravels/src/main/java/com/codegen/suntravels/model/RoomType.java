package com.codegen.suntravels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a room type within a hotel contract.
 * A room type defines the characteristics of a room available under a specific contract,
 * including the price per person, the number of rooms available, and the maximum number of adults it can accommodate.
 * This class maps to the "room_types" table in the database.
 *
 * <p>Each room type is associated with a contract, and the room type details such as price and availability are determined by the contract.</p>
 */
@Entity
@Table(name = "room_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomType {

    /**
     * The unique identifier for the room type.
     * This is the primary key in the "room_types" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the room type (e.g., Single, Double, Suite).
     * This field must not be blank.
     */
    @NotBlank(message = "Room type name is required")
    private String name;

    /**
     * The contract associated with this room type.
     * This is a many-to-one relationship with the {@link Contract} entity, indicating that a room type belongs to a specific contract.
     */
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    /**
     * The price per person for this room type.
     * This value must be positive, indicating the cost of staying in the room type.
     */
    @Positive(message = "Price must be positive")
    private Double pricePerPerson;

    /**
     * The number of rooms available for this room type.
     * This value must be positive, indicating the number of rooms of this type available under the contract.
     */
    @Positive(message = "Number of rooms must be positive")
    private Integer numberOfRooms;

    /**
     * The maximum number of adults that can stay in this room type.
     * This value must be positive, indicating the room's capacity for adults.
     */
    @Positive(message = "Maximum adults must be positive")
    private Integer maxAdults;
}

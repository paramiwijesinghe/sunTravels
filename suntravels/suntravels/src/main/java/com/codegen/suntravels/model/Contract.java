package com.codegen.suntravels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a contract between a hotel and the system.
 * A contract defines the pricing and availability terms for various room types in a hotel.
 * It contains details like the hotel, the contract's start and end dates, markup percentage,
 * and the associated room types under this contract.
 *
 * <p>This entity is mapped to the "contracts" table in the database, with relationships to the
 * {@link Hotel} and {@link RoomType} entities.</p>
 */
@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    /**
     * The unique identifier for the contract.
     * This is the primary key in the "contracts" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The hotel associated with this contract.
     * This is a many-to-one relationship with the {@link Hotel} entity, indicating which hotel this contract belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    /**
     * The start date of the contract.
     * This field must not be null, and it indicates when the contract becomes effective.
     */
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    /**
     * The end date of the contract.
     * This field must not be null, and it indicates when the contract expires.
     */
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    /**
     * The markup percentage applied to the room rates under this contract.
     * This value must be positive, indicating the percentage increase over the base rate.
     */
    @Positive(message = "Markup percentage must be positive")
    private Double markupPercentage;

    /**
     * A list of room types available under this contract.
     * This is a one-to-many relationship with the {@link RoomType} entity.
     * The contract can have multiple room types with different prices and availability.
     * The list is cascaded with all operations and orphan removal is enabled.
     */
    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes = new ArrayList<>();
}

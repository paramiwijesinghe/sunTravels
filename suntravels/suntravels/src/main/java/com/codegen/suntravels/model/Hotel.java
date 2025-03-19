package com.codegen.suntravels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hotel entity.
 * A hotel contains information about its name, location, contact details, and the associated contracts.
 * This class maps to the "hotels" table in the database.
 *
 * <p>The hotel can have multiple contracts, which are represented by the {@link Contract} entity,
 * forming a one-to-many relationship between the hotel and its contracts.</p>
 */
@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "contracts")  // Prevent recursion
public class Hotel {

    /**
     * The unique identifier for the hotel.
     * This is the primary key in the "hotels" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the hotel.
     * This field must not be blank.
     */
    @NotBlank(message = "Hotel name is required")
    private String name;

    /**
     * The location of the hotel.
     * This field provides information about where the hotel is situated.
     */
    private String location;

    /**
     * The contact details of the hotel.
     * This field contains information like phone number or email for contacting the hotel.
     */
    private String contactDetails;

    /**
     * A list of contracts associated with this hotel.
     * This is a one-to-many relationship with the {@link Contract} entity, indicating that a hotel can have multiple contracts.
     * The contracts are cascaded with all operations, and orphan removal is enabled.
     */
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Contract> contracts;
}

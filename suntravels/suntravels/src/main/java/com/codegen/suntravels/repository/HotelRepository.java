package com.codegen.suntravels.repository;

import com.codegen.suntravels.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on {@link Hotel} entities.
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations
 * such as saving, deleting, and finding hotels by their unique identifier (ID).
 *
 * <p>By using Spring Data JPA, this repository simplifies database operations related to the Hotel entity
 * without needing to write custom SQL queries.</p>
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}

package com.codegen.suntravels.repository;

import com.codegen.suntravels.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on {@link RoomType} entities.
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations
 * for the RoomType entity, such as saving, deleting, and finding room types by their unique identifier (ID).
 *
 * <p>By using Spring Data JPA, this repository provides additional custom queries to retrieve room types
 * based on contract-specific conditions, such as the number of available rooms and the maximum number of adults.</p>
 */
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    /**
     * Finds all room types associated with a specific contract.
     *
     * @param contractId the unique identifier of the contract
     * @return a list of room types associated with the specified contract
     */
    List<RoomType> findByContractId(Long contractId);

    /**
     * Finds room types associated with a specific contract that can accommodate at least the specified number of adults.
     *
     * @param contractId the unique identifier of the contract
     * @param requiredAdults the minimum number of adults the room type should accommodate
     * @return a list of room types from the specified contract that can accommodate the required number of adults
     */
    @Query("SELECT rt FROM RoomType rt WHERE rt.contract.id = :contractId AND rt.maxAdults >= :requiredAdults")
    List<RoomType> findValidRoomTypesForContract(
            @Param("contractId") Long contractId,
            @Param("requiredAdults") Integer requiredAdults);

    /**
     * Finds room types associated with a specific contract that have at least the specified number of rooms
     * and can accommodate the required number of adults.
     *
     * @param contractId the unique identifier of the contract
     * @param requiredRooms the minimum number of rooms that should be available
     * @param requiredAdults the minimum number of adults the room type should accommodate
     * @return a list of room types that meet both the room and adult requirements for the specified contract
     */
    @Query("SELECT rt FROM RoomType rt WHERE rt.contract.id = :contractId AND rt.numberOfRooms >= :requiredRooms AND rt.maxAdults >= :requiredAdults")
    List<RoomType> findAvailableRoomTypesForContract(
            @Param("contractId") Long contractId,
            @Param("requiredRooms") Integer requiredRooms,
            @Param("requiredAdults") Integer requiredAdults);
}

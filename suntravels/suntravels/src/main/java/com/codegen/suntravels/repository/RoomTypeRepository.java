package com.codegen.suntravels.repository;

import com.codegen.suntravels.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findByContractId(Long contractId);

    @Query("SELECT rt FROM RoomType rt WHERE rt.contract.id = :contractId AND rt.maxAdults >= :requiredAdults")
    List<RoomType> findValidRoomTypesForContract(
            @Param("contractId") Long contractId,
            @Param("requiredAdults") Integer requiredAdults);

    @Query("SELECT rt FROM RoomType rt WHERE rt.contract.id = :contractId AND rt.numberOfRooms >= :requiredRooms AND rt.maxAdults >= :requiredAdults")
    List<RoomType> findAvailableRoomTypesForContract(
            @Param("contractId") Long contractId,
            @Param("requiredRooms") Integer requiredRooms,
            @Param("requiredAdults") Integer requiredAdults);
}
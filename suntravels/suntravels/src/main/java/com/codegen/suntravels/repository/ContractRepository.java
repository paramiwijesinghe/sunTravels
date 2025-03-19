package com.codegen.suntravels.repository;

import com.codegen.suntravels.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Repository interface for performing CRUD operations on {@link Contract} entities.
 * This interface extends {@link JpaRepository}, which provides basic CRUD operations.
 * It also defines custom queries to retrieve contracts based on specific date ranges.
 *
 * <p>By using Spring Data JPA, this repository provides methods for querying the database
 * and retrieving contract data based on certain conditions.</p>
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    /**
     * Finds contracts that expire between the specified start and end date.
     * This version supports pagination.
     *
     * @param fromDate the starting date for the contract expiration range
     * @param toDate the ending date for the contract expiration range
     * @param pageable pagination information
     * @return a page of contracts that expire within the specified date range
     */
    @Query("SELECT c FROM Contract c WHERE c.endDate BETWEEN :fromDate AND :toDate")
    Page<Contract> findContractsExpiringBetween(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

    /**
     * Finds contracts that are valid for the given check-in and check-out dates.
     * This version supports pagination.
     * A contract is considered valid if both the check-in and check-out dates
     * fall within the contract's start and end date.
     *
     * @param checkInDate the check-in date to check against contract validity
     * @param checkOutDate the check-out date to check against contract validity
     * @param pageable pagination information
     * @return a page of contracts that are valid for the specified date range
     */
    @Query("SELECT c FROM Contract c WHERE :checkInDate BETWEEN c.startDate AND c.endDate AND :checkOutDate BETWEEN c.startDate AND c.endDate")
    Page<Contract> findValidContractsForDateRange(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            Pageable pageable);
}

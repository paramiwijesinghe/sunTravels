package com.codegen.suntravels.repository;

import com.codegen.suntravels.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c WHERE c.endDate BETWEEN :fromDate AND :toDate")
    List<Contract> findContractsExpiringBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("SELECT c FROM Contract c WHERE :checkInDate BETWEEN c.startDate AND c.endDate AND :checkOutDate BETWEEN c.startDate AND c.endDate")
    List<Contract> findValidContractsForDateRange(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);
}
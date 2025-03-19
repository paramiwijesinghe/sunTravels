package com.codegen.suntravels.controller;

import com.codegen.suntravels.dto.ContractDTO;
import com.codegen.suntravels.dto.ContractExpiryReportDTO;
import com.codegen.suntravels.service.ContractService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing contracts.
 */
@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractController {

    private final ContractService contractService;

    /**
     * Constructor to initialize ContractController with ContractService.
     *
     * @param contractService the contract service to handle business logic
     */
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    /**
     * Retrieves all contracts.
     *
     * @return a list of all contracts
     */
    @GetMapping("/getallcontracts")
    public ResponseEntity<List<ContractDTO>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    /**
     * Retrieves a contract by its ID.
     *
     * @param id the contract ID
     * @return the contract details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    /**
     * Creates a new contract.
     *
     * @param contractDTO the contract data transfer object
     * @return the created contract
     */
    @PostMapping("/createcontract")
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.createContract(contractDTO));
    }

    /**
     * Updates an existing contract.
     *
     * @param id          the contract ID
     * @param contractDTO the updated contract data
     * @return the updated contract
     */
    @PutMapping("/updatecontract/{id}")
    public ResponseEntity<ContractDTO> updateContract(@PathVariable Long id, @RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.updateContract(id, contractDTO));
    }

    /**
     * Deletes a contract by its ID.
     *
     * @param id the contract ID
     * @return a response entity with no content
     */
    @DeleteMapping("/deletecontract/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves contracts that are expiring within a given date range.
     *
     * @param fromDate the start date of the range
     * @param toDate   the end date of the range
     * @return a list of expiring contracts
     */
    @GetMapping("/expiring")
    public ResponseEntity<List<ContractExpiryReportDTO>> getContractsExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(contractService.getContractsExpiringBetween(fromDate, toDate, page, size));
    }


    /**
     * Searches contracts based on hotel name and optional date range.
     *
     * @param hotelName optional hotel name filter
     * @param startDate optional start date filter
     * @param endDate   optional end date filter
     * @return a list of matching contracts
     */
    @GetMapping("/search")
    public ResponseEntity<List<ContractDTO>> searchContracts(
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(contractService.searchContracts(hotelName, startDate, endDate));
    }
}
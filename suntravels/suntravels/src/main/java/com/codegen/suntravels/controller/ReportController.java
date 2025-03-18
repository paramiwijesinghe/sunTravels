//package com.codegen.suntravels.controller;
//
//import com.codegen.suntravels.dto.ContractExpiryReportDTO;
//import com.codegen.suntravels.dto.RoomAvailabilityReportDTO;
//import com.codegen.suntravels.service.SearchService;
//import com.codegen.suntravels.dto.ReportRequestDTO;
//import com.codegen.suntravels.service.ContractService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reports")
//@CrossOrigin(origins = "*")
//public class ReportController {
//
//    private final ContractService contractService;
//    private final SearchService searchService; // Inject SearchService
//
//    @Autowired
//    public ReportController(ContractService contractService, SearchService searchService) {
//        this.contractService = contractService;
//        this.searchService = searchService;
//    }
//
//    @GetMapping("/contracts/expiring")
//    public ResponseEntity<List<ContractExpiryReportDTO>> getContractsExpiringReport(
//            @RequestParam("fromDate") String fromDate,
//            @RequestParam("toDate") String toDate) {
//
//        List<ContractExpiryReportDTO> report = contractService.getContractsExpiringBetween(
//                java.time.LocalDate.parse(fromDate),
//                java.time.LocalDate.parse(toDate));
//
//        return ResponseEntity.ok(report);
//    }
//
//    @GetMapping("/availability")
//    public ResponseEntity<List<RoomAvailabilityReportDTO>> getRoomAvailabilityReport(
//            @RequestParam("fromDate") String fromDate,
//            @RequestParam("toDate") String toDate) {
//
//        // Use the injected instance instead of calling as a static method
//        List<RoomAvailabilityReportDTO> report = searchService.generateRoomAvailabilityReport(
//                java.time.LocalDate.parse(fromDate),
//                java.time.LocalDate.parse(toDate));
//
//        return ResponseEntity.ok(report);
//    }
//
//
//}
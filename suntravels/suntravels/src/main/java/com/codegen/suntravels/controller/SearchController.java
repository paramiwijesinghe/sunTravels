package com.codegen.suntravels.controller;

import com.codegen.suntravels.dto.SearchRequestDTO;
import com.codegen.suntravels.dto.SearchResultDTO;
import com.codegen.suntravels.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling room search requests.
 */
@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final SearchService searchService;

    /**
     * Constructor to initialize SearchController with SearchService.
     *
     * @param searchService the search service to handle search logic
     */
    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Searches for available rooms based on the provided search criteria with pagination.
     *
     * @param searchRequestDTO the search request containing criteria such as dates and location
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @return a page of available rooms matching the criteria
     */
    @PostMapping
    public ResponseEntity<Page<SearchResultDTO>> searchRooms(@RequestBody SearchRequestDTO searchRequestDTO,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "1") int size) {

        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Call the service method to get paginated results
        Page<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO, pageable);

        // Return the paginated response
        return ResponseEntity.ok(results);
    }
}

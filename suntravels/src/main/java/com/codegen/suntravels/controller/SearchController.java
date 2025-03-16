package com.codegen.suntravels.controller;

import com.codegen.suntravels.dto.SearchRequestDTO;
import com.codegen.suntravels.dto.SearchResultDTO;
import com.codegen.suntravels.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<SearchResultDTO>> searchRooms(@RequestBody SearchRequestDTO searchRequestDTO) {
        List<SearchResultDTO> results = searchService.searchAvailableRooms(searchRequestDTO);
        return ResponseEntity.ok(results);
    }
}
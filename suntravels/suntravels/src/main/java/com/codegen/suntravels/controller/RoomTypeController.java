package com.codegen.suntravels.controller;

import com.codegen.suntravels.dto.RoomTypeDTO;
import com.codegen.suntravels.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing room types.
 */
@RestController
@RequestMapping("/api/room-types")
@CrossOrigin(origins = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    /**
     * Constructor to initialize RoomTypeController with RoomTypeService.
     *
     * @param roomTypeService the room type service to handle business logic
     */
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    /**
     * Retrieves room types associated with a specific contract ID.
     *
     * @param contractId the contract ID
     * @return a list of room types related to the contract
     */
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypesByContractId(@PathVariable Long contractId) {
        List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesByContractId(contractId);
        return ResponseEntity.ok(roomTypes);
    }

    /**
     * Retrieves a room type by its ID.
     *
     * @param id the room type ID
     * @return the room type details
     */
    @GetMapping("/get-room-type-by-id/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long id) {
        RoomTypeDTO roomTypeDTO = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(roomTypeDTO);
    }

    /**
     * Creates a new room type.
     *
     * @param roomTypeDTO the room type data transfer object
     * @return the created room type
     */
    @PostMapping("/create-room-type")
    public ResponseEntity<RoomTypeDTO> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeDTO createdRoomType = roomTypeService.createRoomType(roomTypeDTO);
        return ResponseEntity.ok(createdRoomType);
    }

    /**
     * Updates an existing room type.
     *
     * @param id          the room type ID
     * @param roomTypeDTO the updated room type data
     * @return the updated room type
     */
    @PutMapping("/update-room-type/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeDTO updatedRoomType = roomTypeService.updateRoomType(id, roomTypeDTO);
        return ResponseEntity.ok(updatedRoomType);
    }

    /**
     * Deletes a room type by its ID.
     *
     * @param id the room type ID
     * @return a response entity with no content
     */
    @DeleteMapping("/delete-room-type/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}
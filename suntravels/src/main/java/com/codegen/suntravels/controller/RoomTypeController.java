package com.codegen.suntravels.controller;

import com.codegen.suntravels.dto.RoomTypeDTO;
import com.codegen.suntravels.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@CrossOrigin(origins = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypesByContractId(@PathVariable Long contractId) {
        List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesByContractId(contractId);
        return ResponseEntity.ok(roomTypes);
    }

    @GetMapping("/get-room-type-by-id/{id}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long id) {
        RoomTypeDTO roomTypeDTO = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(roomTypeDTO);
    }

    @PostMapping("/create-room-type")
    public ResponseEntity<RoomTypeDTO> createRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeDTO createdRoomType = roomTypeService.createRoomType(roomTypeDTO);
        return ResponseEntity.ok(createdRoomType);
    }

    @PutMapping("/update-room-type/{id}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeDTO roomTypeDTO) {
        RoomTypeDTO updatedRoomType = roomTypeService.updateRoomType(id, roomTypeDTO);
        return ResponseEntity.ok(updatedRoomType);
    }

    @DeleteMapping("/delete-room-type/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
}

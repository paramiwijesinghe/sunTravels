package com.codegen.suntravels.service;

import com.codegen.suntravels.dto.RoomTypeDTO;
import com.codegen.suntravels.exception.ResourceNotFoundException;
import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.RoomTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to room types.
 * This service provides methods for creating, updating, deleting, retrieving room types.
 */
@Transactional
@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, ContractRepository contractRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.contractRepository = contractRepository;
    }

    /**
     * Retrieves all room types associated with a given contract ID.
     *
     * @param contractId the ID of the contract for which room types are to be retrieved
     * @return a list of {@link RoomTypeDTO} representing the room types of the contract
     */
    public List<RoomTypeDTO> getRoomTypesByContractId(Long contractId) {
        return roomTypeRepository.findByContractId(contractId).stream()
                                 .map(this::convertToDTO)
                                 .collect(Collectors.toList());
    }

    /**
     * Retrieves a room type by its ID.
     *
     * @param id the unique identifier of the room type
     * @return the {@link RoomTypeDTO} representing the room type
     * @throws ResourceNotFoundException if the room type with the specified ID is not found
     */
    public RoomTypeDTO getRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Room type not found with id: " + id));
        return convertToDTO(roomType);
    }

    /**
     * Creates a new room type based on the provided {@link RoomTypeDTO}.
     *
     * @param roomTypeDTO the DTO containing room type details
     * @return the created {@link RoomTypeDTO}
     */
    public RoomTypeDTO createRoomType(RoomTypeDTO roomTypeDTO) {
        RoomType roomType = convertToEntity(roomTypeDTO);
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        return convertToDTO(savedRoomType);
    }

    /**
     * Updates an existing room type by its ID with the details from the provided {@link RoomTypeDTO}.
     *
     * @param id the unique identifier of the room type to update
     * @param roomTypeDTO the DTO containing updated room type details
     * @return the updated {@link RoomTypeDTO}
     * @throws ResourceNotFoundException if the room type with the specified ID is not found
     */
    public RoomTypeDTO updateRoomType(Long id, RoomTypeDTO roomTypeDTO) {
        RoomType existingRoomType = roomTypeRepository.findById(id)
                                                      .orElseThrow(() -> new ResourceNotFoundException("Room type not found with id: " + id));

        existingRoomType.setName(roomTypeDTO.getName());
        existingRoomType.setPricePerPerson(roomTypeDTO.getPricePerPerson());
        existingRoomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
        existingRoomType.setMaxAdults(roomTypeDTO.getMaxAdults());

        RoomType updatedRoomType = roomTypeRepository.save(existingRoomType);
        return convertToDTO(updatedRoomType);
    }

    /**
     * Deletes a room type by its ID.
     *
     * @param id the unique identifier of the room type to delete
     * @throws ResourceNotFoundException if the room type with the specified ID is not found
     */
    public void deleteRoomType(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room type not found with id: " + id);
        }
        roomTypeRepository.deleteById(id);
    }

    /**
     * Converts a {@link RoomType} entity to a {@link RoomTypeDTO}.
     *
     * @param roomType the room type entity to convert
     * @return the converted {@link RoomTypeDTO}
     */
    private RoomTypeDTO convertToDTO(RoomType roomType) {
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setId(roomType.getId());
        roomTypeDTO.setName(roomType.getName());
        roomTypeDTO.setContractId(roomType.getContract().getId());
        roomTypeDTO.setPricePerPerson(roomType.getPricePerPerson());
        roomTypeDTO.setNumberOfRooms(roomType.getNumberOfRooms());
        roomTypeDTO.setMaxAdults(roomType.getMaxAdults());
        return roomTypeDTO;
    }

    /**
     * Converts a {@link RoomTypeDTO} to a {@link RoomType} entity.
     *
     * @param roomTypeDTO the room type DTO to convert
     * @return the converted {@link RoomType} entity
     * @throws IllegalArgumentException if the provided DTO is null
     * @throws ResourceNotFoundException if the contract with the provided ID is not found
     */
    private RoomType convertToEntity(RoomTypeDTO roomTypeDTO) {
        if (roomTypeDTO == null) {
            throw new IllegalArgumentException("RoomTypeDTO cannot be null");
        }
        RoomType roomType = new RoomType();
        roomType.setId(roomTypeDTO.getId());
        roomType.setName(roomTypeDTO.getName());

        if (roomTypeDTO.getContractId() != null) {
            Contract contract = contractRepository.findById(roomTypeDTO.getContractId())
                                                  .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + roomTypeDTO.getContractId()));
            roomType.setContract(contract);
        }

        roomType.setPricePerPerson(roomTypeDTO.getPricePerPerson());
        roomType.setNumberOfRooms(roomTypeDTO.getNumberOfRooms());
        roomType.setMaxAdults(roomTypeDTO.getMaxAdults());

        return roomType;
    }

}

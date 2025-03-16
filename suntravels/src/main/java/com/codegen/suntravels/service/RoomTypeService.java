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

@Transactional
@Service
public class RoomTypeService {

    @Autowired
    private final RoomTypeRepository roomTypeRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, ContractRepository contractRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.contractRepository = contractRepository;
    }

    public List<RoomTypeDTO> getRoomTypesByContractId(Long contractId) {
        return roomTypeRepository.findByContractId(contractId).stream()
                                 .map(this::convertToDTO)
                                 .collect(Collectors.toList());
    }

    public RoomTypeDTO getRoomTypeById(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException("Room type not found with id: " + id));
        return convertToDTO(roomType);
    }

    public RoomTypeDTO createRoomType(RoomTypeDTO roomTypeDTO) {
        RoomType roomType = convertToEntity(roomTypeDTO);
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        return convertToDTO(savedRoomType);
    }

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

    public void deleteRoomType(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room type not found with id: " + id);
        }
        roomTypeRepository.deleteById(id);
    }

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


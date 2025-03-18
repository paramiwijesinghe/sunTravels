package com.codegen.suntravels;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.service.ContractService;
import com.codegen.suntravels.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
        contract.setId(1L);
        contract.setHotelName("Sample Hotel");
    }

    @Test
    void testGetContractById_ShouldReturnContract_WhenContractExists() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Contract foundContract = contractService.getContractById(1L);

        assertNotNull(foundContract);
        assertEquals(1L, foundContract.getId());
        assertEquals("Sample Hotel", foundContract.getHotelName());
    }

    @Test
    void testGetContractById_ShouldThrowException_WhenContractDoesNotExist() {
        when(contractRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> contractService.getContractById(2L));

        assertEquals("Contract not found", exception.getMessage());
    }
}

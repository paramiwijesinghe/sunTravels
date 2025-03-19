package com.codegen.suntravels;


import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel testHotel;

    @BeforeEach
    void setUp() {
        // Create a test hotel
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setContactDetails("Test Contact");
        testHotel = hotelRepository.save(testHotel);
    }

    @Test
    public void testSaveContract() {
        // Arrange
        Contract contract = new Contract();
        contract.setHotel(testHotel);
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusMonths(6));
        contract.setMarkupPercentage(10.0);

        // Act
        Contract savedContract = contractRepository.save(contract);

        // Assert
        assertThat(savedContract).isNotNull();
        assertThat(savedContract.getId()).isNotNull();
        assertThat(savedContract.getHotel().getId()).isEqualTo(testHotel.getId());
    }

    @Test
    public void testFindContractsExpiringBetween() {
        // Arrange
        Contract contract1 = new Contract();
        contract1.setHotel(testHotel);
        contract1.setStartDate(LocalDate.now());
        contract1.setEndDate(LocalDate.now().plusMonths(1));
        contract1.setMarkupPercentage(10.0);

        Contract contract2 = new Contract();
        contract2.setHotel(testHotel);
        contract2.setStartDate(LocalDate.now());
        contract2.setEndDate(LocalDate.now().plusMonths(2));
        contract2.setMarkupPercentage(15.0);

        Contract contract3 = new Contract();
        contract3.setHotel(testHotel);
        contract3.setStartDate(LocalDate.now());
        contract3.setEndDate(LocalDate.now().plusMonths(6));
        contract3.setMarkupPercentage(20.0);

        contractRepository.save(contract1);
        contractRepository.save(contract2);
        contractRepository.save(contract3);

        // Act
        LocalDate fromDate = LocalDate.now().plusDays(15);
        LocalDate toDate = LocalDate.now().plusMonths(3);
        List<Contract> expiringContracts = contractRepository.findContractsExpiringBetween(fromDate, toDate);

        // Assert
        assertThat(expiringContracts).hasSize(2);
        assertThat(expiringContracts).extracting(Contract::getMarkupPercentage).contains(10.0, 15.0);
    }

    @Test
    public void testFindValidContractsForDateRange() {
        // Arrange
        Contract contract1 = new Contract();
        contract1.setHotel(testHotel);
        contract1.setStartDate(LocalDate.now().minusMonths(1));
        contract1.setEndDate(LocalDate.now().plusMonths(2));
        contract1.setMarkupPercentage(10.0);

        Contract contract2 = new Contract();  // Outside date range
        contract2.setHotel(testHotel);
        contract2.setStartDate(LocalDate.now().plusMonths(3));
        contract2.setEndDate(LocalDate.now().plusMonths(5));
        contract2.setMarkupPercentage(15.0);

        contractRepository.save(contract1);
        contractRepository.save(contract2);

        // Act
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(7);
        List<Contract> validContracts = contractRepository.findValidContractsForDateRange(checkInDate, checkOutDate);

        // Assert
        assertThat(validContracts).hasSize(1);
        assertThat(validContracts.get(0).getMarkupPercentage()).isEqualTo(10.0);
    }
}
package com.codegen.suntravels;


import com.codegen.suntravels.model.Contract;
import com.codegen.suntravels.model.Hotel;
import com.codegen.suntravels.model.RoomType;
import com.codegen.suntravels.repository.HotelRepository;
import com.codegen.suntravels.repository.ContractRepository;
import com.codegen.suntravels.repository.RoomTypeRepository;
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
public class RoomTypeRepositoryTest {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel testHotel;
    private Contract testContract;

    @BeforeEach
    void setUp() {
        // Create test hotel
        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setLocation("Test Location");
        testHotel.setContactDetails("Test Contact");
        testHotel = hotelRepository.save(testHotel);

        // Create test contract
        testContract = new Contract();
        testContract.setHotel(testHotel);
        testContract.setStartDate(LocalDate.now());
        testContract.setEndDate(LocalDate.now().plusMonths(6));
        testContract.setMarkupPercentage(10.0);
        testContract = contractRepository.save(testContract);
    }

    @Test
    public void testSaveRoomType() {
        // Arrange
        RoomType roomType = new RoomType();
        roomType.setName("Deluxe Room");
        roomType.setContract(testContract);
        roomType.setPricePerPerson(100.0);
        roomType.setNumberOfRooms(5);
        roomType.setMaxAdults(2);

        // Act
        RoomType savedRoomType = roomTypeRepository.save(roomType);

        // Assert
        assertThat(savedRoomType).isNotNull();
        assertThat(savedRoomType.getId()).isNotNull();
        assertThat(savedRoomType.getName()).isEqualTo("Deluxe Room");
        assertThat(savedRoomType.getContract().getId()).isEqualTo(testContract.getId());
    }

    @Test
    public void testFindByContractId() {
        // Arrange
        RoomType roomType1 = new RoomType();
        roomType1.setName("Deluxe Room");
        roomType1.setContract(testContract);
        roomType1.setPricePerPerson(100.0);
        roomType1.setNumberOfRooms(5);
        roomType1.setMaxAdults(2);

        RoomType roomType2 = new RoomType();
        roomType2.setName("Family Room");
        roomType2.setContract(testContract);
        roomType2.setPricePerPerson(150.0);
        roomType2.setNumberOfRooms(3);
        roomType2.setMaxAdults(4);

        roomTypeRepository.save(roomType1);
        roomTypeRepository.save(roomType2);

        // Act
        List<RoomType> roomTypes = roomTypeRepository.findByContractId(testContract.getId());

        // Assert
        assertThat(roomTypes).hasSize(2);
        assertThat(roomTypes).extracting(RoomType::getName).containsExactlyInAnyOrder("Deluxe Room", "Family Room");
    }

    @Test
    public void testFindValidRoomTypesForContract() {
        // Arrange
        RoomType roomType1 = new RoomType();
        roomType1.setName("Deluxe Room");
        roomType1.setContract(testContract);
        roomType1.setPricePerPerson(100.0);
        roomType1.setNumberOfRooms(5);
        roomType1.setMaxAdults(2);

        RoomType roomType2 = new RoomType();
        roomType2.setName("Family Room");
        roomType2.setContract(testContract);
        roomType2.setPricePerPerson(150.0);
        roomType2.setNumberOfRooms(3);
        roomType2.setMaxAdults(4);

        roomTypeRepository.save(roomType1);
        roomTypeRepository.save(roomType2);

        // Act - looking for rooms that can handle 3 adults
        List<RoomType> validRoomTypes = roomTypeRepository.findValidRoomTypesForContract(testContract.getId(), 3);

        // Assert
        assertThat(validRoomTypes).hasSize(1);
        assertThat(validRoomTypes.get(0).getName()).isEqualTo("Family Room");
    }

    @Test
    public void testFindAvailableRoomTypesForContract() {
        // Arrange
        RoomType roomType1 = new RoomType();
        roomType1.setName("Deluxe Room");
        roomType1.setContract(testContract);
        roomType1.setPricePerPerson(100.0);
        roomType1.setNumberOfRooms(5);
        roomType1.setMaxAdults(2);

        RoomType roomType2 = new RoomType();
        roomType2.setName("Family Room");
        roomType2.setContract(testContract);
        roomType2.setPricePerPerson(150.0);
        roomType2.setNumberOfRooms(3);
        roomType2.setMaxAdults(4);

        roomTypeRepository.save(roomType1);
        roomTypeRepository.save(roomType2);

        // Act - looking for 2 rooms that can handle 3 adults
        List<RoomType> availableRoomTypes = roomTypeRepository.findAvailableRoomTypesForContract(
                testContract.getId(), 2, 3);

        // Assert
        assertThat(availableRoomTypes).hasSize(1);
        assertThat(availableRoomTypes.get(0).getName()).isEqualTo("Family Room");
    }
}

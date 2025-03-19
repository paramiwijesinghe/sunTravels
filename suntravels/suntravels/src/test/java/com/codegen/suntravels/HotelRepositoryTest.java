package com.codegen.suntravels;

import com.codegen.suntravels.model.Hotel;
import org.junit.jupiter.api.Test;
import com.codegen.suntravels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void testSaveHotel() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setContactDetails("Test Contact");

        // Act
        Hotel savedHotel = hotelRepository.save(hotel);

        // Assert
        assertThat(savedHotel).isNotNull();
        assertThat(savedHotel.getId()).isNotNull();
        assertThat(savedHotel.getName()).isEqualTo("Test Hotel");
    }

    @Test
    public void testFindHotelById() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setContactDetails("Test Contact");
        Hotel savedHotel = hotelRepository.save(hotel);

        // Act
        Optional<Hotel> foundHotel = hotelRepository.findById(savedHotel.getId());

        // Assert
        assertThat(foundHotel).isPresent();
        assertThat(foundHotel.get().getName()).isEqualTo("Test Hotel");
    }

    @Test
    public void testFindAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel();
        hotel1.setName("Test Hotel 1");
        hotel1.setLocation("Test Location 1");
        hotel1.setContactDetails("Test Contact 1");

        Hotel hotel2 = new Hotel();
        hotel2.setName("Test Hotel 2");
        hotel2.setLocation("Test Location 2");
        hotel2.setContactDetails("Test Contact 2");

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        // Act
        List<Hotel> hotels = hotelRepository.findAll();

        // Assert
        assertThat(hotels).hasSize(2);
        assertThat(hotels).extracting(Hotel::getName).contains("Test Hotel 1", "Test Hotel 2");
    }

    @Test
    public void testDeleteHotel() {
        // Arrange
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");
        hotel.setContactDetails("Test Contact");
        Hotel savedHotel = hotelRepository.save(hotel);

        // Act
        hotelRepository.deleteById(savedHotel.getId());
        Optional<Hotel> deletedHotel = hotelRepository.findById(savedHotel.getId());

        // Assert
        assertThat(deletedHotel).isEmpty();
    }
}
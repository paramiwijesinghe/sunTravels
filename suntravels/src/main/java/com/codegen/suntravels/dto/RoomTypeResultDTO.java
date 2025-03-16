package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeResultDTO {
    private Long id;
    private String name;
    private Double totalPrice;
    private boolean available;
    private Integer maxAdults;
    private Integer availableRooms;
}

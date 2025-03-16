package com.codegen.suntravels.dto;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDTO {
    private Long id;
    private String name;
    private Long contractId;
    private Double pricePerPerson;
    private Integer numberOfRooms;
    private Integer maxAdults;
}

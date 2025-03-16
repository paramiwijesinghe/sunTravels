package com.codegen.suntravels.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAssignmentDTO {
    private Long roomTypeId;
    private String roomTypeName;
    private Integer adultsAssigned;
    private Integer maxAdults;
    private Double roomPrice;
}
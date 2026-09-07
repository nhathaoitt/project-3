package com.devon.building.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignmentCustomerDTO {
    @NotNull(message = "BuildingId not be bull")
    Long customerId;
    @NotNull(message = "StaffId not be bull")
    List<Long> staffIds;
}

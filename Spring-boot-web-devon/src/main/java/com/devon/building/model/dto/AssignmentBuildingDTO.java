package com.devon.building.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignmentBuildingDTO {
    @NotNull(message = "BuildingId not be bull")
    Long buildingId;
    @NotNull(message = "StaffId not be bull")
    List<Long> staffIds;
}
